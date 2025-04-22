package com.example.gamesfriends.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.gamesfriends.R
import com.example.gamesfriends.model.DataBaseHelper
import com.example.gamesfriends.model.datos.Historial
import com.example.gamesfriends.model.datos.Juego
import com.example.gamesfriends.view.detalle.Detalle_historial
import com.example.gamesfriends.view.detalle.Detalle_perfil
import com.example.gamesfriends.viewModel.Gestor

class Historial : AppCompatActivity() {

    private lateinit var dbHelper: DataBaseHelper
    private var idUsuarioRegistrado: Int = -1
    private lateinit var listadoHitorial: List<Historial>
    private lateinit var gestor: Gestor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.listado_historiales)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        dbHelper = DataBaseHelper(this)

        val toolbarCuerpo = findViewById<Toolbar>(R.id.toolbar_historial)
        setSupportActionBar(toolbarCuerpo)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        gestor = Gestor(this)
        idUsuarioRegistrado = gestor.obetenerIdRegistro()
        dbHelper = DataBaseHelper(this)
        listadoHitorial = dbHelper.listaTodasPartidas(idUsuarioRegistrado)

        val listVHistoriales = findViewById<ListView>(R.id.listV_listado_historail)
        val adaptadorPropio =
            AdaptadorPersonalizado(this, R.layout.item_listado_historial, listadoHitorial)
        listVHistoriales.adapter = adaptadorPropio

        listVHistoriales.setOnItemClickListener { _, _, position, _ ->
            val historialSeleccionado = listadoHitorial[position]
            val intent =Intent(this, Detalle_historial::class.java)
            intent.putExtra("ID_HISTORIAL",listadoHitorial[position].id_historial)
            startActivity(intent)
        }
    }


    //ADAPTADOIRRRRRRRR
    private inner class AdaptadorPersonalizado(
        context: Context,
        resource: Int,
        objects: List<Historial>
    ) : ArrayAdapter<Historial>(context, resource, objects) {

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
            return crearListadoJuegos(position, convertView, parent)
        }


        private fun crearListadoJuegos(position: Int, convertView: View?, parent: ViewGroup): View {
            val layoutInflater = LayoutInflater.from(context)

            val rowView = convertView ?: layoutInflater.inflate(
                R.layout.item_listado_historial,
                parent,
                false
            )

            // los componentes de la fila
            rowView.findViewById<TextView>(R.id.txt_nombrePartida_item_listado_partida).text =
                listadoHitorial[position].nombre_historial
            rowView.findViewById<TextView>(R.id.txt_numero_juegos_item_listaHistorial).text =
                listadoHitorial[position].id_historial?.let {
                    dbHelper.agrupacionJuegosEnUso(it).toString() + " juegos"
                }
            rowView.findViewById<TextView>(R.id.txt_fecha_item_listado_partida).text=listadoHitorial[position].fecha_historial.toString()
            rowView.findViewById<TextView>(R.id.txt_numero_personas_itemListadoHistorial).text =
                listadoHitorial[position].numeroPersonas_historial.toString() + " personas"
            return rowView
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            return crearListadoJuegos(position, convertView, parent)
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
                finish()
                true
            }

            R.id.item_addJuego_bbd_general -> {
                val intent = Intent(this, Juego_nuevo::class.java)
                startActivity(intent)
                finish()
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