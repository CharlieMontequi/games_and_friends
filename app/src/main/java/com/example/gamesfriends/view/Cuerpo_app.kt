package com.example.gamesfriends.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.gamesfriends.R
import com.example.gamesfriends.model.DataBaseHelper
import com.example.gamesfriends.view.detalle.Detalle_convocar
import com.example.gamesfriends.view.detalle.Detalle_perfil
import com.example.gamesfriends.viewModel.Gestor

class Cuerpo_app : AppCompatActivity() {

    private lateinit var dbHelper: DataBaseHelper
    private lateinit var estadoRegistro :Gestor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cuerpo_app)

        dbHelper = DataBaseHelper(this)

        val toolbarCuerpo = findViewById<Toolbar>(R.id.toolbar_cuerpo_app)
        setSupportActionBar(toolbarCuerpo)

        val b_misJuegos = findViewById<Button>(R.id.b_misJuegos_cuerpo)
        val b_amigos = findViewById<Button>(R.id.b_amistades_cuerpo)
        val b_historial = findViewById<Button>(R.id.b_historial_cuerpo)
        val b_convocar = findViewById<Button>(R.id.b_convocar_cuerpo)

        b_convocar.setOnClickListener {
            val intent = Intent(this, Detalle_convocar::class.java)
            startActivity(intent)
        }
        b_amigos.setOnClickListener {
            val intent = Intent(this, Amistades::class.java)
            startActivity(intent)
        }
        b_historial.setOnClickListener {
            val intent = Intent(this, Historial::class.java)
            startActivity(intent)
        }
        b_misJuegos.setOnClickListener {
            val intent = Intent(this, Mis_juegos::class.java)
            startActivity(intent)
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
                Toast.makeText(
                    this,
                    "En desarrollo helmosho",
                    Toast.LENGTH_LONG
                ).show()
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
                estadoRegistro = Gestor(this)
                estadoRegistro.estaRegristrado(false)
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