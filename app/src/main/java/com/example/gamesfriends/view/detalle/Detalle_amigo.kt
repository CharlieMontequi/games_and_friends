package com.example.gamesfriends.view.detalle

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.gamesfriends.R
import com.example.gamesfriends.model.DataBaseHelper
import com.example.gamesfriends.viewModel.Gestor

class Detalle_amigo : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detalle_amigo)

        val gestor = Gestor(this)
        val idUsuario = gestor.obetenerIdRegistro()
        val idAmigo = intent.getIntExtra("ID_AMIGO", 0)

        val dbHelper = DataBaseHelper(this)
        val usuarioAmigo = dbHelper.detalleUsuario(idAmigo)

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
}