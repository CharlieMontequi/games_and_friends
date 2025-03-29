package com.example.gamesfriends.model.datos

import java.sql.Date

data class JuegoEnPropiedad(
    val id_juegoEnPropiedad: Int?,
    val precioCompra_juegoPropiedad: Double,
    val vecesJugado_juegoPropiedad: Int,
    val ultimaVezJugado_juegoPropiedad: Date,
    var anotacionPersonal_juegoPropiedad: String,
    val fk_usuario_juegoPropiedad: Int,
    val juego_juegoPropiedad: Int
)
