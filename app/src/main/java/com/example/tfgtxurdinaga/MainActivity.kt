package com.example.tfgtxurdinaga

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private lateinit var crearnota: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        crearnota=findViewById(R.id.imageButtonMas2)
        crearnota.setOnClickListener{
            val intent = Intent(this, addnota::class.java)
            startActivity(intent)
        }

        val intent = Intent(this, calendario2::class.java)
        startActivity(intent)

        finish()
    }
}