package com.example.gamesfriends.view.detalle

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.gamesfriends.R
import com.example.gamesfriends.view.Juego_nuevo
import com.example.gamesfriends.view.MainActivity
import com.example.gamesfriends.view.fragments.Fragment_historial_cuerpo
import com.example.gamesfriends.viewModel.Gestor

class Detalle_historial  : AppCompatActivity() {

    private lateinit var gestor : Gestor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detalle_historial)

        gestor = Gestor(this)
        val idUsuario = gestor.obetenerIdRegistro()
        val toolbarCuerpo = findViewById<Toolbar>(R.id.toolbar_detalle_historial)
        setSupportActionBar(toolbarCuerpo)

        val idHistorial = intent.getIntExtra("ID_HISTORIAL",0)

        val bundleIJuego = Bundle()
        bundleIJuego.putInt("ID_HISTORIAL_DETALLE", idHistorial!!)

        val fragmnetDetalleHistorial = Fragment_historial_cuerpo()
        fragmnetDetalleHistorial.arguments=bundleIJuego
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_detalle_historial_cuerpo, fragmnetDetalleHistorial)
            .commit()

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