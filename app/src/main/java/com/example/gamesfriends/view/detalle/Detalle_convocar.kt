package com.example.gamesfriends.view.detalle

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.gamesfriends.R
import com.example.gamesfriends.model.DataBaseHelper
import com.example.gamesfriends.model.datos.Coleccion
import com.example.gamesfriends.model.datos.Juego
import com.example.gamesfriends.model.datos.JuegoUsado
import com.example.gamesfriends.view.Cuerpo_app
import com.example.gamesfriends.view.Historial
import com.example.gamesfriends.view.Juego_nuevo
import com.example.gamesfriends.view.MainActivity
import com.example.gamesfriends.viewModel.Gestor
import com.example.gamesfriends.viewModel.dialogs.DialogConvocarFiltrado
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale

class Detalle_convocar : AppCompatActivity() {

    private lateinit var gestor: Gestor
    private val juegosAActualizar = mutableListOf<Coleccion>()
    private var jugadoresInvitados = 0
    private lateinit var  listadoJuegosFiltradoTiempo: MutableList<Juego>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detalle_convocar)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val dbHelper = DataBaseHelper(this)
        var listadoJuego = mutableListOf<Juego>()

        gestor = Gestor(this)
        val idUsuario = gestor.obetenerIdRegistro()
        val toolbarCuerpo = findViewById<Toolbar>(R.id.toolbar_convocar_detalle)
        setSupportActionBar(toolbarCuerpo)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val txtNombrePartida = findViewById<EditText>(R.id.etxt_nombrePartida)
        val listaViewJuegosAllevar = findViewById<ListView>(R.id.listV_juegosAlllevar)
        val txtFechaRecogida = findViewById<TextView>(R.id.txt_fechaMarcada)
        val imB_calendario = findViewById<ImageButton>(R.id.imB_calendario)
        val bProponer = findViewById<Button>(R.id.b_proponer_juegos_convocar)
        val b_invitar = findViewById<Button>(R.id.b_compartir_invitacion)

        val calendar = Calendar.getInstance()

        val datePicker = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val fechaSeleccionada = LocalDate.of(year, month + 1, dayOfMonth)
                // Aquí haces lo que quieras con la fecha seleccionada
                val fechaActual = LocalDate.now()

                if (fechaSeleccionada.isAfter(fechaActual)) {

                    txtFechaRecogida.text = fechaSeleccionada.toString()
                } else {
                    Toast.makeText(this, "No puedes jugar en el pasado", Toast.LENGTH_SHORT).show()
                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        imB_calendario.setOnClickListener {
            datePicker.show()
        }

        bProponer.setOnClickListener {
            if (txtFechaRecogida.text.toString().equals("Sin determinar")) {
                Toast.makeText(this, "Es necesario marcar la fecha primero", Toast.LENGTH_SHORT)
                    .show()
            } else {

                DialogConvocarFiltrado(this) { jugadores, idMecanica, horas ->
                    if (idMecanica == 0) {
                        listadoJuego =
                            dbHelper.listaJuegosPropiedadSiendoJuegosLoObtenido(idUsuario)
                    } else {
                        listadoJuego =
                            dbHelper.listadoJuegosFiltrados(idUsuario, jugadores, horas, idMecanica)
                    }

                    jugadoresInvitados = jugadores

                     listadoJuegosFiltradoTiempo = calculoHoras(listadoJuego, horas)
                    val nombresJuego =
                        listadoJuegosFiltradoTiempo!!.map { it.nombreJuego }.toTypedArray()
                    val adapter =
                        ArrayAdapter(this, android.R.layout.simple_list_item_1, nombresJuego)
                    listaViewJuegosAllevar.adapter = adapter

                    juegosAActualizar.clear()

                    listadoJuegosFiltradoTiempo.forEach { juegoJuegado ->

                        val juegosEnPropiedad = dbHelper.listaJuegosPropiedad(idUsuario)
                        juegosEnPropiedad.forEach { juegoEnColeccion ->
                            if (juegoEnColeccion.id_coleccion == juegoJuegado.idJuego) {
                                val copiaJuego = juegoEnColeccion.copy(
                                    vecesJugado_coleccion = juegoEnColeccion.vecesJugado_coleccion + 1,
                                    ultimaVezJugado_coleccion = txtFechaRecogida.text.toString()
                                )
                                juegosAActualizar.add(copiaJuego)
                            }
                        }


                    }
                }.mostrar()
            }

        }


        b_invitar.setOnClickListener {
            val idHistorialNuevo =  dbHelper.crearHistorial(
                com.example.gamesfriends.model.datos.Historial(
                    id_historial = null,
                    txtNombrePartida.text.toString(),
                    txtFechaRecogida.text.toString(),
                    jugadoresInvitados,
                    idUsuario
                )
            )
            juegosAActualizar.forEach {
                dbHelper.actualizarJuegoEnColeccion(it)
                dbHelper.crearJuegoEnUso(JuegoUsado(idJuegoUsado = null, fkJuegoJuegousado = it.fk_juego_en_coleccion!!, fkHistorialJuegousado = idHistorialNuevo))
            }

            val nombresJuegos = listadoJuegosFiltradoTiempo.joinToString(", "){it.nombreJuego}
            val mensaje = "¡Hola! Te convoco a ${txtNombrePartida.text.toString()}. Una emocionante jornada de juegos de mesa el día ${txtFechaRecogida.text.toString()}. " +
                    "Seremos unas ${jugadoresInvitados.toString()} personas y jugaremos a: $nombresJuegos."

            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, mensaje)
            }

            val chooser = Intent.createChooser(intent, "Enviar invitación con...")
            startActivity(chooser)
            finish()
        }

    }

    private fun calculoHoras(listadoJuegos: MutableList<Juego>, horas: Int): MutableList<Juego> {
        var juegosFiltradosSumatorioDuracion = mutableListOf<Juego>()
        val duracionMaxima = horas * 60
        var duracionIncremental = 0
        listadoJuegos.forEach { juego ->
            if (duracionIncremental < duracionMaxima) {
                juegosFiltradosSumatorioDuracion.add(juego)
                duracionIncremental += juego.duracionJuego
            }
        }

        return juegosFiltradosSumatorioDuracion
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
                    "En desarrollo, lo sentimos",
                    Toast.LENGTH_SHORT
                ).show()
                true
            }

            R.id.item_acercaDe_general -> {
                Toast.makeText(
                    this,
                    "Aplicacion de desarrollada por Carlos Montequi",
                    Toast.LENGTH_SHORT
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
