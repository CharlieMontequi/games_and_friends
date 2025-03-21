package com.example.gamesfriends.view

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.gamesfriends.R

class Cuerpo_app  : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cuerpo_app)

        val b_misJuegos = findViewById<Button>(R.id.b_misJuegos_cuerpo)
        val b_amigos = findViewById<Button>(R.id.b_amistades_cuerpo)
        val b_historial = findViewById<Button>(R.id.b_historial_cuerpo)
        val b_convocar = findViewById<Button>(R.id.b_convocar_cuerpo)

    }
}