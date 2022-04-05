package com.mobile2app.inventoryapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "inventory")
data class InventoryItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val category: String?,
    val item: String,
    var quantity: Int,
    val description: String?
)