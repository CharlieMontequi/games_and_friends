package com.example.gamesfriends.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gamesfriends.model.datos.Coleccion

class DetalleJuegoViewModel : ViewModel() {
    private val _estaEnColeccion = MutableLiveData<Boolean>()
    val estaEnColeccion: LiveData<Boolean> = _estaEnColeccion

    private val _datosColeccion = MutableLiveData<Coleccion?>()
    val datosColeccion: LiveData<Coleccion?> = _datosColeccion

    fun actualizarEstado(esta: Boolean, datos: Coleccion?) {
        _estaEnColeccion.value = esta
        _datosColeccion.value = datos
    }
}
