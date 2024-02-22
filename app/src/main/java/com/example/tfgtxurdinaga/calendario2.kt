package com.example.tfgtxurdinaga

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class calendario2 : AppCompatActivity() {
    private lateinit var entityDao: dao // Asegúrate de inicializar entityDao
    private lateinit var listView: ListView
    private lateinit var spinner: Spinner

    // Año y mes seleccionados
    private var selectedYear: Int = 0
    private var selectedMonth: Int = 0

    @SuppressLint("MissingInflatedId")
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
                    fecha = "2022-10-21", // Cambia la fecha según sea necesario
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
        val listaDeMeses = arrayOf(
            "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto",
            "Septiembre", "Octubre", "Noviembre", "Diciembre"
        )

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
                updateNotesList()
            }
        }

        imageButtonSiguiente.setOnClickListener {
            // Cambiar al mes siguiente
            if (indiceActual < listaDeMeses.size - 1) {
                indiceActual++
                textView.text = listaDeMeses[indiceActual]
                updateNotesList()
            }
        }

        // Inicializar el Spinner con los años en un hilo en segundo plano
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
            spinner = findViewById(R.id.spinner)
            spinner.adapter = adapter

            // Manejar la selección de año en el Spinner
            spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View?, position: Int, id: Long) {
                    selectedYear = uniqueYears[position].toInt()
                    updateNotesList()
                }

                override fun onNothingSelected(parentView: AdapterView<*>) {
                    // No hacer nada si no se selecciona nada
                }
            })
        }

        // Inicializar el ListView
        listView = findViewById(R.id.listview)
    }

    private fun updateNotesList() {
        // Obtener el mes seleccionado actualmente
        val selectedMonthIndex = spinner.selectedItemPosition
        selectedMonth = selectedMonthIndex + 1 // Los índices de los meses suelen ser 0-based

        // Cargar las notas correspondientes al año y mes seleccionados desde la base de datos
        GlobalScope.launch(Dispatchers.IO) {
            val filteredNotes = entityDao.getNotesByYearAndMonth(selectedYear, selectedMonth)

            // Mostrar las notas en el ListView
            runOnUiThread {
                val adapter = ArrayAdapter(this@calendario2, android.R.layout.simple_list_item_1, filteredNotes)
                listView.adapter = adapter
            }
        }
    }
}
