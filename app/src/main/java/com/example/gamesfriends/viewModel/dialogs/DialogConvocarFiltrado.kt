package com.example.gamesfriends.viewModel.dialogs

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.example.gamesfriends.R
import com.example.gamesfriends.model.DataBaseHelper

class DialogConvocarFiltrado (private val context: Context, private val onAceptar :(numJugadores:Int, idMencanica:Int, horasJuego:Int)-> Unit){

    fun mostrar(){
        val dialog =Dialog(context)

        val vista = LayoutInflater.from(context).inflate(R.layout.ventana_convocar_completar_campos, null)

        dialog.setContentView(vista)
        dialog.window?.setBackgroundDrawableResource(R.drawable.fondo_con_borde_verde)

        val dbHelper= DataBaseHelper(context)
        val listadoMecanicas = dbHelper.listaTodasMecanicas()
        val nombresMecanicasArray = listadoMecanicas.map { it.nombreMecanica }.toTypedArray()

        val inputJugadores = vista.findViewById<EditText>(R.id.etxt_numeroJugadores_ventana_convocar)
        val inputSpinner = vista.findViewById<Spinner>(R.id.spinner_tipo_categoria_convocar)
        val inputHoras = vista.findViewById<EditText>(R.id.etxt_horasJuego_ventana_convocar)
        val bAceptar= vista.findViewById<Button>(R.id.b_aceptar_convocar)
        val bCacelar = vista.findViewById<Button>(R.id.b_cancelar_convocar)

        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item,nombresMecanicasArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        inputSpinner.adapter= adapter

        bAceptar.setOnClickListener {

            val numJugadores = inputJugadores.text.toString().toIntOrNull()?:0
            val horasJuego = inputHoras.text.toString().toIntOrNull()?:0
            val itemSeletd = inputSpinner.selectedItemPosition
            val idMencanica = listadoMecanicas[itemSeletd].id_mecanica

            if (numJugadores <= 0 || horasJuego <= 0) {
                Toast.makeText(context, "Completa todos los campos correctamente", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            onAceptar(numJugadores,idMencanica!!,horasJuego)
            dialog.dismiss()
        }

        bCacelar.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}