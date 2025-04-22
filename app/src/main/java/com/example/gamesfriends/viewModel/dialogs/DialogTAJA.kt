package com.example.gamesfriends.viewModel.dialogs

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.gamesfriends.R

class DialogTAJA(val context: Context, private val onAceptar: (jugadoresMedia: Int) -> Unit) {

    fun mostrar() {
        val dialog = Dialog(context)

        val vista = LayoutInflater.from(context).inflate(R.layout.vetana_calcular_taja, null)

        dialog.setContentView(vista)
        dialog.window?.setBackgroundDrawableResource(R.drawable.fondo_con_borde_verde)
        val inputMediaJugadores = vista.findViewById<EditText>(R.id.etxt_media_jugadores_TAJA)
        val bAceptar = vista.findViewById<Button>(R.id.b_calcaularEmergenteTAJA)
        val bCancelar = vista.findViewById<Button>(R.id.b_cancelar_taja)

        Log.d("DialogTAJA", "Botón encontrado: ${bAceptar != null}")

        bAceptar.setOnClickListener {
            if (inputMediaJugadores != null) {
                val mediaJugadores = inputMediaJugadores.text.toString().toInt()
                onAceptar(mediaJugadores)
                dialog.dismiss()
            } else {
                Toast.makeText(
                    context,
                    "Es necesario indicar la media de jugaores",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        bCancelar.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}