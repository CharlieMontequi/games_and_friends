package com.example.gamesfriends.viewModel.dialogs

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import com.example.gamesfriends.R

class Dialog_mostrar_resultado_TAJA(private val context: Context, val tasaTaja: Double) {

    fun mostrar() {
        val dialog = Dialog(context)

        val vista = LayoutInflater.from(context).inflate(R.layout.ventana_resultado_taja, null)

        dialog.setContentView(vista)
        dialog.window?.setBackgroundDrawableResource(R.drawable.fondo_con_borde_verde)

        val txtResultado = vista.findViewById<TextView>(R.id.txt_resultado_taja)
        val tasaTajaRedondeada = String.format("%.2f", tasaTaja)

        val mensaje = when {
            tasaTaja <= 1 -> context.getString(R.string.taja1)
            tasaTaja < 5 -> context.getString(R.string.taja2)
            else -> context.getString(R.string.taja3)
        }

        txtResultado.text = "$mensaje $tasaTajaRedondeada"

        dialog.show()
    }
}