package com.example.gamesfriends.viewModel.dialogs

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import com.example.gamesfriends.R

class Dialog_infoTAJA(val context: Context) {

    fun mostrar() {
        val dialog = Dialog(context)

        val vista = LayoutInflater.from(context).inflate(R.layout.vetana_info_taja, null)
        dialog.setContentView(vista)
        dialog.window?.setBackgroundDrawableResource(R.drawable.fondo_con_borde_verde)

        // para que se cierre al pinchar fuera
        dialog.setCanceledOnTouchOutside(true)

        vista.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }


}