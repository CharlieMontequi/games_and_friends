package com.example.gamesfriends.view.detalle

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gamesfriends.R
import com.example.gamesfriends.model.DataBaseHelper
import com.example.gamesfriends.model.datos.Usuario
import com.example.gamesfriends.view.Juego_nuevo
import com.example.gamesfriends.view.MainActivity
import com.example.gamesfriends.viewModel.Gestor
import org.w3c.dom.Text
import kotlin.time.TestTimeSource

class Detalle_perfil : AppCompatActivity() {

    private lateinit var dbHelper: DataBaseHelper
    private lateinit var gestor: Gestor

    private lateinit var nombre: EditText
    private lateinit var correo: EditText
    private lateinit var contrasenia: EditText
    private lateinit var guardar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detalle_perfil)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        dbHelper = DataBaseHelper(this)
        gestor = Gestor(this)

        val idUsuario = gestor.obetenerIdRegistro()

        val usuarioRegistrado = dbHelper.detalleUsuario(idUsuario)

        nombre = findViewById(R.id.etxt_nombre_detalle_perfil)
        correo = findViewById(R.id.etxt_correo_perfil_detalle)
        contrasenia = findViewById(R.id.etxt_contrasenia_perfil_detalle)
        guardar = findViewById(R.id.b_guardar_perfil_detalle)
        val txtJuegoColeccionTotal = findViewById<TextView>(R.id.txt_nueroJuegos_perdil_detalle)
        txtJuegoColeccionTotal.text = dbHelper.totalJuegosEnColeccion(idUsuario).toString()

        val toolbarCuerpo =
            findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_perfil_detalel)
        setSupportActionBar(toolbarCuerpo)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        if (usuarioRegistrado != null) {
            nombre.setText(usuarioRegistrado.nombre_usuario)
            correo.setText(usuarioRegistrado.correo_usuario)
            contrasenia.setText(usuarioRegistrado.contrasenia_usuario)

        }
        soloVer()

        guardar.setOnClickListener {
            dbHelper.actualizarUsuario(
                Usuario(
                    id_usuario = idUsuario,
                    nombre_usuario = nombre.text.toString(),
                    correo_usuario = correo.text.toString(),
                    contrasenia_usuario = contrasenia.text.toString()
                )
            )
            soloVer()
        }
    }

    // TOOLBAR
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_toolbar_perfil_detalle, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.item_perfil_perfilEditar -> {
                editarPerfil()
                true
            }

            R.id.item_perfil_borrar_perfil -> {
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


    fun editarPerfil() {

        nombre.isEnabled = true
        correo.isEnabled = true
        contrasenia.isEnabled = true
        guardar.isEnabled = true
        guardar.visibility = View.VISIBLE
    }

    fun soloVer() {

        nombre.isEnabled = false
        correo.isEnabled = false
        contrasenia.isEnabled = false
        guardar.isEnabled = false
        guardar.visibility = View.INVISIBLE
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
