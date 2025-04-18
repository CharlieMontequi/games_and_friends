package com.example.gamesfriends.model.datos

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Usuario(
    val id_usuario: Int?,
    var nombre_usuario: String,
    var correo_usuario: String?,
    var contrasenia_usuario: String?
) :
    Parcelable

