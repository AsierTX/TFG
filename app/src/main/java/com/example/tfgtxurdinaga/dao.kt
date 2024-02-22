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

    @Query("SELECT DISTINCT SUBSTR(fecha, 1, 4) AS year FROM entity ORDER BY year DESC")
    fun getDistinctYears(): List<String>

    @Query("SELECT * FROM entity WHERE strftime('%Y', fecha) = :year AND strftime('%m', fecha) = :month")
    fun getNotesByYearAndMonth(year: Int, month: Int): List<entity>
}