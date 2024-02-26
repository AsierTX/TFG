package com.example.tfgtxurdinaga

import android.R.attr.button
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.MalformedURLException
import java.net.URISyntaxException
import java.net.URL
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
    private lateinit var eliminar: ImageButton

    private lateinit var switch: Switch

    private lateinit var btnlink: ImageButton
    private lateinit var btnemail: ImageButton
    private lateinit var btntelefono: ImageButton


    companion object{
        lateinit var database: appdatabase
            private set
    }

    @SuppressLint("MissingInflatedId", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editarnota)

        GlobalScope.launch(Dispatchers.IO) {

            database = Room.databaseBuilder(
                application,
                appdatabase::class.java,
                appdatabase.DATABASE_NAME
            )
                .allowMainThreadQueries()
                .build()
        }
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
        btnlink = findViewById(R.id.imageButton3)
        btnemail = findViewById(R.id.imageButton4)
        btntelefono = findViewById(R.id.imageButton5)
        eliminar = findViewById(R.id.imageButton6)

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
        GlobalScope.launch(Dispatchers.IO) {

            val intent = intent
            if (intent != null) {
                val tituloExtra = intent.getStringExtra("titulo")
                val descripcionExtra = intent.getStringExtra("descripcion")
                val horaExtra = intent.getStringExtra("hora")
                val fechaExtra = intent.getStringExtra("fecha")
                val linkExtra = intent.getStringExtra("link")
                val emailExtra = intent.getStringExtra("email")
                val telefonoExtra = intent.getStringExtra("telefono")
                var hechobool = tituloExtra?.let { database.dao.gethecho(it) }



                titulo.setText(tituloExtra)
                descripcion.setText(descripcionExtra)
                hora.setText(horaExtra)
                fecha.setText(fechaExtra)
                link.setText(linkExtra)
                email.setText(emailExtra)
                telefono.setText(telefonoExtra)
                if (hechobool != null) {
                    switch.isChecked = hechobool
                }
            }
        }
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

                if (!isValidDateFormat(nuevaFecha)) {
                    // Mostrar un mensaje de error y salir del método
                    Toast.makeText(this, "Formato de fecha no válido. Utiliza xx/xx/xxxx", Toast.LENGTH_SHORT).show()
                } else {
                    GlobalScope.launch(Dispatchers.IO) {


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

                        database.dao.updateNotaDetallada(
                            nuevoTitulo,
                            nuevaDescripcion,
                            nuevaHora,
                            nuevaFecha,
                            nuevoLink,
                            nuevoEmail,
                            nuevoTelefono,
                            hechoActualizado
                        )
                    }
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                }
        }

        eliminar.setOnClickListener {
            mostrarDialogOpciones()
        }
        btntelefono.setOnClickListener {
            val phoneNumber = telefono.text.toString()
            if (phoneNumber.isNotEmpty()) {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:$phoneNumber")
                startActivity(intent)
            }else{
                dialog()
            }
        }

        // Configura el OnClickListener para el ImageButton de correo electrónico
        btnemail.setOnClickListener {
            val emailAddress = email.text.toString()
            if (emailAddress.isNotEmpty()) {
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse("mailto:$emailAddress")
                startActivity(intent)
            }else{
                dialog()
            }
        }

        btnlink.setOnClickListener {
            val url = link.text.toString().trim()

            if (url.isNotEmpty()) {
                // Verificar si la URL tiene el prefijo "http://" o "https://"
                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    // Si no tiene, añadir "https://"
                    abrirEnlace("https://$url")
                } else {
                    abrirEnlace(url)
                }
            } else {
                // Manejar el caso en el que el campo esté vacío
                dialog()
            }
        }


    }

    private fun abrirEnlace(url: String) {
        // Verificar si la URL es válida
        if (isValidUrl(url)) {
            // Crear el intent para ver la URL
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

            // Verificar si hay al menos una aplicación que pueda manejar la intención
            if (intent.resolveActivity(packageManager) != null) {
                // Abrir el enlace en un navegador
                startActivity(intent)
            } else {
                // Manejar el caso en el que no hay aplicación para abrir la URL
                Toast.makeText(this, "No hay aplicación para abrir la URL", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Manejar el caso en el que la URL no sea válida
            Toast.makeText(this, "URL no válida", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isValidUrl(url: String): Boolean {
        return try {
            URL(url).toURI()
            true
        } catch (e: MalformedURLException) {
            false
        } catch (e: URISyntaxException) {
            false
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
    private fun isValidDateFormat(date: String): Boolean {
        val regex = Regex("""^\d{2}/\d{2}/\d{4}$""")
        return date.matches(regex)
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
    private fun dialog() {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("ERROR")
            .setMessage("Campo vacio, escribe algo.")
            .setPositiveButton("Vale") { dialog, which ->
            }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
    private fun mostrarDialogOpciones() {
        val opciones = arrayOf("Sí", "No", "Cancelar")

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Seguro que lo quieres borrar?")
            .setItems(opciones) { dialog, which ->
                when (which) {
                    0 -> {
                        database.dao.delete(titulo.text.toString())
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        dialog.dismiss()
                    }
                    1 -> {
                        dialog.dismiss()
                    }
                    2 -> {
                        dialog.dismiss()
                    }
                }
            }

        // Crea y muestra el diálogo
        val dialog = builder.create()
        dialog.show()
    }
}

