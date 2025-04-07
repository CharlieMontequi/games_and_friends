package com.example.gamesfriends.viewModel

import android.content.Context
import android.content.SharedPreferences

class Gestor (contexto : Context) {

    // instancia de las preferencias
    // se toma el modo_private para la gestion de cuentas(?)
    private val sharedPreferences : SharedPreferences = contexto.getSharedPreferences("AppPreference", Context.MODE_PRIVATE)

    companion object{
        private const val ESTA_REGISTRADO ="esta_registrado"
        private const val ID_USUAIO_REGISTRADO ="ID_USUARIO"
    }

    // funcion para guardar que en las preferencias que esta registrado
    fun estaRegristrado (estaRegistrado: Boolean){

        // por clave valor se indica que el refistro es true o false
        sharedPreferences.edit().putBoolean(ESTA_REGISTRADO, estaRegistrado).apply()
    }

    fun comprobarEstadoRegistro(): Boolean{

        // toma el valor guardado, por defecto duelve false
        return sharedPreferences.getBoolean(ESTA_REGISTRADO, false)
    }

    fun guardarIdRegistro (id: Int){
        sharedPreferences.edit().putInt(ID_USUAIO_REGISTRADO, id).apply()
    }
    fun obetenerIdRegistro():Int{
        return sharedPreferences.getInt(ID_USUAIO_REGISTRADO, 0)
    }
}