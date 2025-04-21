package com.example.gamesfriends.view.detalle

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.gamesfriends.R
import com.example.gamesfriends.view.Juego_nuevo
import com.example.gamesfriends.view.MainActivity
import com.example.gamesfriends.viewModel.Gestor
import java.util.Calendar

class Detalle_convocar  : AppCompatActivity() {

    private lateinit var gestor : Gestor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detalle_convocar)

        gestor = Gestor(this)
        val idUsuario = gestor.obetenerIdRegistro()
        val toolbarCuerpo = findViewById<Toolbar>(R.id.toolbar_convocar_detalle)
        setSupportActionBar(toolbarCuerpo)
        supportActionBar?.setDisplayShowTitleEnabled(false)

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

    // TOOLBAR
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inlfater: MenuInflater = menuInflater
        inlfater.inflate(R.menu.menu_toolbar_general, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.item_juego_perfil -> {
                val intent = Intent(this, Detalle_perfil::class.java)
                startActivity(intent)
                true
            }

            R.id.item_addJuego_bbd_general -> {
                val intent = Intent(this, Juego_nuevo::class.java)
                startActivity(intent)
                true
            }

            R.id.item_notificaciones_general -> {
                Toast.makeText(
                    this,
                    "En desarrollo helmosho2",
                    Toast.LENGTH_LONG
                ).show()
                true
            }

            R.id.item_acercaDe_general -> {
                Toast.makeText(
                    this,
                    "Aplicacion de juegos de mesa- dialog en desarrollo",
                    Toast.LENGTH_LONG
                ).show()
                true
            }

            R.id.item_salir_desconectar_general -> {
                gestor = Gestor(this)
                gestor.estaRegristrado(false)
                Toast.makeText(
                    this,
                    "Adiosito",
                    Toast.LENGTH_LONG
                ).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
