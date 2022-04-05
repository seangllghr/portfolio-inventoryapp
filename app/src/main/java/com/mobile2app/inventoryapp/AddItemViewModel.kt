package com.mobile2app.inventoryapp

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlin.IllegalArgumentException

class AddItemViewModel(private val repository: InventoryRepository) : ViewModel() {

    enum class ItemAddResult{OK, FAILED, CANCELLED, NOT_ATTEMPTED}
    val newItemName = MutableLiveData("")
    val newItemCategory = MutableLiveData("")
    val newItemDescription = MutableLiveData("")
    val newItemCount = MutableLiveData("0")
    val newItemCreated = MutableLiveData(ItemAddResult.NOT_ATTEMPTED)

    private fun addItem(item: InventoryItem) = viewModelScope.launch {
        repository.addItem(item)
        newItemCreated.value = ItemAddResult.OK
    }

    fun onAddItemClick(view: View) {
        if (newItemName.value.isNullOrBlank() || newItemCount.value.isNullOrBlank()) {
            newItemCreated.value = ItemAddResult.FAILED
            return
        }
        val newItem = InventoryItem(
            category = newItemCategory.value,
            item = newItemName.value!!,
            quantity = newItemCount.value!!.toInt(),
            description = newItemDescription.value
        )
        addItem(newItem)
    }

    fun onCancelButtonClick(view: View) {
        newItemCreated.value = ItemAddResult.CANCELLED
    }

}

class AddItemViewModelFactory(private val repository: InventoryRepository)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddItemViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddItemViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}