package com.example.gamesfriends.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.example.gamesfriends.R
import com.example.gamesfriends.model.DataBaseHelper
import com.example.gamesfriends.model.datos.Usuario
import com.example.gamesfriends.view.detalle.Detalle_amigo
import com.example.gamesfriends.view.detalle.Detalle_juego
import com.example.gamesfriends.viewModel.Gestor

class Amistades  : AppCompatActivity() {

     private lateinit var listaDeAmigosTienes: List<Usuario>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.listado_amigos)

        val gestor = Gestor(this)
        val idUsuarioRegistrado = gestor.obetenerIdRegistro()

        val dbHelper = DataBaseHelper(this)
        val tienesAmigops = dbHelper.tienesAmigos(idUsuarioRegistrado)

        val buscarAmigos = findViewById<SearchView>(R.id.searchV_nombreAmigos_listado_amigos)
        val lisaAmigos = findViewById<ListView>(R.id.listView_listadoAmigos_lista_amigos)

        if(tienesAmigops){
            // cargar datos amigos
            listaDeAmigosTienes = dbHelper.listadoTodosAmigosObteniendoElAmigo(idUsuarioRegistrado)
            val adaotadorConAmigos = AdaptadorPersonalizado(this, R.layout.item_listado_amigos, listaDeAmigosTienes)
            lisaAmigos.adapter= adaotadorConAmigos

            lisaAmigos.setOnItemClickListener { _, _, position, _ ->
                val amigoSeleccionado = listaDeAmigosTienes[position]
                val intent = Intent(this, Detalle_amigo::class.java)
                intent.putExtra("ID_AMIGO", amigoSeleccionado.id_usuario)
                startActivity(intent)
            }

        }else{
            // array de no tienes amigos aun
            val noAmigos = arrayOf(
                "No tienes amigo",
                "Guardado por él",
                "Momento",
                "Usa la barra de búsqueda para encontrar tus amigos"
            )
            val adaptadorSinAmigos = ArrayAdapter(this, android.R.layout.simple_list_item_1, noAmigos)
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
            rowView.findViewById<TextView>(R.id.txt_item_nombre_lista_amigos).text = listaDeAmigosTienes[position].nombre_usuario

            return rowView
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            return crearListadoJuegos(position, convertView, parent)
        }
    }
}