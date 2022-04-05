package com.mobile2app.inventoryapp

import kotlinx.coroutines.flow.flow
import kotlin.random.Random

class InventoryRepository(private val inventoryDao: InventoryDao) {

    val inventory = inventoryDao.getSortedInventory() // It really is this simple...

    suspend fun addItem(item: InventoryItem) {
        inventoryDao.addItem(item)
    }

    suspend fun updateItem(item: InventoryItem) {
        inventoryDao.updateItem(item)
    }

    suspend fun deleteItem(item: InventoryItem) {
        inventoryDao.deleteItem(item)
    }

}