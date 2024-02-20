package com.example.tfgtxurdinaga

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [entity::class], version = 1, exportSchema = false)
abstract class appdatabase : RoomDatabase() {
    abstract val dao: dao
    companion object {
        const val DATABASE_NAME = "DATABASE_NAME"
    }
}


