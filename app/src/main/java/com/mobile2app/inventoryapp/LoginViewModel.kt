package com.mobile2app.inventoryapp

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: LoginRepository) : ViewModel() {

    val username = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val confirmPassword = MutableLiveData<String>()

    val authenticationResult = MutableLiveData(LoginRepository.AuthResult.NO_LOGIN_ATTEMPT)

    val addUser = MutableLiveData(false)

    private val formComplete: Boolean
        get() {
            return when {
                username.value.isNullOrBlank() -> false
                password.value.isNullOrBlank() -> false
                confirmPassword.value.isNullOrBlank() && addUser.value!! -> false
                else -> true
            }
        }

    fun loginButtonClick(view: View) {
        hideKeyboard(view)
        if (formComplete) {
            viewModelScope.launch {
                authenticationResult.value =
                        repository.makeLoginAttempt(username.value!!, password.value!!)
            }
        } else {
            authenticationResult.value = LoginRepository.AuthResult.LOGIN_INCOMPLETE
        }
    }

    fun addUserButtonClick(view: View) {
        hideKeyboard(view)

        val passwordsMatch = password.value.equals(confirmPassword.value)

        when {
            formComplete && passwordsMatch -> viewModelScope.launch {
                repository.addUser(username.value!!, password.value!!)
                // Once we've added the user, clear the form
                clearForm()
                authenticationResult.value = LoginRepository.AuthResult.USER_ADDED
            }
            formComplete && !passwordsMatch -> {
                authenticationResult.value = LoginRepository.AuthResult.PASSWORDS_DO_NOT_MATCH

            }
            !formComplete -> {
                authenticationResult.value = LoginRepository.AuthResult.LOGIN_INCOMPLETE
            }
        }
    }

    fun cancelButtonClick(view: View) {
        hideKeyboard(view)
        clearForm()
    }

    fun clearForm() {
        addUser.value = false
        username.value = ""
        password.value = ""
        confirmPassword.value = ""
        authenticationResult.value = LoginRepository.AuthResult.NO_LOGIN_ATTEMPT
    }

    private fun hideKeyboard(view: View) {
        // This should probably happen in the Activity, but I *just* can't be bothered anymore. It
        // does only get called on a button click, so the view reference should be transient
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}

class LoginViewModelFactory(private val repository: LoginRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}