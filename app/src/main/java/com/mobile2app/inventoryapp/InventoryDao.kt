package com.mobile2app.inventoryapp

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface InventoryDao {

    @Insert
    suspend fun addItem(item: InventoryItem)

    @Query("SELECT * FROM inventory ORDER BY category ASC, item ASC")
    fun getSortedInventory(): Flow<List<InventoryItem>>

    @Update
    suspend fun updateItem(items: InventoryItem)

    @Delete
    suspend fun deleteItem(items: InventoryItem)

}