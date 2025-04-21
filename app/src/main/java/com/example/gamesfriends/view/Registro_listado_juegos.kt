package com.example.gamesfriends.view

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.gamesfriends.R
import com.example.gamesfriends.model.DataBaseHelper
import com.example.gamesfriends.model.datos.Coleccion
import com.example.gamesfriends.model.datos.Juego
import com.example.gamesfriends.model.datos.Usuario
import com.example.gamesfriends.viewModel.dialogs.DialogAgregarJuegoVerDos
import com.example.gamesfriends.viewModel.Gestor

class Registro_listado_juegos : AppCompatActivity() {

    private lateinit var todosJuegosListados: List<Juego>
    private val elementosSeleccionados = mutableListOf<Int>()
    private lateinit var listaMostrarJuegs: ListView

    private  val juegosEnColeccion = mutableListOf<Coleccion>()

    private lateinit var juegosAgregados: MutableList<Coleccion>

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registro_nuevo_usuario_seleccion_juegos)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val usuarioEnRegristro = intent.getParcelableExtra<Usuario>("USUARIO", Usuario::class.java)

        val dbHelper = DataBaseHelper(this)
        todosJuegosListados = dbHelper.listaTodosJuegosBBDD()

        val bGuardarRegistrar = findViewById<Button>(R.id.b_Guardar_Registrar)
        listaMostrarJuegs = findViewById(R.id.listV_todos_juegos_registro_2)
        val adaptadorPersola =
            AdaptadorPersonalizado(this, R.layout.item_listado_check, todosJuegosListados)
        listaMostrarJuegs.adapter = adaptadorPersola


        bGuardarRegistrar.setOnClickListener {
            if (usuarioEnRegristro != null) {
                val idUsuarioNuevo = dbHelper.crearUsuario(usuarioEnRegristro)

                juegosEnColeccion.forEach { coleccion ->
                    coleccion.fk_usuario_tiene_coleccion = idUsuarioNuevo
                    dbHelper.crearJuegoEnPropiedad(coleccion)
                }

                val gestor = Gestor(this)
                gestor.estaRegristrado(true)
                gestor.guardarIdRegistro(idUsuarioNuevo)


            }
            val intent = Intent(this, Cuerpo_app::class.java)
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
                R.layout.item_listado_check,
                parent,
                false
            )

            // los componentes de la fila
            val checkBox = rowView.findViewById<CheckBox>(R.id.checkBox_marcador_listadoitem)
            checkBox.isChecked = elementosSeleccionados.contains(position)

            checkBox.setOnCheckedChangeListener { _, isChecked ->
                val juego = todosJuegosListados[position]

                if (isChecked) {
                    if (!elementosSeleccionados.contains(position)) {
                        elementosSeleccionados.add(position)
                    }

                    // Mostrar el diálogo y añadir a colección
                    DialogAgregarJuegoVerDos(context) { precio, veces, ultimaVez ->
                        val nuevaColeccion = Coleccion(
                            id_coleccion = null,
                            precioCompra_coleccion = precio,
                            vecesJugado_coleccion = veces,
                            ultimaVezJugado_coleccion = ultimaVez,
                            anotacionPersonal_coleccion = null,
                            fk_usuario_tiene_coleccion = -1,  // Se llenará luego
                            fk_juego_en_coleccion = juego.idJuego!!
                        )
                        juegosEnColeccion.add(nuevaColeccion)
                        Toast.makeText(context, "Juego añadido a colección", Toast.LENGTH_SHORT).show()
                    }.mostrar(juego.nombreJuego)

                } else {
                    elementosSeleccionados.remove(position)

                    // Eliminar el juego de la colección
                    val juegoId = juego.idJuego
                    juegosEnColeccion.removeAll { it.fk_juego_en_coleccion == juegoId }

                    Toast.makeText(context, "Juego eliminado de colección", Toast.LENGTH_SHORT).show()
                }
            }


            rowView.findViewById<TextView>(R.id.txt_item_texto_listado_chech).text =
                todosJuegosListados[position].nombreJuego
            return rowView
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            return crearListadoJuegos(position, convertView, parent)
        }
    }

    // crear coleccion

    fun construyendoColeccion(posicion: Int, idUsuario: Int): Coleccion {
        var coleccion = Coleccion(
            null,
            -1.0,
            -1,
            null,
            null,
            idUsuario,
            todosJuegosListados[posicion].idJuego!!
        )
        DialogAgregarJuegoVerDos(this) { precio, veces, ultimaVez ->
            coleccion = Coleccion(
                id_coleccion = null,
                precio,
                veces,
                ultimaVez,
                null,
                idUsuario,
                todosJuegosListados[posicion].idJuego!!
            )
        }.mostrar(todosJuegosListados[posicion].nombreJuego)
        return coleccion
    }

}