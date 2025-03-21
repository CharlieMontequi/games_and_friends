package com.example.gamesfriends.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.gamesfriends.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val b_entrar = findViewById<Button>(R.id.b_entrar_inicioSesion)

        b_entrar.setOnClickListener {
            val intent = Intent(this, Cuerpo_app::class.java)
            startActivity(intent)

        }
        val txt_registro = findViewById<TextView>(R.id.txt_registrarse_inicioSesion)

        txt_registro.setOnClickListener {
            val intent = Intent(this, Regristro::class.java)
            startActivity(intent)
        }

    }
}