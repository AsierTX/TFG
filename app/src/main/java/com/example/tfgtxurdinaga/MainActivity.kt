package com.example.tfgtxurdinaga

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var crearnota: ImageButton
    private val lista: ArrayList<entity> = ArrayList()
    private lateinit var adapter: ArrayAdapter<entity>
    private lateinit var listView: ListView
    companion object{
        lateinit var database: appdatabase
            private set
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.listview)
        crearnota=findViewById(R.id.imageButtonMas2)


        GlobalScope.launch(Dispatchers.IO) {
            database = Room.databaseBuilder(
                application,
                appdatabase::class.java,
                appdatabase.DATABASE_NAME
            )
                .allowMainThreadQueries()
                .build()

                lista.addAll(database.dao.getAllDatos())
                val Adapter = adapter(this@MainActivity, R.layout.nota_layout, lista)
                listView.adapter = Adapter
        }

        listView.setOnItemClickListener { parent, view, position, id ->

            val itemSeleccionado: entity = lista[position]

            val intent = Intent(this@MainActivity, editarnota::class.java)

            intent.putExtra("titulo", itemSeleccionado.titulo)
            intent.putExtra("descripcion", itemSeleccionado.descripcion)
            intent.putExtra("hora", itemSeleccionado.hora)
            intent.putExtra("fecha", itemSeleccionado.fecha)
            intent.putExtra("link", itemSeleccionado.link)
            intent.putExtra("email", itemSeleccionado.email)
            intent.putExtra("telefono", itemSeleccionado.telefono)
            intent.putExtra("hecho", itemSeleccionado.hecho)

            startActivity(intent)
        }



        crearnota.setOnClickListener{
            val intent = Intent(this, addnota::class.java)
            startActivity(intent)
        }


/*        val intent = Intent(this, Ejemplos::class.java)
        startActivity(intent)

        finish()
    }
}