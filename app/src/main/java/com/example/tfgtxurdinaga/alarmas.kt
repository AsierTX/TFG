package com.example.tfgtxurdinaga

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Toast
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Calendar

class alarmas : AppCompatActivity() {

    private lateinit var hora: EditText
    private lateinit var fecha: EditText
    private lateinit var crear: ImageButton
    private val diasSeleccionados: MutableSet<Int> = mutableSetOf()
    private lateinit var listview: ListView





    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarmas)

        fecha = findViewById(R.id.fecha3)
        hora = findViewById(R.id.fecha4)
        crear = findViewById(R.id.crearbtn)


        crear.setOnClickListener {
            if (fecha.text.toString().isNotEmpty() && hora.text.toString().isNotEmpty()) {
                val fechaIngresada = fecha.text.toString()
                val horaIngresada = hora.text.toString()

                    val horasYMinutos = obtenerHorasYMinutos(horaIngresada)

                    val horas = horasYMinutos?.first
                    val minutos = horasYMinutos?.second

                    val calendario = Calendar.getInstance()
                    calendario.set(Calendar.YEAR, calendario.get(Calendar.YEAR))
                    calendario.set(Calendar.MONTH, calendario.get(Calendar.MONTH))
                    calendario.set(Calendar.DAY_OF_MONTH, calendario.get(Calendar.DAY_OF_MONTH))
                    calendario.set(Calendar.HOUR_OF_DAY, horas!!)
                    calendario.set(Calendar.MINUTE, minutos!!)

                    val intent = Intent(AlarmClock.ACTION_SET_ALARM)
                    intent.putExtra(AlarmClock.EXTRA_HOUR, horas)
                    intent.putExtra(AlarmClock.EXTRA_MINUTES, minutos)
                    intent.putExtra(AlarmClock.EXTRA_SKIP_UI, true)
                if (diasSeleccionados.isNotEmpty()) {
                    intent.putExtra(AlarmClock.EXTRA_DAYS, diasSeleccionados.toIntArray())
                }


                    if (intent.resolveActivity(packageManager) != null) {
                        startActivity(intent)
                        hora.text.clear()
                        fecha.text.clear()
                    } else {
                        dialogalarmas()
                        hora.text.clear()
                        fecha.text.clear()
                    }

            } else {
                dialog()
            }
            recreate()

        }

        fecha.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                mostrarDialogoSeleccionDias()
            }
        }

        hora.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                mostrarTimePickerDialog()
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
                // Actualizar la fecha del calendario con la fecha seleccionada
                calendario.set(Calendar.YEAR, year)
                calendario.set(Calendar.MONTH, month)
                calendario.set(Calendar.DAY_OF_MONTH, dayOfMonth)

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
    private fun mostrarDialogoSeleccionDias() {
        val dias = arrayOf("Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado")

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Selecciona los días de la semana")

        val isCheckedArray = BooleanArray(dias.size) { diasSeleccionados.contains(it + 1) }

        builder.setMultiChoiceItems(dias, isCheckedArray) { _, which, isChecked ->
            if (isChecked) {
                diasSeleccionados.add(which + 1)
            } else {
                diasSeleccionados.remove(which + 1)
            }
        }

        builder.setPositiveButton("Aceptar") { dialog, _ ->

            if (diasSeleccionados.isNotEmpty()) {
                val diasSeleccionadosStr = diasSeleccionados.joinToString(", ") {
                    dias[it - 1]
                }
                // Escribe los días seleccionados en el EditText
                fecha.setText(diasSeleccionadosStr)
            }

            dialog.dismiss()
        }

        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }

        builder.show()
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
    private fun dialogformato() {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("ERROR")
            .setMessage("Formato incorrecto. Formato requerido: dd/mm/aaaa y 00:00")
            .setPositiveButton("Vale") { dialog, which ->
            }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
    private fun dialogalarmas() {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("ERROR")
            .setMessage("Aplicacion de alarmas no disponible.")
            .setPositiveButton("Vale") { dialog, which ->
            }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun obtenerHorasYMinutos(hora: String): Pair<Int, Int>? {
        val partes = hora.split(":")

        if (partes.size == 2) {
            try {
                val horas = partes[0].toInt()
                val minutos = partes[1].toInt()

                if (horas in 0..23 && minutos in 0..59) {
                    return Pair(horas, minutos)
                }
            } catch (e: NumberFormatException) {
                // Manejar el caso en el que no se pueden convertir a números
            }
        }

        return null
    }
}