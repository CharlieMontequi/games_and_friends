package com.example.gamesfriends.model.json

import android.content.Context
import android.os.Environment
import com.example.gamesfriends.model.datos.Juego
import com.example.gamesfriends.model.datos.MecanicaEnJuego
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream

class ExportarJson(private val context: Context) {

    fun exportarJuegosConMecanicas(juegos: List<Juego>, mecanicasEnJuego: List<MecanicaEnJuego>, usuarioHace: String ) {
        val jsonArray = JSONArray()

        val archivoNombre = usuarioHace + "juegos_exportados.json"
        for (juego in juegos) {
            val juegoJson = JSONObject()
            juegoJson.put("nombreJuego", juego.nombreJuego)
            juegoJson.put("duracionJuego", juego.duracionJuego)
            juegoJson.put("minimoJugadoresJuego", juego.minimoJugadoresJuego)
            juegoJson.put("maximoJugadoresJuego", juego.maximoJugadoresJuego)
            juegoJson.put("descipcionJuego", juego.descipcionJuegp)

            // cpger el id mecánicas relacionadas con el juego
            val mecanicasIds = mecanicasEnJuego
                .filter { it.fk_juego_mecanicaJuego == juego.idJuego }
                .map { it.fk_mecanica_mecanicaJuego }

            // si las mecancias son varias estan en un array
            juegoJson.put("mencanicaenjuego", if (mecanicasIds.size == 1) mecanicasIds.first() else JSONArray(mecanicasIds))

            jsonArray.put(juegoJson)
        }

        //guardarlo en descargas de archoivos del movil
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val archivo = File(downloadsDir, archivoNombre)
        FileOutputStream(archivo).use {
            it.write(jsonArray.toString(4).toByteArray())
        }
    }
}