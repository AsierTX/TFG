package com.example.tfgtxurdinaga

import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class calendario2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendario2)

        // Obtén la referencia al LinearLayout dentro del ScrollView
        val linearLayout = findViewById<LinearLayout>(R.id.linearLayoutEventos)

        // Simulación de eventos (puedes reemplazarlo con tu lógica)
        val eventos = obtenerEventosDelMesActual()

        // Crea y establece el adaptador
        val eventosAdapter = EventosAdapter(this, eventos)

        // Agrega tarjetas al LinearLayout
        for (i in 0 until eventosAdapter.count) {
            val tarjeta = eventosAdapter.getView(i, null, linearLayout)
            linearLayout.addView(tarjeta)
        }

        // Resto de tu lógica...
    }

    private fun obtenerEventosDelMesActual(): List<Evento> {
        // Implementa la lógica para obtener eventos del mes actual
        // ...

        // Ejemplo de eventos simulados
        return listOf(
            Evento("Reunión importante", "Planificación para el próximo proyecto", "2024-02-20"),
            Evento("Entrenamiento", "Ir al gimnasio", "2024-02-25"),
            Evento("Cita médica", "Consulta con el doctor", "2024-02-28")
        )
    }
}
