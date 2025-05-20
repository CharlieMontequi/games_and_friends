package com.example.gamesfriends.model.json

import android.content.Context
import android.database.DatabaseUtils
import com.example.gamesfriends.model.DataBaseHelper
import com.example.gamesfriends.model.datos.Juego
import com.example.gamesfriends.model.datos.MecanicaEnJuego
import org.json.JSONArray

class ImportarJson (private val context: Context, private val dbHelper: DataBaseHelper) {


    fun importarDesdeTexto(jsonTexto: String) {
        val jsonArray = JSONArray(jsonTexto)

        for (i in 0 until jsonArray.length()) {
            val juegoJson = jsonArray.getJSONObject(i)

            // construir card juego para meterlo
            val juego = Juego(
                nombreJuego = juegoJson.getString("nombreJuego"),
                duracionJuego = juegoJson.getInt("duracionJuego"),
                minimoJugadoresJuego = juegoJson.getInt("minimoJugadoresJuego"),
                maximoJugadoresJuego = juegoJson.getInt("maximoJugadoresJuego"),
                descipcionJuegp = juegoJson.getString("descipcionJuego")
            )

            val idJuegoInsertado = dbHelper.crearJuego(juego)

            val mecanicas = juegoJson.get("mencanicaenjuego")
            val idsMecanicas = when (mecanicas) {
                is JSONArray -> (0 until mecanicas.length()).map { mecanicas.getInt(it) }
                is Int -> listOf(mecanicas)
                else -> emptyList()
            }

            // tomar las mecacnas asociasdas a cada juego
            for (idMecanica in idsMecanicas) {
                val mecanicaEnJuego = MecanicaEnJuego(
                    id_mecanicaJuego = null,
                    fk_juego_mecanicaJuego = idJuegoInsertado,
                    fk_mecanica_mecanicaJuego = idMecanica
                )
                dbHelper.crearMecanciaEnJuego(mecanicaEnJuego)
            }
        }
    }
}