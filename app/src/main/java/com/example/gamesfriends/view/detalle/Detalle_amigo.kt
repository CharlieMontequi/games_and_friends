package com.example.gamesfriends.view.detalle

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.gamesfriends.R
import com.example.gamesfriends.model.DataBaseHelper
import com.example.gamesfriends.view.Juego_nuevo
import com.example.gamesfriends.view.MainActivity
import com.example.gamesfriends.viewModel.Gestor

class Detalle_amigo : AppCompatActivity() {

    private lateinit var gestor :Gestor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detalle_amigo)

        gestor = Gestor(this)
        val idUsuario = gestor.obetenerIdRegistro()
        val idAmigo = intent.getIntExtra("ID_AMIGO", 0)

        val dbHelper = DataBaseHelper(this)
        val usuarioAmigo = dbHelper.detalleUsuario(idAmigo)

        val toolbarCuerpo = findViewById<Toolbar>(R.id.toolbar_detalle_amigo)
        setSupportActionBar(toolbarCuerpo)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        /*
        comprobar si el id del usuario y el del amigo estan en la tabla relacionados
        si lo estan habilitar borrar y tapar añadir y sino al reves
         */
        val listadoJuegoAmnigo = dbHelper.listaJuegosPropiedadSiendoJuegosLoObtenido(idAmigo)
        val listadoNombreJuegos = listadoJuegoAmnigo.map { it.nombreJuego }


        val txtNombreAmigo = findViewById<TextView>(R.id.txt_nombre_detalle_amigo)
        val txtUltimoJuegoJugado = findViewById<TextView>(R.id.txt_ultimoJuegoJugado_amigoDetalle)

        val bAniadirAmigo = findViewById<Button>(R.id.b_add_amigo)
        val bBorrarAmigo = findViewById<Button>(R.id.b_borrar_amigo)

        val listJuegoColeccionAmigo = findViewById<ListView>(R.id.listView_juegosAmigoDetalle)
        val adaotador= ArrayAdapter(this, android.R.layout.simple_list_item_1, listadoNombreJuegos)
        listJuegoColeccionAmigo.adapter= adaotador

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