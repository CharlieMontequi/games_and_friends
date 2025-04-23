package com.example.gamesfriends.view.detalle

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.gamesfriends.R
import com.example.gamesfriends.model.DataBaseHelper
import com.example.gamesfriends.model.datos.Amigo
import com.example.gamesfriends.view.Cuerpo_app
import com.example.gamesfriends.view.Juego_nuevo
import com.example.gamesfriends.view.MainActivity
import com.example.gamesfriends.viewModel.Gestor

class Detalle_amigo : AppCompatActivity() {

    private lateinit var gestor :Gestor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detalle_amigo)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        gestor = Gestor(this)
        val idUsuario = gestor.obetenerIdRegistro()
        val idAmigo = intent.getIntExtra("ID_AMIGO", -1)


        val dbHelper = DataBaseHelper(this)
        val usuarioAmigo = dbHelper.detalleUsuario(idAmigo)
        val ultimoJuegoJugado = dbHelper.ulitmoJuegoJugado(idAmigo)

        if (usuarioAmigo == null) {
            Toast.makeText(this, "Error: amigo no encontrado", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        val toolbarCuerpo = findViewById<Toolbar>(R.id.toolbar_detalle_amigo)
        setSupportActionBar(toolbarCuerpo)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        var sonAmigos = dbHelper.comrpobarSiSonAmigos(idUsuario,idAmigo)


        val listadoJuegoAmnigo = dbHelper.listaJuegosPropiedadSiendoJuegosLoObtenido(idAmigo)
        val listadoNombreJuegos = listadoJuegoAmnigo.map { it.nombreJuego }


        val txtNombreAmigo = findViewById<TextView>(R.id.txt_nombre_detalle_amigo)
        val txtUltimoJuegoJugado = findViewById<TextView>(R.id.txt_ultimoJuegoJugado_amigoDetalle)

        txtNombreAmigo.text = usuarioAmigo.nombre_usuario + "@"+ usuarioAmigo.id_usuario
        Toast.makeText(this, "EL JUEGO ES "+ ultimoJuegoJugado!!.nombreJuego + ultimoJuegoJugado.idJuego.toString(), Toast.LENGTH_SHORT).show()
        txtUltimoJuegoJugado.text= ultimoJuegoJugado!!.nombreJuego

        val bAniadirAmigo = findViewById<Button>(R.id.b_add_amigo)
        val bBorrarAmigo = findViewById<Button>(R.id.b_borrar_amigo)

        val listJuegoColeccionAmigo = findViewById<ListView>(R.id.listView_juegosAmigoDetalle)
        val adaotador= ArrayAdapter(this, android.R.layout.simple_list_item_1, listadoNombreJuegos)
        listJuegoColeccionAmigo.adapter= adaotador

        if(sonAmigos){
            bAniadirAmigo.visibility= View.INVISIBLE
            bAniadirAmigo.isEnabled= false
        }else{
            bBorrarAmigo.visibility= View.INVISIBLE
            bBorrarAmigo.isEnabled= false
        }

        bAniadirAmigo.setOnClickListener {
            try {
                dbHelper.crearAmigos(Amigo(null, idUsuario, idAmigo))
                Toast.makeText(this, "Ahora son amigos", Toast.LENGTH_SHORT).show()
                recreate() // Actualizamos la actividad para reflejar los cambios
            } catch (e: Exception) {
                Log.e("DetalleAmigo", "Error al añadir amigo: ${e.message}")
                Toast.makeText(this, "Error al añadir amigo", Toast.LENGTH_SHORT).show()
            }
        }

        bBorrarAmigo.setOnClickListener {
            try {
                dbHelper.borrarAmigo(idUsuario, idAmigo)
                Toast.makeText(this, "Amigo eliminado", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, Cuerpo_app::class.java)  // Corrigiendo nombre de variable
                startActivity(intent)
                finish() // Terminamos la actividad actual
            } catch (e: Exception) {
                Log.e("DetalleAmigo", "Error al borrar amigo: ${e.message}")
                Toast.makeText(this, "Error al borrar amigo", Toast.LENGTH_SHORT).show()
            }
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