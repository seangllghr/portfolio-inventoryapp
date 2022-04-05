package com.mobile2app.inventoryapp

import androidx.lifecycle.*

class InventoryViewModel(private val repository: InventoryRepository) : ViewModel() {

    val inventory = repository.inventory.asLiveData()

}

class InventoryViewModelFactory(private val repository: InventoryRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InventoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InventoryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}