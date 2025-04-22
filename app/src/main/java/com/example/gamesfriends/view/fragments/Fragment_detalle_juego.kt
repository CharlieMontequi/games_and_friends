package com.example.gamesfriends.view.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.gamesfriends.R
import com.example.gamesfriends.model.DataBaseHelper
import com.example.gamesfriends.model.datos.Juego
import com.example.gamesfriends.viewModel.DetalleJuegoViewModel
import com.example.gamesfriends.viewModel.Gestor
import com.example.gamesfriends.viewModel.dialogs.DialogTAJA
import com.example.gamesfriends.viewModel.dialogs.Dialog_mostrar_resultado_TAJA
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.reflect.KProperty

class Fragment_detalle_juego : Fragment() {
    private lateinit var juego: Juego
    private lateinit var dbHelper: DataBaseHelper
    private lateinit var gestor: Gestor

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

        gestor = Gestor(requireContext())

        val idJuego = arguments?.getInt("ID_JUEGO_DETALLE") ?: return
        dbHelper = DataBaseHelper(requireContext())
        juego = dbHelper.detalleJuego(idJuego)!!
        val mecanicasEnjuego = dbHelper.listaMecanicasEnJuegoDadoJuego(idJuego)

        val txtMecanicas = view.findViewById<TextView>(R.id.txt_tipoJuego_fragment)
        val txtDescripcion = view.findViewById<TextView>(R.id.txt_descripcionJuego_fragment)
        val txtVecesJugado = view.findViewById<TextView>(R.id.txt_veces_jugado_fragmentDetalleJuego)
        val txtUltimaVez =
            view.findViewById<TextView>(R.id.txt_fecha_ultimoJuegada_fragmentDetalleJuego)
        val bTAJA = view.findViewById<Button>(R.id.b_calcularTAJA)

        txtDescripcion.text = juego?.descipcionJuegp ?: "Sin descripción"
        txtMecanicas.text = mecanicasEnjuego.joinToString(", ")

        val detalleViewModel =
            ViewModelProvider(requireActivity())[DetalleJuegoViewModel::class.java]

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
            calcularTAJA(juego)
        }
    }

    private fun calcularTAJA(juegoTaja: Juego) {
        val idUsuario = gestor.obetenerIdRegistro()
        Toast.makeText(requireContext(),"usuario "+idUsuario.toString()+" id juego "+ juegoTaja.idJuego.toString(), Toast.LENGTH_SHORT).show()
        val juegoEnConleccion = dbHelper.detalleJuegoTiene( idUsuario, juegoTaja.idJuego!!)

        if (juegoEnConleccion != null) {
            val precioCompra = juegoEnConleccion.precioCompra_coleccion
            val duracion = BigDecimal(juegoTaja.duracionJuego.toDouble() / 60)
                .setScale(2, RoundingMode.HALF_UP)
                .toDouble()
            val vecesJugado = juegoEnConleccion.vecesJugado_coleccion
            var numeroMedioJugadores = 0
            var aprovechamiento = 0.0
            if(vecesJugado<=0){
                Toast.makeText(requireContext(),"No se puede calcular T.A.J.A. si no se ha jugado al menos una vez",Toast.LENGTH_SHORT).show()
            }else{
                DialogTAJA(requireContext()) { jugadoresMedio ->
                    if (jugadoresMedio > 0) {
                        numeroMedioJugadores = jugadoresMedio
                        aprovechamiento = precioCompra / (duracion * vecesJugado * numeroMedioJugadores)
                        Dialog_mostrar_resultado_TAJA(requireContext(), aprovechamiento).mostrar()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "No se puede calcular con jugadores a cero",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }.mostrar()
            }

        } else {
            Toast.makeText(
                requireContext(),
                "Fallo critico. Contacte con el desarrllador.",
                Toast.LENGTH_SHORT
            ).show()
        }

    }
}