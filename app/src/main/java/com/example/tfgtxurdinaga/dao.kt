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

    @Query("SELECT * FROM entity")
    fun getAllDatos(): List<entity>

    @Query("SELECT * FROM entity WHERE titulo = :titulo")
    fun getNotaPorTitulo(titulo: String): entity?

    //@Query("SELECT * FROM entity WHERE strftime('%Y', fecha) = :year AND strftime('%m', fecha) = :month")
    //fun getNotesByYearAndMonth(year: Int, month: Int): List<entity>
    @Query("SELECT * FROM entity WHERE substr(fecha, 4, 2) = :month AND substr(fecha, 7, 4) = :year")
    fun getNotesByYearAndMonth(year: String, month: String): List<entity>


    @Query("SELECT DISTINCT SUBSTR(fecha, -4) AS año FROM entity WHERE fecha IS NOT NULL AND fecha != ''")
    fun getYears(): List<Int>




    @Query("UPDATE entity SET descripcion = :nuevaDescripcion, hora = :nuevaHora, fecha = :nuevaFecha, link = :nuevoLink, email = :nuevoEmail, telefono = :nuevoTelefono, hecho = :nuevoHecho WHERE titulo = :titulo")
    fun updateNotaDetallada(
        titulo: String,
        nuevaDescripcion: String,
        nuevaHora: String,
        nuevaFecha: String,
        nuevoLink: String,
        nuevoEmail: String,
        nuevoTelefono: String,
        nuevoHecho: Boolean
    )
}