package com.example.tfgtxurdinaga

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "entity")
data class entity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0,
    @ColumnInfo(name = "titulo")
    var titulo: String,
    @ColumnInfo(name = "descripcion")
    var descripcion: String,
    @ColumnInfo(name = "fecha")
    var fecha: String,
    @ColumnInfo(name = "hora")
    var hora: String,
    @ColumnInfo(name = "link")
    var link: String,
    @ColumnInfo(name = "email")
    var email: String,
    @ColumnInfo(name = "telefono")
    var telefono: String,
    @ColumnInfo(name = "hecho")
    var hecho: Boolean
)