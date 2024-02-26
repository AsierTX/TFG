package com.example.tfgtxurdinaga

import android.content.res.Configuration
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class ajustes : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ajustes)

        // Asociar clics de botón con el cambio de idioma
        val botonEuskera = findViewById<ImageButton>(R.id.imageButton7)
        val botonEspanol = findViewById<ImageButton>(R.id.imageButton8)
        val botonIngles = findViewById<ImageButton>(R.id.imageButton9)

        botonEuskera.setOnClickListener {
            cambiarIdioma("eu")
        }

        botonEspanol.setOnClickListener {
            cambiarIdioma("es")
        }

        botonIngles.setOnClickListener {
            cambiarIdioma("ing")
        }
    }

    private fun cambiarIdioma(idioma: String) {
        val configuracion = Configuration(resources.configuration)
        configuracion.setLocale(Locale(idioma))
        resources.updateConfiguration(configuracion, resources.displayMetrics)

        // Recargar la actividad actual para aplicar el cambio de idioma
        recreate()
    }
}
