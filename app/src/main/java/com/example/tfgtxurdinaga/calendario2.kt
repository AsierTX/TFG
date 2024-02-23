package com.example.tfgtxurdinaga

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class calendario2 : AppCompatActivity() {
    private lateinit var entityDao: dao
    private lateinit var listView: ListView
    private lateinit var spinner: Spinner
    private var indiceActual = 0

    companion object {
        lateinit var database: appdatabase
            private set
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendario2)


        // Inicializar la base de datos en un hilo de fondo
        GlobalScope.launch(Dispatchers.IO) {
            database = Room.databaseBuilder(
                application,
                appdatabase::class.java,
                appdatabase.DATABASE_NAME
            ).build()

            listView = findViewById(R.id.listview)

            // Obtener los años desde la base de datos
            val years = database.dao.getYears()

            // Crear un adaptador para el Spinner con los años obtenidos
            val adapter =
                ArrayAdapter(this@calendario2, android.R.layout.simple_spinner_item, years)

            // Especificar el diseño del dropdown
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            // Inicializar el Spinner
            spinner = findViewById(R.id.spinner)
            spinner.adapter = adapter

            updateNotesList()
        }
        // Lista de meses
        val listaDeMeses = arrayOf(
            "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto",
            "Septiembre", "Octubre", "Noviembre", "Diciembre"
        )



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
                updateNotesList()            }
        }

        imageButtonSiguiente.setOnClickListener {
            // Cambiar al mes siguiente
            if (indiceActual < listaDeMeses.size - 1) {
                indiceActual++
                textView.text = listaDeMeses[indiceActual]
                updateNotesList()
            }
        }

        // Aquí puedes agregar la inicialización del ListView si es necesario
    }
    private fun updateNotesList() {
        // Obtener el año seleccionado desde el Spinner
        val selectedYear = spinner.selectedItem as Int
        val selectedYearstring = selectedYear.toString()
        // Obtener el mes mostrado en el TextView
        val selectedMonth = (indiceActual + 1) as Int
        val selectedMonthstring = selectedMonth.toString()

        GlobalScope.launch(Dispatchers.IO) {
            val filteredNotes = database.dao.getNotesByYearAndMonth(selectedYearstring, selectedMonthstring)

            // Mostrar las notas en el ListView
            withContext(Dispatchers.Main) {

                val adapter = adapter(this@calendario2, R.layout.nota_layout, filteredNotes)
                listView.adapter = adapter
            }
        }
    }
}
