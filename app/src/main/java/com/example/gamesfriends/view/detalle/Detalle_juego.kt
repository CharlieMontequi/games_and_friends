package com.example.gamesfriends.view.detalle

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.gamesfriends.R
import com.example.gamesfriends.model.DataBaseHelper
import com.example.gamesfriends.view.fragments.Fragment_detalle_juego
import com.example.gamesfriends.view.fragments.Fragment_observaciones_personales_juego

class Detalle_juego  : AppCompatActivity() {

    private lateinit var dbHelper: DataBaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detalle_juego)

        val idJuego = intent.getIntExtra("ID_JUEGO",0)

        dbHelper = DataBaseHelper(this)

        val juego = dbHelper.detalleJuego(idJuego = idJuego)
        val txt_nombreJuegoDetalle=findViewById<TextView>(R.id.txt_nombre_juego_detalle_juego)
        val txt_NJugadores = findViewById<TextView>(R.id.txt_nJugadores_detalle_juego)
        val txt_duracion = findViewById<TextView>(R.id.txt_tiempoJuego_detalle_juego)
        if (juego != null) {
            txt_nombreJuegoDetalle.text = juego.nombreJuego
            txt_NJugadores.text = juego.minimoJugadoresJuego.toString() +" - " +  juego.maximoJugadoresJuego.toString()+" jugadores"
            txt_duracion.text = juego.duracionJuego.toString() + "min"

            val bundleIJuego = Bundle()
            bundleIJuego.putInt("ID_JUEGO_DETALLE", juego.idJuego!!)

            val fragmentCuerpoJuegoDetalle = Fragment_detalle_juego()
            fragmentCuerpoJuegoDetalle.arguments= bundleIJuego
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_detalle_juego, fragmentCuerpoJuegoDetalle)
                .commit()


            val fragmentObservacionesPersonales = Fragment_observaciones_personales_juego()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_observaciones_deatlleJuego, fragmentObservacionesPersonales)
                .commit()
        }

    }
}