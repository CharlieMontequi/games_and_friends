package com.example.gamesfriends.view.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.gamesfriends.R
import com.example.gamesfriends.model.DataBaseHelper
import com.example.gamesfriends.viewModel.Gestor

class Fragment_detalle_juego : Fragment() {


    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val vista = inflater.inflate(R.layout.fragment_detalle_juego, container, false)
        return vista
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState) // Llamada al método de la superclase.
        val gestor = Gestor(requireContext())
        val idUsuario = gestor.obetenerIdRegistro()

        val idJuego = arguments?.getInt("ID_JUEGO_DETALLE")

        val dbHelper = DataBaseHelper(requireContext())
        val juegoEnColeccion = dbHelper.detalleJuegoTiene(idUsuario!!)
        val juego = dbHelper.detalleJuego(idJuego!!)

        /*
        obtener listado de mecanicas
        nombres de mecanicas
         */
        val mecanicasEnjuego = dbHelper.listaMecanicasEnJuegoDadoJuego(idJuego)

        val txtMecanicas = view.findViewById<TextView>(R.id.txt_tipoJuego_fragment)
        val txtDescripcion = view.findViewById<TextView>(R.id.txt_descripcionJuego_fragment)
        val txtVecesJugado = view.findViewById<TextView>(R.id.txt_veces_jugado_fragmentDetalleJuego)
        val txtUltimaVez =
            view.findViewById<TextView>(R.id.txt_fecha_ultimoJuegada_fragmentDetalleJuego)
        val bTAJA = view.findViewById<Button>(R.id.b_calcularTAJA)

        if (juegoEnColeccion != null) {
            txtDescripcion.text = juego!!.descipcionJuegp
            txtVecesJugado.text = juegoEnColeccion!!.vecesJugado_coleccion.toString()
            txtUltimaVez.text = juegoEnColeccion!!.ultimaVezJugado_coleccion.toString()
            txtMecanicas.text = mecanicasEnjuego.joinToString(", ")
            bTAJA.setOnClickListener { calcularTAJA() }

        } else {
            bTAJA.visibility = View.INVISIBLE
            bTAJA.isEnabled = false
            txtUltimaVez.text = "Nunca lo has jugado."
            txtVecesJugado.text = "No lo tienes en la colección."
            txtDescripcion.text = juego!!.descipcionJuegp
            txtMecanicas.text = mecanicasEnjuego.joinToString(", ")
        }

    }

    private fun calcularTAJA() {

    }
}