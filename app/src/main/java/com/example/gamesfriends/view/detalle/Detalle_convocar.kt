package com.example.gamesfriends.view.detalle

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gamesfriends.R
import java.util.Calendar

class Detalle_convocar  : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detalle_convocar)


        val txtNombrePartida = findViewById<EditText>(R.id.etxt_nombrePartida)
        val listaJuegosAllevar = findViewById<ListView>(R.id.listV_juegosAlllevar)
        val txtFechaRecogida = findViewById<TextView>(R.id.txt_fechaMarcada)
        val imB_calendario = findViewById<ImageButton>(R.id.imB_calendario)
        val bProponer= findViewById<Button>(R.id.b_proponer_juegos_convocar)
        val b_invitar = findViewById<Button>(R.id.b_compartir_invitacion)

        val calendar = Calendar.getInstance()

        val datePicker = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val fechaSeleccionada = "$dayOfMonth/${month + 1}/$year"
                // Aquí haces lo que quieras con la fecha seleccionada
                txtFechaRecogida.text = fechaSeleccionada
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        imB_calendario.setOnClickListener {
            datePicker.show()
        }

        bProponer.setOnClickListener {
            Toast.makeText(this, "se abrirar una ventana de filtrado", Toast.LENGTH_LONG).show()
        }

        b_invitar.setOnClickListener {
            Toast.makeText(this, "se abrira opcioens para enviar texto", Toast.LENGTH_LONG).show()
        }

    }
}
