package com.example.gamesfriends.view

import android.content.Context
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
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.gamesfriends.R
import com.example.gamesfriends.model.DataBaseHelper
import com.example.gamesfriends.model.datos.Juego
import com.example.gamesfriends.model.datos.Usuario

class Registro_listado_juegos : AppCompatActivity() {

    private lateinit var todosJuegosListados: List<Juego>

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registro_nuevo_usuario_seleccion_juegos)

        val usuarioEnRegristro = intent.getParcelableExtra<Usuario>("USUARIO", Usuario::class.java)

        val dbHelper= DataBaseHelper(this)
        todosJuegosListados = dbHelper.listaTodosJuegosBBDD()

        val listaMostrarJuegs = findViewById<ListView>(R.id.listV_todos_juegos_registro_2)
        val bGuardarRegistrar = findViewById<Button>(R.id.b_Guardar_Registrar)
        val adaptadorPersola = AdaptadorPersonalizado(this, R.layout.item_listado_check, todosJuegosListados)
        listaMostrarJuegs.adapter= adaptadorPersola




        bGuardarRegistrar.setOnClickListener {
            if (usuarioEnRegristro != null) {
                dbHelper.crearUsuario(usuarioEnRegristro)
            }

           // dbHelper.crearJuegoEnPropiedad()
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
            rowView.findViewById<CheckBox>(R.id.checkBox_marcador_listadoitem)
            rowView.findViewById<TextView>(R.id.txt_item_texto_listado_chech).text = todosJuegosListados[position].nombreJuego
            return rowView
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            return crearListadoJuegos(position, convertView, parent)
        }
    }

}