package com.example.gamesfriends.view.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.gamesfriends.R
import com.example.gamesfriends.model.DataBaseHelper

class Fragment_historial_cuerpo : Fragment() {


    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val vista = inflater.inflate(R.layout.fragment_detalle_historial_cuerpo, container, false)
        return vista
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState) // Llamada al método de la superclase.

        val idHistorial = arguments?.getInt("ID_HISTORIAL_DETALLE",0)


        val dbHelper = DataBaseHelper(requireContext())
        val historialDetallado = dbHelper.detalleHistorial(idHistorial!!)
        val listaJuegosPartida = dbHelper.listadoTodosJuegosPartidaMostrandoJuego(idHistorial)
        val nombresJuegos = listaJuegosPartida.map { it.nombreJuego }

        val txtNombrePartida =
            view.findViewById<TextView>(R.id.txt_nombreQuedada_fragment_detalle_historia)
        val txtFecha = view.findViewById<TextView>(R.id.txt_fechaHistorial_fragment)
        val txtParticpantes =
            view.findViewById<TextView>(R.id.txt_numeroParticpantes_fragmentDetalleHistorial)

        txtNombrePartida.text = historialDetallado!!.nombre_historial
        txtFecha.text = historialDetallado!!.fecha_historial.toString()
        txtParticpantes.text = historialDetallado!!.numeroPersonas_historial.toString()

        val listaJuego = view.findViewById<ListView>(R.id.listView_historial_detalle_fragment)
        val adaptar =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, nombresJuegos)
        listaJuego.adapter = adaptar

    }
}