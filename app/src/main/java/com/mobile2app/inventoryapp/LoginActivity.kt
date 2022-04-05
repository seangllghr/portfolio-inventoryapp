package com.mobile2app.inventoryapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.mobile2app.inventoryapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var mLoginBinding: ActivityLoginBinding
    private lateinit var mLoginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Since we've shifted to MVVM, we're using a different view/view model binding approach
        mLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        mLoginViewModel =
            LoginViewModelFactory((application as InventoryApplication).loginRepository)
                .create(LoginViewModel::class.java)
        mLoginBinding.loginViewModel = mLoginViewModel
        mLoginBinding.lifecycleOwner = this

        // Watch for changes in authentication state and respond accordingly
        mLoginViewModel.authenticationResult.observe(this, this::authenticationResultListener)

    }

    private fun authenticationResultListener(it: LoginRepository.AuthResult?) {
        // This `when` cascade arguably isn't ideal, but it works
        when (it!!) {
            LoginRepository.AuthResult.NO_LOGIN_ATTEMPT -> {
                // If the user hasn't made a login request yet, do nothing.
            }
            LoginRepository.AuthResult.LOGIN_INCOMPLETE -> {
                Snackbar.make(
                        mLoginBinding.root,
                        R.string.login_error_incomplete,
                        Snackbar.LENGTH_SHORT
                ).show()
            }
            LoginRepository.AuthResult.BAD_PASSWORD -> {
                Snackbar.make(
                        mLoginBinding.root,
                        R.string.login_error_failed,
                        Snackbar.LENGTH_SHORT
                ).show()
            }
            LoginRepository.AuthResult.NO_SUCH_USER -> {
                Snackbar.make(
                        mLoginBinding.root,
                        R.string.login_error_nouser,
                        Snackbar.LENGTH_LONG
                ).setAction(R.string.login_adduser) {
                    mLoginViewModel.addUser.value = true
                }.show()
            }
            LoginRepository.AuthResult.USER_ADDED -> {
                Snackbar.make(
                        mLoginBinding.root,
                        R.string.adduser_success,
                        Snackbar.LENGTH_SHORT
                ).show()
            }
            LoginRepository.AuthResult.PASSWORDS_DO_NOT_MATCH -> {
                Snackbar.make(
                        mLoginBinding.root,
                        R.string.adduser_failed_noMatch,
                        Snackbar.LENGTH_SHORT
                ).show()
            }
            LoginRepository.AuthResult.OK -> {
                mLoginViewModel.clearForm()
                val intent = Intent(this, OverviewActivity::class.java)
                startActivity(intent)
            }
        }
    }

}