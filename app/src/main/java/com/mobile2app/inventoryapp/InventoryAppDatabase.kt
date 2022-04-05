package com.mobile2app.inventoryapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

@Database(entities = [User::class, InventoryItem::class], version = 2, exportSchema = false)
abstract class InventoryAppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun inventoryDao(): InventoryDao

    companion object {
        @Volatile
        private var INSTANCE: InventoryAppDatabase? = null

        fun getInstance(context: Context): InventoryAppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    InventoryAppDatabase::class.java,
                    "inventory_app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}