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
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import com.example.gamesfriends.R
import com.example.gamesfriends.model.DataBaseHelper
import com.example.gamesfriends.model.datos.Usuario
import com.example.gamesfriends.view.detalle.Detalle_amigo
import com.example.gamesfriends.view.detalle.Detalle_juego
import com.example.gamesfriends.view.detalle.Detalle_perfil
import com.example.gamesfriends.viewModel.Gestor

class Amistades : AppCompatActivity() {

    private lateinit var listaDeAmigosTienes: List<Usuario>
    private lateinit var gestor: Gestor
    private var idUsuarioRegistrado: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.listado_amigos)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        gestor = Gestor(this)
        idUsuarioRegistrado = gestor.obetenerIdRegistro()

        val dbHelper = DataBaseHelper(this)
        val tienesAmigops = dbHelper.tienesAmigos(idUsuarioRegistrado)

        val toolbarAmigos = findViewById<Toolbar>(R.id.toolbar_listado_amigos)
        setSupportActionBar(toolbarAmigos)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val searchViewBuscarAmigos = findViewById<SearchView>(R.id.searchV_nombreAmigos_listado_amigos)
        val lisaAmigos = findViewById<ListView>(R.id.listView_listadoAmigos_lista_amigos)

        searchViewBuscarAmigos.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val filtro = query.orEmpty().trim()

                // Regex: cualquier texto + @ + uno o más dígitos al final
                val patron = Regex(".*@\\d+$")

                if (patron.matches(filtro)) {
                    val idPosibleAmigo = filtro.substringAfter("@")
                    val idAmigo = idPosibleAmigo.toIntOrNull() ?: -1
                    val usuario = dbHelper.detalleUsuario(idAmigo)

                    if (usuario != null && usuario.id_usuario!! > 0) {
                        val intentDetalleAmigo = Intent(this@Amistades, Detalle_amigo::class.java)
                        intentDetalleAmigo.putExtra("ID_AMIGO", usuario.id_usuario)
                        startActivity(intentDetalleAmigo)

                    } else {
                        Toast.makeText(
                            this@Amistades,
                            "No existe ningún usuario con ese ID.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@Amistades,
                        "Formato inválido. Usa nombre@numero",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                return true // Indica que manejaste el submit
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false // No hacemos nada con el cambio de texto
            }
        })


        if (tienesAmigops) {
            // cargar datos amigos
            listaDeAmigosTienes = dbHelper.listadoTodosAmigosObteniendoElAmigo(idUsuarioRegistrado)
            val adaotadorConAmigos =
                AdaptadorPersonalizado(this, R.layout.item_listado_amigos, listaDeAmigosTienes)
            lisaAmigos.adapter = adaotadorConAmigos

            lisaAmigos.setOnItemClickListener { _, _, position, _ ->
                val amigoSeleccionado = listaDeAmigosTienes[position]
                val intent = Intent(this, Detalle_amigo::class.java)
                intent.putExtra("ID_AMIGO", amigoSeleccionado.id_usuario)
                startActivity(intent)
            }

        } else {
            // array de no tienes amigos aun
            val noAmigos = arrayOf(
                "No tienes amigo",
                "Guardado por él",
                "Momento",
                "Usa la barra de búsqueda para encontrar tus amigos"
            )
            val adaptadorSinAmigos =
                ArrayAdapter(this, android.R.layout.simple_list_item_1, noAmigos)
            lisaAmigos.adapter = adaptadorSinAmigos
        }
    }

    //ADAPTADORRRR
    private inner class AdaptadorPersonalizado(
        context: Context,
        resource: Int,
        objects: List<Usuario>
    ) : ArrayAdapter<Usuario>(context, resource, objects) {

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
            return crearListadoJuegos(position, convertView, parent)
        }


        private fun crearListadoJuegos(position: Int, convertView: View?, parent: ViewGroup): View {
            val layoutInflater = LayoutInflater.from(context)

            val rowView = convertView ?: layoutInflater.inflate(
                R.layout.item_listado_amigos,
                parent,
                false
            )

            // los componentes de la fila
            rowView.findViewById<TextView>(R.id.txt_item_nombre_lista_amigos).text =
                listaDeAmigosTienes[position].nombre_usuario

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