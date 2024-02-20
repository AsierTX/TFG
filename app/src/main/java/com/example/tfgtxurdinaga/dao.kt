package com.example.tfgtxurdinaga

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface dao {
    @Insert
    fun insert(entity: entity)
}