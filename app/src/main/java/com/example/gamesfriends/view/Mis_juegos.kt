package com.example.gamesfriends.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.icu.text.Transliterator.Position
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
import com.example.gamesfriends.model.datos.Juego
import com.example.gamesfriends.view.detalle.Detalle_juego
import com.example.gamesfriends.view.detalle.Detalle_perfil
import com.example.gamesfriends.viewModel.Gestor
import org.w3c.dom.Text

class Mis_juegos : AppCompatActivity() {

    private lateinit var dbHelper: DataBaseHelper
    private var idUsuarioRegistrado: Int = -1
    private lateinit var listadoJuegos: List<Juego>
    private lateinit var gestor :Gestor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.listado_juegos)


        gestor = Gestor(this)
        idUsuarioRegistrado = gestor.obetenerIdRegistro()
        dbHelper = DataBaseHelper(this)
        listadoJuegos = dbHelper.listaJuegosPropiedadSiendoJuegosLoObtenido(idUsuarioRegistrado)

        val toolbarCuerpo = findViewById<Toolbar>(R.id.toolbar_listadoJuegos)
        setSupportActionBar(toolbarCuerpo)

        val listadoJuegosColeccion = findViewById<ListView>(R.id.listView_listado_juegos)
        val adaptaador =
            AdaptadorPersonalizado(this, R.layout.item_juego_listado_juegos, listadoJuegos)
        listadoJuegosColeccion.adapter = adaptaador
        listadoJuegosColeccion.setOnItemClickListener { _, _, position, _ ->
            val juegoSeleccionado = listadoJuegos[position]
            val intent = Intent(this, Detalle_juego::class.java)
            intent.putExtra("ID_JUEGO", juegoSeleccionado.idJuego)
            startActivity(intent)
        }

    }

    //ADAPTADORRRR
    private inner class AdaptadorPersonalizado(
        context: Context,
        resource: Int,
        objects: List<Juego>
    ) : ArrayAdapter<Juego>(context, resource, objects) {

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
            return crearListadoJuegos(position, convertView, parent)
        }


        private fun crearListadoJuegos(position: Int, convertView: View?, parent: ViewGroup): View {
            val layoutInflater = LayoutInflater.from(context)

            val rowView = convertView ?: layoutInflater.inflate(
                R.layout.item_juego_listado_juegos,
                parent,
                false
            )

            // los componentes de la fila
            rowView.findViewById<TextView>(R.id.txt_nombreJuego_item_listado_juego).text =
                listadoJuegos[position].nombreJuego
            rowView.findViewById<TextView>(R.id.txt_nJugadores_item_listado_juegos).text =
                "${listadoJuegos[position].minimoJugadoresJuego}-${listadoJuegos[position].maximoJugadoresJuego} jgs"
            rowView.findViewById<TextView>(R.id.txt_tiempo_item_listado_jeugos).text =
                listadoJuegos[position].duracionJuego.toString() + "min"
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