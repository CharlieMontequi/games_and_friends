package com.example.gamesfriends.viewModel.dialogs

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Switch
import android.widget.Toast
import com.example.gamesfriends.R
import com.example.gamesfriends.model.DataBaseHelper
import com.example.gamesfriends.viewModel.Gestor
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Dialong_juegoAletorio(
    val context: Context,
    private val onAceptar: (idJuegoMarcado: Int) -> Unit
) {
    private lateinit var dbHelper: DataBaseHelper
    fun mostrar() {
        val dialog = Dialog(context)

        val vista = LayoutInflater.from(context).inflate(R.layout.ventana_juego_aleatorio, null)
        dialog.setContentView(vista)
        dialog.window?.setBackgroundDrawableResource(R.drawable.fondo_con_borde_verde)

        dbHelper = DataBaseHelper(context)
        val listadoMecanicas = dbHelper.listaTodasMecanicas()

        val nombresMecanicasArray =
            arrayOf("Selecciona una mecánica...") + listadoMecanicas.map { it.nombreMecanica }

        val etxt_numJugadores = vista.findViewById<EditText>(R.id.etxt_juegoAletaroio_nJugadores)
        val spinnerMecanicaAletaria =
            vista.findViewById<Spinner>(R.id.spinner_mecnicas_juegoAleatorio)
        val switchJugarAhora = vista.findViewById<Switch>(R.id.switch_juegoAleatorio)
        val b_aceptar = vista.findViewById<Button>(R.id.b_aceptar_juegoAleatorio)
        val b_cancelar = vista.findViewById<Button>(R.id.b_cancelarJuegoAleatorio)

        val adapter =
            ArrayAdapter(context, android.R.layout.simple_spinner_item, nombresMecanicasArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMecanicaAletaria.adapter = adapter

        b_aceptar.setOnClickListener {

            var njugadores = 0
            val idMecanica: Int
            val itemSelected = spinnerMecanicaAletaria.selectedItemPosition
            val inputJugadores = etxt_numJugadores.text.toString()

            if (inputJugadores.isNotBlank()) {
                if (etxt_numJugadores.text.toString().toInt() <= 0) {
                    Toast.makeText(
                        context,
                        "Los jugadores no pueden ser cero o negativos",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    njugadores = etxt_numJugadores.editableText.toString().toIntOrNull() ?: 0
                }
            }

            if (itemSelected == 0) {
                idMecanica = 0
            } else {
                idMecanica = listadoMecanicas[itemSelected - 1].id_mecanica!!
            }
            val idJuegoMarcado =
                DarJuegoAleatorio(njugadores, idMecanica, switchJugarAhora.isChecked)
            if (idJuegoMarcado > 0) {
                dialog.dismiss()
                onAceptar(idJuegoMarcado)
            } else {
                Toast.makeText(
                    context,
                    "No se encontró un juego que cumpla los criterios",
                    Toast.LENGTH_SHORT
                ).show()
                dialog.dismiss()
            }

        }
        b_cancelar.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun DarJuegoAleatorio(jugadores: Int, idMecanica: Int, ahora: Boolean): Int {
        var idJuego = 0
        val gestor = Gestor(context)

        val idUsuario = gestor.obetenerIdRegistro()

        // sin jugadores marcado y con mecanica
        if (jugadores == 0 && idMecanica > 0) {
            idJuego = JuegoAleatorioColeccion(
                dbHelper.todaColecciónUsuarioPorMecanica(
                    idUsuario,
                    idMecanica
                )
            )
        }
        //sin mecanica cargada y con jugadores
        if (idMecanica == 0 && jugadores > 0) {
            idJuego = JuegoAleatorioColeccion(
                dbHelper.todaColeccionUsuarioPorNumJugadores(
                    idUsuario,
                    jugadores
                )
            )

        }
        //con jugadores y mecánica
        if (jugadores > 0 && idMecanica > 0) {
            idJuego = JuegoAleatorioColeccion(
                dbHelper.todaColeccionUsuarioConMecanicaYJugadores(
                    idUsuario,
                    jugadores,
                    idMecanica
                )
            )
        }

        // sin mecánica marcada y sin  jugadores
        if (jugadores == 0 && idMecanica == 0) {
            idJuego = JuegoAleatorioColeccion(dbHelper.todaColeccionDelUsuario(idUsuario))
        }

        if (ahora) {
            val juegoColeccion = dbHelper.detalleJuegoTiene(idUsuario, idJuego)
            if (juegoColeccion != null) {
                val fechaHoy = LocalDate.now()
                juegoColeccion.vecesJugado_coleccion += 1
                juegoColeccion.ultimaVezJugado_coleccion =
                    fechaHoy.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                dbHelper.actualizarJuegoEnColeccion(juegoColeccion)
            }

        }

        return idJuego
    }

    private fun JuegoAleatorioColeccion(coleccion: MutableList<Int>): Int {
        if (coleccion.isEmpty()) {
            return 0
        }
        val idJuegoAleatorio = coleccion.random() // elige un id aleatorio
        return idJuegoAleatorio
    }


}