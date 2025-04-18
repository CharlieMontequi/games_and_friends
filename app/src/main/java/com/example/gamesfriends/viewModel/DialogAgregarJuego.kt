package com.example.gamesfriends.viewModel

import android.app.DatePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.gamesfriends.R
import java.time.LocalDate
import java.util.Calendar

class DialogAgregarJuego(
    private val context: Context,
    private val onAceptar: (precio: Double, veces: Int, ultimaVez: LocalDate?) -> Unit
) {

    private var fechaSeleccionada: LocalDate? = null

    fun mostrar(){
        val vista = LayoutInflater.from(context).inflate(R.layout.ventana_emergente_al_sumarlo_coleccion, null)
        val inputPrecio = vista.findViewById<EditText>(R.id.etxt_precioCompra_AddJuego)
        val inputVecesJugado = vista.findViewById<EditText>(R.id.etxt_vecesJugado_AddJuego)
        val imbMostrarCalendario = vista.findViewById<ImageButton>(R.id.imb_seleccionarFecha__AddJuego)
        val inputFecha = vista.findViewById<TextView>(R.id.txt_fechaUltimaVez_AddJuego)

        val calendar = Calendar.getInstance()

        val datePicker = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val fechaSeleccionada = "$dayOfMonth/${month + 1}/$year"
                // Aquí haces lo que quieras con la fecha seleccionada
                inputFecha.text = fechaSeleccionada
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        imbMostrarCalendario.setOnClickListener { datePicker.show() }

        AlertDialog.Builder(context)
            .setTitle("Agregar a colección")
            .setView(vista)
            .setPositiveButton("Aceptar") { _, _ ->
                val precio = inputPrecio.text.toString().toDoubleOrNull() ?: 0.0
                val veces = inputVecesJugado.text.toString().toIntOrNull() ?: 0
                val fecha = inputFecha.text.toString()
                onAceptar(precio, veces, fechaSeleccionada)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}