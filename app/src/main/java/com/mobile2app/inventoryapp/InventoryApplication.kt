package com.mobile2app.inventoryapp

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class InventoryApplication : Application() {
    private val database by lazy { InventoryAppDatabase.getInstance(this) }
    val applicationScope by lazy { CoroutineScope(Dispatchers.IO) }
    val loginRepository by lazy { LoginRepository(database.userDao()) }
    val inventoryRepository by lazy { InventoryRepository(database.inventoryDao()) }
}