package com.example.gamesfriends.view.detalle

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.gamesfriends.R
import com.example.gamesfriends.view.fragments.Fragment_detalle_juego
import com.example.gamesfriends.view.fragments.Fragment_historial_cuerpo

class Detalle_juego  : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detalle_juego)

        val fragmnetTitulo = Fragment_detalle_juego()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_detalle_juego, fragmnetTitulo)
            .commit()

    }
}