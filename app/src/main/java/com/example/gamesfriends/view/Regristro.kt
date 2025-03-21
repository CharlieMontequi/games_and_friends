package com.example.gamesfriends.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.gamesfriends.R

class Regristro  : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registro_nuevo_usuario)

        val b_registrarse = findViewById<Button>(R.id.b_registro_registro)
        b_registrarse.setOnClickListener {
            val intent = Intent(this, Cuerpo_app::class.java)
            startActivity(intent)
        }

    }
}