package com.example.gamesfriends.viewModel.dialogs

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.example.gamesfriends.R
import java.time.LocalDate
import java.util.Calendar

class DialogAgregarJuegoVerDos(
    private val context: Context,
    private val onAceptar: (precio: Double, veces: Int, ultimaVez: LocalDate?) -> Unit
) {

    private var fechaSeleccionada: LocalDate? = null

    fun mostrar(texto :String?) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // sin título predeterminado
        val vista = LayoutInflater.from(context).inflate(R.layout.ventana_emergente_al_sumarlo_coleccion_ver2, null)

        dialog.setContentView(vista)
        dialog.window?.setBackgroundDrawableResource(R.drawable.fondo_con_borde_verde) // fondo personalizado

        // Referencias UI
        val titulo = vista.findViewById<TextView>(R.id.txt_titulo_emergente_add_juego_coleccion)

        if(texto!=null){
            titulo.text= texto
        }
        val inputPrecio = vista.findViewById<EditText>(R.id.etxt_precioCompra_AddJuego)
        val inputVecesJugado = vista.findViewById<EditText>(R.id.etxt_vecesJugado_AddJuego)
        val imbMostrarCalendario = vista.findViewById<ImageButton>(R.id.imb_seleccionarFecha__AddJuego)
        val inputFecha = vista.findViewById<TextView>(R.id.txt_fechaUltimaVez_AddJuego)
        val btnAceptar = vista.findViewById<Button>(R.id.b_aceptar_addJuego)
        val btnCancelar = vista.findViewById<Button>(R.id.b_cancelar_addJuego)

        // Calendario
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val fecha = LocalDate.of(year, month + 1, dayOfMonth)
                val fechaActual = LocalDate.now()

                if (fecha.isBefore(fechaActual)) {
                    fechaSeleccionada = fecha
                    inputFecha.text = fecha.toString()
                } else {
                    Toast.makeText(context, "No puedes jugar en el futuro", Toast.LENGTH_SHORT).show()
                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        imbMostrarCalendario.setOnClickListener { datePicker.show() }

        // Botones
        btnAceptar.setOnClickListener {
            val precio = inputPrecio.text.toString().toDoubleOrNull() ?: 0.0
            val veces = inputVecesJugado.text.toString().toIntOrNull() ?: 0
            onAceptar(precio, veces, fechaSeleccionada)
            dialog.dismiss()
        }

        btnCancelar.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    //comprobar fecha anterior
    fun comprobarFechaSeleccionada(context: Context, fecha: LocalDate): String? {
        val fechaActual = LocalDate.now()

        return if (fecha.isBefore(fechaActual)) {
            fecha.toString()
        } else {
            Toast.makeText(context, "No puedes jugar en el futuro", Toast.LENGTH_SHORT).show()
            null
        }
    }
}