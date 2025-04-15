package com.example.gamesfriends.view.detalle

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.gamesfriends.R
import com.example.gamesfriends.view.fragments.Fragment_historial_cuerpo

class Detalle_historial  : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detalle_historial)

        val idHistorial = intent.getIntExtra("ID_HISTORIAL",0)

        val bundleIJuego = Bundle()
        bundleIJuego.putInt("ID_HISTORIAL_DETALLE", idHistorial!!)

        val fragmnetDetalleHistorial = Fragment_historial_cuerpo()
        fragmnetDetalleHistorial.arguments=bundleIJuego
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_detalle_historial_cuerpo, fragmnetDetalleHistorial)
            .commit()

    }
}