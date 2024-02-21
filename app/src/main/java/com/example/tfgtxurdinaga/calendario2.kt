package com.example.tfgtxurdinaga

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class calendario2 : AppCompatActivity() {
    private lateinit var entityDao: dao // Asegúrate de inicializar entityDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendario2)

        // Inicializar entityDao utilizando Room
        entityDao = Room.databaseBuilder(
            applicationContext,
            appdatabase::class.java,
            "appdatabase"
        ).build().dao

        GlobalScope.launch(Dispatchers.IO) {
            // Insertar nota 1
            entityDao.insert(
                entity(
                    titulo = "Nota 1",
                    descripcion = "Descripción de la Nota 1",
                    fecha = "2022-02-21", // Cambia la fecha según sea necesario
                    hora = "12:00 PM",
                    link = "https://ejemplo.com",
                    email = "correo@example.com",
                    telefono = "123456789",
                    hecho = false
                )
            )

            // Insertar nota 2
            entityDao.insert(
                entity(
                    titulo = "Nota 2",
                    descripcion = "Descripción de la Nota 2",
                    fecha = "2023-02-22", // Cambia la fecha según sea necesario
                    hora = "2:30 PM",
                    link = "https://ejemplo.com",
                    email = "correo@example.com",
                    telefono = "987654321",
                    hecho = true
                )
            )
        }

        // Lista de meses
        val listaDeMeses = arrayOf("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre")

        // Índice actual en la lista
        var indiceActual = 0

        // Inicializar el TextView con el mes inicial
        val textView: TextView = findViewById(R.id.textView2)
        textView.text = listaDeMeses[indiceActual]

        // Manejar clics en los ImageButtons para cambiar el mes
        val imageButtonAnterior: ImageButton = findViewById(R.id.imageButton)
        val imageButtonSiguiente: ImageButton = findViewById(R.id.imageButton2)

        imageButtonAnterior.setOnClickListener {
            // Cambiar al mes anterior
            if (indiceActual > 0) {
                indiceActual--
                textView.text = listaDeMeses[indiceActual]
            }
        }

        imageButtonSiguiente.setOnClickListener {
            // Cambiar al mes siguiente
            if (indiceActual < listaDeMeses.size - 1) {
                indiceActual++
                textView.text = listaDeMeses[indiceActual]
            }
        }

        // Inicializar el Spinner con los años en un hilo en segundo plano
        val spinner: Spinner = findViewById(R.id.spinner)
        GlobalScope.launch(Dispatchers.Main) {
            // Obtener los años únicos de la base de datos en un hilo en segundo plano
            val uniqueYears = withContext(Dispatchers.IO) {
                entityDao.getDistinctYears()
            }

            // Crear un adaptador para el Spinner
            val adapter = ArrayAdapter(this@calendario2, android.R.layout.simple_spinner_item, uniqueYears)

            // Especificar el diseño del dropdown
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            // Asignar el adaptador al Spinner
            spinner.adapter = adapter
        }
    }
}
