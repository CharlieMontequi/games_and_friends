package com.example.gamesfriends.view

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
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
import com.example.gamesfriends.view.detalle.Detalle_juego
import com.example.gamesfriends.view.detalle.Detalle_perfil
import com.example.gamesfriends.viewModel.Gestor
import com.example.gamesfriends.viewModel.dialogs.DialogConvocarFiltrado
import com.example.gamesfriends.viewModel.dialogs.Dialong_juegoAletorio

class Cuerpo_app : AppCompatActivity() {

    private lateinit var dbHelper: DataBaseHelper
    private lateinit var gestor :Gestor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cuerpo_app)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        dbHelper = DataBaseHelper(this)

        val toolbarCuerpo = findViewById<Toolbar>(R.id.toolbar_cuerpo_app)
        setSupportActionBar(toolbarCuerpo)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val b_juegoAleatorio = findViewById<Button>(R.id.b_juegoAleatorio)
        val b_misJuegos = findViewById<Button>(R.id.b_misJuegos_cuerpo)
        val b_amigos = findViewById<Button>(R.id.b_amistades_cuerpo)
        val b_historial = findViewById<Button>(R.id.b_historial_cuerpo)
        val b_convocar = findViewById<Button>(R.id.b_convocar_cuerpo)

        b_juegoAleatorio.setOnClickListener {
            Dialong_juegoAletorio(this){idJuego :Int ->
                val intent = Intent(this, Detalle_juego::class.java)
                intent.putExtra("ID_JUEGO", idJuego)
                startActivity(intent)
            }.mostrar()
        }

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
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    ////////////////////////////////////CIERRE AL DAR ATRAS/////////////////////
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        val backDispatcher = onBackPressedDispatcher

        // Llamar al manejador del botón de retroceso
        backDispatcher.onBackPressed()

        // Si necesitas cerrar la actividad
        finish()
    }
}