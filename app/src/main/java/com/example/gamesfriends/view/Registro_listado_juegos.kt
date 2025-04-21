package com.example.gamesfriends.view

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView.Validator
import android.widget.Button
import android.widget.CheckBox
import android.widget.CheckedTextView
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
import com.example.gamesfriends.viewModel.DialogAgregarJuegoVerDos
import com.example.gamesfriends.viewModel.Gestor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

class Registro_listado_juegos : AppCompatActivity() {

    private lateinit var todosJuegosListados: List<Juego>
    private val elementosSeleccionados = mutableListOf<Int>()
    private lateinit var listaMostrarJuegs: ListView

    private lateinit var juegosAgregados: MutableList<Coleccion>

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registro_nuevo_usuario_seleccion_juegos)

        val usuarioEnRegristro = intent.getParcelableExtra<Usuario>("USUARIO", Usuario::class.java)

        val dbHelper = DataBaseHelper(this)
        todosJuegosListados = dbHelper.listaTodosJuegosBBDD()

        listaMostrarJuegs = findViewById(R.id.listV_todos_juegos_registro_2)
        val bGuardarRegistrar = findViewById<Button>(R.id.b_Guardar_Registrar)
        val adaptadorPersola =
            AdaptadorPersonalizado(this, R.layout.item_listado_check, todosJuegosListados)
        listaMostrarJuegs.adapter = adaptadorPersola

        bGuardarRegistrar.setOnClickListener {
            if (usuarioEnRegristro != null) {
                val idUsuarioNuevo = dbHelper.crearUsuario(usuarioEnRegristro)

                val gestor = Gestor(this)
                gestor.estaRegristrado(true)
                gestor.guardarIdRegistro(idUsuarioNuevo)

                Toast.makeText(this, elementosSeleccionados.joinToString(", "), Toast.LENGTH_SHORT)
                    .show()

                val juegosEnColeccion = mutableListOf<Coleccion>()

                for (posicion in elementosSeleccionados) {

                    juegosEnColeccion.add(construyendoColeccion(posicion, idUsuarioNuevo))
                    Toast.makeText(this, posicion.toString(), Toast.LENGTH_SHORT)
                        .show()
                }

                val intent = Intent(this, Cuerpo_app::class.java)
                startActivity(intent)


            }
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
                if (isChecked) {
                    // Si está marcado, agregar a la lista de seleccionados
                    if (!elementosSeleccionados.contains(position)) {
                        elementosSeleccionados.add(position)
                    }
                } else {
                    // Si no está marcado, eliminar de la lista de seleccionados
                    elementosSeleccionados.remove(position)
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