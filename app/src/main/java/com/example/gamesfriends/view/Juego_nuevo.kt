package com.example.gamesfriends.view

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.GridLayout
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.example.gamesfriends.R
import com.example.gamesfriends.model.DataBaseHelper
import com.example.gamesfriends.model.datos.Coleccion
import com.example.gamesfriends.model.datos.Juego
import com.example.gamesfriends.model.datos.Mecanica
import com.example.gamesfriends.model.datos.MecanicaEnJuego
import com.example.gamesfriends.view.detalle.Detalle_perfil
import com.example.gamesfriends.viewModel.dialogs.DialogAgregarJuegoVerDos
import com.example.gamesfriends.viewModel.Gestor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Juego_nuevo : AppCompatActivity() {

    private lateinit var gestor: Gestor
    private lateinit var listadoMecanicas: MutableList<Mecanica>
    private lateinit var checkBoxList: MutableList<CheckBox>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.proponer_juego_bbdd)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val dbHelper = DataBaseHelper(this)

        var nuevoJuegoAColeccion= Coleccion()


        gestor = Gestor(this)

        val toolbarAmigos = findViewById<Toolbar>(R.id.toolbar_juevonuevo)
        setSupportActionBar(toolbarAmigos)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val nombrejuegoNuevo = findViewById<EditText>(R.id.etxt_nombreJuego_nuevo)
        val descripcionJuegoNuevo = findViewById<EditText>(R.id.etxt_descripcionJuego_nuevo)
        val minJugadoresNuevo = findViewById<EditText>(R.id.etxt_minjueg_nuevo)
        val maxJugadoresNuevo = findViewById<EditText>(R.id.etxt_maxjug_nuevo)
        val duracionJuego_Nuevo = findViewById<EditText>(R.id.etxt_duracionjuego_nuevo)
        val switchTambienColeccion = findViewById<Switch>(R.id.switch_add_colecciojn_tambien)

        val b_aniadirJuego = findViewById<Button>(R.id.b_agregar_juego)

        val gridCrearJuego = findViewById<GridLayout>(R.id.grid_mecanicas)

        checkBoxList = mutableListOf()

        // con corrutina para cargar los datos correctamente
        lifecycleScope.launch {
            listadoMecanicas = withContext(Dispatchers.IO) {
                dbHelper.listaTodasMecanicas()
            }

            // agregar los CheckBox en el hilo principal
            for (mecanica in listadoMecanicas) {
                val checkBox = CheckBox(this@Juego_nuevo).apply {
                    text = mecanica.nombreMecanica
                    textSize = 20f
                    setPadding(16, 16, 16, 16)
                    layoutParams = GridLayout.LayoutParams(
                        GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f),
                        GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f)
                    ).apply {
                        width = 0
                        height = ViewGroup.LayoutParams.WRAP_CONTENT
                        setMargins(8, 8, 8, 8)
                    }
                }
                checkBoxList.add(checkBox)
                gridCrearJuego.requestLayout()
                gridCrearJuego.addView(checkBox)
            }
        }

        switchTambienColeccion.setOnClickListener {
            if(switchTambienColeccion.isChecked){
                DialogAgregarJuegoVerDos(this) { precio, veces, fecha ->
                    nuevoJuegoAColeccion= Coleccion(
                        id_coleccion = null,
                        fk_usuario_tiene_coleccion = gestor.obetenerIdRegistro(),
                        fk_juego_en_coleccion = null,
                        precioCompra_coleccion = precio,
                        vecesJugado_coleccion = veces,
                        ultimaVezJugado_coleccion = fecha.toString(),
                        anotacionPersonal_coleccion = null
                    )
                }.mostrar(null)
            }
        }

        b_aniadirJuego.setOnClickListener {
            if (nombrejuegoNuevo.text.isNullOrEmpty() || descripcionJuegoNuevo.text.isNullOrEmpty() || minJugadoresNuevo.text.isNullOrEmpty() || maxJugadoresNuevo.text.isNullOrEmpty() || duracionJuego_Nuevo.text.isNullOrEmpty()) {
                Toast.makeText(
                    this,
                    "Por favor, completa todos los campos para agregar el juego",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val juegoNuevo = Juego(
                    idJuego = null,
                    nombreJuego = nombrejuegoNuevo.text.toString(),
                    descipcionJuegp = descripcionJuegoNuevo.text.toString(),
                    minimoJugadoresJuego = minJugadoresNuevo.text.toString().toInt(),
                    maximoJugadoresJuego = maxJugadoresNuevo.text.toString().toInt(),
                    duracionJuego = duracionJuego_Nuevo.text.toString().toInt()
                )
                val idJuegoNuevo = dbHelper.crearJuego(juegoNuevo)

                val mecanicasSeleccionadas = recogerMecacnias()
                mecanicasSeleccionadas.forEach {mecanica->
                    dbHelper.crearMecanciaEnJuego(MecanicaEnJuego(id_mecanicaJuego = null, fk_juego_mecanicaJuego = idJuegoNuevo, fk_mecanica_mecanicaJuego = mecanica))
                }

                if (switchTambienColeccion.isChecked) {
                    nuevoJuegoAColeccion.fk_juego_en_coleccion= idJuegoNuevo
                    dbHelper.crearJuegoEnPropiedad(nuevoJuegoAColeccion)
                }

            }

            val intent =Intent(this, Cuerpo_app::class.java)
            startActivity(intent)
        }

    }

    private fun recogerMecacnias():MutableList<Int>{
        var mecanicasEnJuego = mutableListOf<Int>()
        for (i in checkBoxList.indices) {
            val checkBox = checkBoxList[i]
            if (checkBox.isChecked) {
                // Si el checkbox está marcado, añadir la mecanica correspondiente
                val mecanicaMarcada = listadoMecanicas[i].id_mecanica
                if (mecanicaMarcada != null) {
                    mecanicasEnJuego.add(mecanicaMarcada)
                }
            }
        }


        return mecanicasEnJuego
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
                Toast.makeText(
                    this,
                    "Y estás en añadir el juego. Completa los campos.",
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