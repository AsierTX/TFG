package com.example.tfgtxurdinaga

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Switch
import android.widget.TextView
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Calendar

class editarnota : AppCompatActivity() {

    private lateinit var titulo: TextView
    private lateinit var descripcion: EditText
    private lateinit var hora: EditText
    private lateinit var fecha: EditText
    private lateinit var link: EditText
    private lateinit var email: EditText
    private lateinit var telefono: EditText
    private lateinit var cerrarnota: ImageButton
    private lateinit var editar: ImageButton
    private lateinit var switch: Switch

    companion object{
        lateinit var database: appdatabase
            private set
    }

    @SuppressLint("MissingInflatedId", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editarnota)

        titulo = findViewById(R.id.titulo2)
        descripcion = findViewById(R.id.descripcion2)
        link = findViewById(R.id.link2)
        email = findViewById(R.id.email2)
        telefono = findViewById(R.id.telefono2)
        hora = findViewById(R.id.horas2)
        fecha = findViewById(R.id.fecha2)
        cerrarnota = findViewById(R.id.imageButtonX3)
        editar = findViewById(R.id.imageButtoCheckn3)
        switch = findViewById(R.id.switch2)

        fecha.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                mostrarCalendario()
            }
        }

        hora.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                mostrarTimePickerDialog()
            }
        }

        val intent = intent
        if (intent != null) {
            val tituloExtra = intent.getStringExtra("titulo")
            val descripcionExtra = intent.getStringExtra("descripcion")
            val horaExtra = intent.getStringExtra("hora")
            val fechaExtra = intent.getStringExtra("fecha")
            val linkExtra = intent.getStringExtra("link")
            val emailExtra = intent.getStringExtra("email")
            val telefonoExtra = intent.getStringExtra("telefono")
            val hechoExtra = intent.getStringExtra("hecho").toBoolean()

            titulo.setText(tituloExtra)
            descripcion.setText(descripcionExtra)
            hora.setText(horaExtra)
            fecha.setText(fechaExtra)
            link.setText(linkExtra)
            email.setText(emailExtra)
            telefono.setText(telefonoExtra)
            val switchValue = hechoExtra == true
            switch.isChecked = switchValue

            cerrarnota.setOnClickListener {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }

            editar.setOnClickListener {
                val nuevoTitulo = titulo.text.toString()
                val nuevaDescripcion = descripcion.text.toString()
                val nuevaHora = hora.text.toString()
                val nuevaFecha = fecha.text.toString()
                val nuevoLink = link.text.toString()
                val nuevoEmail = email.text.toString()
                val nuevoTelefono = telefono.text.toString()
                val switchValue = switch.isChecked
                val hechoActualizado: Boolean = if (switchValue) true else false

                val notaActualizada = entity(
                    titulo = nuevoTitulo,
                    descripcion = nuevaDescripcion,
                    hora = nuevaHora,
                    fecha = nuevaFecha,
                    link = nuevoLink,
                    email = nuevoEmail,
                    telefono = nuevoTelefono,
                    hecho = hechoActualizado
                )

                // Llama al método de actualización en tu DAO
                GlobalScope.launch(Dispatchers.IO) {

                    database = Room.databaseBuilder(
                        application,
                        appdatabase::class.java,
                        appdatabase.DATABASE_NAME
                    )
                        .allowMainThreadQueries()
                        .build()
                        database.dao.updateNotaDetallada(
                            nuevoTitulo,
                            nuevaDescripcion,
                            nuevaHora,
                            nuevaFecha,
                            nuevoLink,
                            nuevoEmail,
                            nuevoTelefono,
                            hechoActualizado)

                    }
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                }
            }
            }
    private fun mostrarCalendario() {
        val calendario = Calendar.getInstance()
        val año = calendario.get(Calendar.YEAR)
        val mes = calendario.get(Calendar.MONTH)
        val día = calendario.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                // Manejar la fecha seleccionada
                val fechaSeleccionada = "$dayOfMonth/${month + 1}/$year"
                fecha.setText(fechaSeleccionada)
            },
            año,
            mes,
            día
        )

        datePickerDialog.show()
    }
    private fun mostrarTimePickerDialog() {
        val calendario = Calendar.getInstance()
        val horaActual = calendario.get(Calendar.HOUR_OF_DAY)
        val minutoActual = calendario.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            { _, horaSeleccionada, minutoSeleccionado ->
                // Formatear la hora seleccionada a "HH:mm"
                val horaFormateada = String.format("%02d:%02d", horaSeleccionada, minutoSeleccionado)
                // Establecer el texto en el EditText
                hora.setText(horaFormateada)
            },
            horaActual,
            minutoActual,
            true
        )

        timePickerDialog.show()
        }
}

