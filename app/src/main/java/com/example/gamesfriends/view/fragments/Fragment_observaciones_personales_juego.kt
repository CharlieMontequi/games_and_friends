package com.example.gamesfriends.view.fragments

import android.content.Context
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.example.gamesfriends.R
import com.example.gamesfriends.model.DataBaseHelper
import com.example.gamesfriends.viewModel.Gestor

class Fragment_observaciones_personales_juego : Fragment() {

    private lateinit var imbGuardar: ImageButton
    private lateinit var imbEditar: ImageButton
    private lateinit var txtObservacioens: EditText

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val vista = inflater.inflate(R.layout.fragment_observaciones_personales, container, false)
        return vista
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState) // Llamada al método de la superclase.

        val gestor = Gestor(requireContext())
        val idUsuario = gestor.obetenerIdRegistro()

        val dbHelper = DataBaseHelper(requireContext())
        var juegoEnColeccion = dbHelper.detalleJuegoTiene(idUsuario!!)

        txtObservacioens = view.findViewById<EditText>(R.id.etxt_observacionesfragment)
        imbGuardar = view.findViewById<ImageButton>(R.id.imB_guardar)
        imbEditar = view.findViewById<ImageButton>(R.id.imB_editar)

        if (juegoEnColeccion != null) {
            txtObservacioens.setText(juegoEnColeccion.anotacionPersonal_coleccion)

            imbEditar.setOnClickListener { inicarEdicion() }
            imbGuardar.setOnClickListener {
                soloVer()
                juegoEnColeccion.anotacionPersonal_coleccion = txtObservacioens.text.toString()
            }

        } else {
            txtObservacioens.setText("Este juego no forma parte de tu colección. No puedes añadir observaciones. \n Añadelo a la colección para poder escribir una observación.")
            juegoSinPropiedad()
        }


    }

    fun inicarEdicion() {
        imbGuardar.visibility = View.VISIBLE
        imbGuardar.isEnabled = true
        txtObservacioens.isEnabled = true

        imbEditar.isEnabled = false
    }

    fun soloVer() {
        imbGuardar.isEnabled = false
        txtObservacioens.isEnabled = false

        imbEditar.isEnabled = true
    }

    fun juegoSinPropiedad() {
        imbGuardar.isEnabled = false
        imbEditar.visibility = View.INVISIBLE
        imbEditar.isEnabled = false
    }
}