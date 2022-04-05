package com.mobile2app.inventoryapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val username: String,
    @ColumnInfo(name = "password_hash") val passwordHash: String,
    val salt: String
    )
