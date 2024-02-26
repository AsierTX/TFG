package com.example.tfgtxurdinaga

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Switch
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Calendar

class addnota : AppCompatActivity() {

    private lateinit var titulo: EditText
    private lateinit var descripcion: EditText
    private lateinit var hora: EditText
    private lateinit var fecha: EditText
    private lateinit var link: EditText
    private lateinit var email: EditText
    private lateinit var telefono: EditText
    private lateinit var cerrarnota: ImageButton
    private lateinit var crearnota: ImageButton

    companion object {
        lateinit var database: appdatabase
            private set
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addnota)

        titulo = findViewById(R.id.titulo)
        descripcion = findViewById(R.id.descripcion)
        link = findViewById(R.id.link)
        email = findViewById(R.id.email)
        telefono = findViewById(R.id.telefono)
        hora = findViewById(R.id.horas)
        fecha = findViewById(R.id.fecha)
        cerrarnota = findViewById(R.id.imageButtonX2)
        crearnota = findViewById(R.id.imageButtoCheckn2)

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

        cerrarnota.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        crearnota.setOnClickListener {
            val titulostring = titulo.text.toString()
            val descripcionstring = descripcion.text.toString()
            val fechastring = fecha.text.toString()
            val horastring = hora.text.toString()
            val linkstring = link.text.toString()
            val emailstring = email.text.toString()
            val telefonostring = telefono.text.toString()


            if (titulostring.isNotEmpty()) {
                if (fechastring!=""){
                if (!isValidDateFormat(fechastring)) {
                    dialogerror()
                } else {
                    // Comprobar si el título ya existe
                    GlobalScope.launch(Dispatchers.IO) {

                        database = Room.databaseBuilder(
                            application,
                            appdatabase::class.java,
                            appdatabase.DATABASE_NAME
                        )
                            .allowMainThreadQueries()
                            .build()

                        val notaExistente = database.dao.getNotaPorTitulo(titulostring)

                        if (notaExistente == null) {
                            // El título no existe, puedes insertar la nota
                            val notanueva = entity(
                                id = 0,
                                titulo = titulostring,
                                descripcion = descripcionstring,
                                fecha = fechastring,
                                hora = horastring,
                                link = linkstring,
                                email = emailstring,
                                telefono = telefonostring,
                                hecho = true
                            )

                            database.dao.insert(notanueva)

                            titulo.text.clear()
                            descripcion.text.clear()
                            fecha.text.clear()
                            hora.text.clear()
                            link.text.clear()
                            email.text.clear()
                            telefono.text.clear()

                            val intent = Intent(this@addnota, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            // El título ya existe, mostrar diálogo de error
                            runOnUiThread {
                                dialog2()
                            }
                        }
                    }
                }}else{
                    GlobalScope.launch(Dispatchers.IO) {

                        database = Room.databaseBuilder(
                            application,
                            appdatabase::class.java,
                            appdatabase.DATABASE_NAME
                        )
                            .allowMainThreadQueries()
                            .build()

                        val notaExistente = database.dao.getNotaPorTitulo(titulostring)

                        if (notaExistente == null) {
                            // El título no existe, puedes insertar la nota
                            val notanueva = entity(
                                id = 0,
                                titulo = titulostring,
                                descripcion = descripcionstring,
                                fecha = fechastring,
                                hora = horastring,
                                link = linkstring,
                                email = emailstring,
                                telefono = telefonostring,
                                hecho = true
                            )

                            database.dao.insert(notanueva)

                            titulo.text.clear()
                            descripcion.text.clear()
                            fecha.text.clear()
                            hora.text.clear()
                            link.text.clear()
                            email.text.clear()
                            telefono.text.clear()

                            val intent = Intent(this@addnota, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            // El título ya existe, mostrar diálogo de error
                            runOnUiThread {
                                dialog2()
                            }
                        }
                    }
                }
            } else {
                dialog()
            }
        }
    }

    private fun dialog() {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("ERROR")
            .setMessage("Es imprescindible escribir el titulo!")
            .setPositiveButton("Vale") { dialog, which ->
            }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
    private fun isValidDateFormat(date: String): Boolean {
        val regex = Regex("""^\d{2}/\d{2}/\d{4}$""")
        return date.matches(regex)
    }

    private fun dialog2() {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("ERROR")
            .setMessage("Ese titulo ya existe! Prueba con otro.")
            .setPositiveButton("Vale") { dialog, which ->
            }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun mostrarCalendario() {
        val calendario = Calendar.getInstance()
        val año = calendario.get(Calendar.YEAR)
        val mes = calendario.get(Calendar.MONTH)
        val día = calendario.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                // Formatear el día y el mes seleccionados con dos dígitos
                val dayOfMonthFormatted = String.format("%02d", dayOfMonth)
                val monthFormatted = String.format("%02d", month + 1)

                // Construir la fecha con el formato deseado
                val fechaSeleccionada = "$dayOfMonthFormatted/$monthFormatted/$year"
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
                val horaFormateada =
                    String.format("%02d:%02d", horaSeleccionada, minutoSeleccionado)
                // Establecer el texto en el EditText
                hora.setText(horaFormateada)
            },
            horaActual,
            minutoActual,
            true
        )

        timePickerDialog.show()
    }
    private fun dialogerror() {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("ERROR")
            .setMessage("Formato de fecha no válido. Utiliza xx/xx/xxxx")
            .setPositiveButton("Vale") { dialog, which ->
            }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}