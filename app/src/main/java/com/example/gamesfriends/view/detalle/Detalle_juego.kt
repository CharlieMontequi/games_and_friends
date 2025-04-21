package com.example.gamesfriends.view.detalle

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.example.gamesfriends.R
import com.example.gamesfriends.model.DataBaseHelper
import com.example.gamesfriends.model.datos.Coleccion
import com.example.gamesfriends.view.Cuerpo_app
import com.example.gamesfriends.view.MainActivity
import com.example.gamesfriends.view.fragments.Fragment_detalle_juego
import com.example.gamesfriends.view.fragments.Fragment_observaciones_personales_juego
import com.example.gamesfriends.viewModel.DetalleJuegoViewModel
import com.example.gamesfriends.viewModel.dialogs.DialogAgregarJuegoVerDos
import com.example.gamesfriends.viewModel.Gestor

class Detalle_juego : AppCompatActivity() {

    private lateinit var dbHelper: DataBaseHelper
    private lateinit var gestor: Gestor
    private var idUsuario: Int = 0
    private var idJuego = 0

    //////////////////////////////////////////////////////
    private lateinit var detalleViewModel: DetalleJuegoViewModel
//////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detalle_juego)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        gestor = Gestor(this)
        idUsuario = gestor.obetenerIdRegistro()
        val toolbarCuerpo = findViewById<Toolbar>(R.id.toolbar_detalle_juego)
        setSupportActionBar(toolbarCuerpo)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        idJuego = intent.getIntExtra("ID_JUEGO", 0)

        dbHelper = DataBaseHelper(this)

//////////////////////////////////////////////////////
        detalleViewModel = ViewModelProvider(this)[DetalleJuegoViewModel::class.java]
//////////////////////////////////////////////////////

        val juego = dbHelper.detalleJuego(idJuego = idJuego)
        val txt_nombreJuegoDetalle = findViewById<TextView>(R.id.txt_nombre_juego_detalle_juego)
        val txt_NJugadores = findViewById<TextView>(R.id.txt_nJugadores_detalle_juego)
        val txt_duracion = findViewById<TextView>(R.id.txt_tiempoJuego_detalle_juego)

        // Consultar si el juego está en la colección//////////////////////////////////////////////////////
        val estaEnColeccion = dbHelper.comprobarJuegoEnColeccion(idJuego, idUsuario)
        val datosColeccion =
            if (estaEnColeccion) dbHelper.detalleJuegoTiene(idUsuario, idJuego) else null
        detalleViewModel.actualizarEstado(estaEnColeccion, datosColeccion)
        //////////////////////////////////////////////////////

        if (juego != null) {
            txt_nombreJuegoDetalle.text = juego.nombreJuego
            txt_NJugadores.text =
                juego.minimoJugadoresJuego.toString() + " - " + juego.maximoJugadoresJuego.toString() + " jugadores"
            txt_duracion.text = juego.duracionJuego.toString() + "min"

            val bundleIJuego = Bundle()
            bundleIJuego.putInt("ID_JUEGO_DETALLE", juego.idJuego!!)

            val fragmentCuerpoJuegoDetalle = Fragment_detalle_juego()
            fragmentCuerpoJuegoDetalle.arguments = bundleIJuego
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_detalle_juego, fragmentCuerpoJuegoDetalle)
                .commit()


            val fragmentObservacionesPersonales = Fragment_observaciones_personales_juego()
            fragmentObservacionesPersonales.arguments = bundleIJuego
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_observaciones_deatlleJuego, fragmentObservacionesPersonales)
                .commit()
        }

    }

    // TOOLBAR
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_toolbar_detalle_juego, menu)

        val itemAddJuego = menu?.findItem(R.id.item_juegol_addJuego_coleccion)
        val itemBorrarJuego = menu?.findItem(R.id.item_juego_eliminarJuego_coleccion)

        detalleViewModel.estaEnColeccion.value?.let { enColeccion ->
            itemBorrarJuego?.isVisible = enColeccion
            itemAddJuego?.isVisible = !enColeccion
        }


        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.item_juegol_addJuego_coleccion -> {
                DialogAgregarJuegoVerDos(this) { precio, veces, fecha ->
                    val nuevoRegistro = Coleccion(
                        id_coleccion = null,
                        fk_usuario_tiene_coleccion = idUsuario,
                        fk_juego_en_coleccion = idJuego,
                        precioCompra_coleccion = precio,
                        vecesJugado_coleccion = veces,
                        ultimaVezJugado_coleccion = fecha,
                        anotacionPersonal_coleccion = null
                    )

                    dbHelper.crearJuegoEnPropiedad(nuevoRegistro)
                    detalleViewModel.actualizarEstado(true, nuevoRegistro)

                    // Mostrar mensaje y actualizar menú dentro del callback
                    Toast.makeText(this, "Juego añadido a la colección", Toast.LENGTH_LONG).show()
                    invalidateOptionsMenu()
                }.mostrar(null)
                true
            }

            R.id.item_juego_eliminarJuego_coleccion -> {
                dbHelper.borrarJuegoDeJuegosTiene(idJuego, idUsuario)
                detalleViewModel.actualizarEstado(false, null)
                Toast.makeText(this, "Juego eliminado de la colección", Toast.LENGTH_LONG).show()
                val intent= Intent(this, Cuerpo_app::class .java)
                startActivity(intent)
                true
            }

            R.id.item_juego_perfil -> {
                val intent = Intent(this, Detalle_perfil::class.java)
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