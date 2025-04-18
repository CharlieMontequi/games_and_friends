package com.example.gamesfriends.view.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.gamesfriends.R
import com.example.gamesfriends.model.DataBaseHelper
import com.example.gamesfriends.viewModel.DetalleJuegoViewModel
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
        super.onViewCreated(view, savedInstanceState)

        val idJuego = arguments?.getInt("ID_JUEGO_DETALLE") ?: return
        val dbHelper = DataBaseHelper(requireContext())
        val juego = dbHelper.detalleJuego(idJuego)
        val mecanicasEnjuego = dbHelper.listaMecanicasEnJuegoDadoJuego(idJuego)

        val txtMecanicas = view.findViewById<TextView>(R.id.txt_tipoJuego_fragment)
        val txtDescripcion = view.findViewById<TextView>(R.id.txt_descripcionJuego_fragment)
        val txtVecesJugado = view.findViewById<TextView>(R.id.txt_veces_jugado_fragmentDetalleJuego)
        val txtUltimaVez = view.findViewById<TextView>(R.id.txt_fecha_ultimoJuegada_fragmentDetalleJuego)
        val bTAJA = view.findViewById<Button>(R.id.b_calcularTAJA)

        txtDescripcion.text = juego?.descipcionJuegp ?: "Sin descripción"
        txtMecanicas.text = mecanicasEnjuego.joinToString(", ")

        val detalleViewModel = ViewModelProvider(requireActivity())[DetalleJuegoViewModel::class.java]

        detalleViewModel.estaEnColeccion.observe(viewLifecycleOwner) { enColeccion ->
            if (enColeccion) {
                detalleViewModel.datosColeccion.value?.let { datos ->
                    txtVecesJugado.text = datos.vecesJugado_coleccion.toString()
                    txtUltimaVez.text = (datos.ultimaVezJugado_coleccion ?: "Sin info").toString()
                    bTAJA.isEnabled = true
                    bTAJA.visibility = View.VISIBLE
                }
            } else {
                txtVecesJugado.text = "No lo tienes en la colección."
                txtUltimaVez.text = "Nunca lo has jugado."
                bTAJA.isEnabled = false
                bTAJA.visibility = View.INVISIBLE
            }
        }

        bTAJA.setOnClickListener {
            calcularTAJA()
        }
    }

    private fun calcularTAJA() {
// toast que dice que se va a caclular taja
    }
}