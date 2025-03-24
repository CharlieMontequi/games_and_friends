package com.example.gamesfriends.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.gamesfriends.R
import com.example.gamesfriends.view.detalle.Detalle_convocar

class Cuerpo_app  : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cuerpo_app)

        val b_misJuegos = findViewById<Button>(R.id.b_misJuegos_cuerpo)
        val b_amigos = findViewById<Button>(R.id.b_amistades_cuerpo)
        val b_historial = findViewById<Button>(R.id.b_historial_cuerpo)
        val b_convocar = findViewById<Button>(R.id.b_convocar_cuerpo)

        b_convocar.setOnClickListener {
            val intent = Intent(this, Detalle_convocar::class.java)
            startActivity(intent)
        }
        b_amigos.setOnClickListener {
            val intent = Intent(this, Amistades::class.java)
            startActivity(intent)
        }
        b_historial.setOnClickListener {
            val intent = Intent(this, Historial::class.java)
            startActivity(intent)
        }
        b_misJuegos.setOnClickListener {
            val intent = Intent(this, Mis_juegos::class.java)
            startActivity(intent)
        }
    }
}