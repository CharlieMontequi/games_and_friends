package com.example.gamesfriends.model.datos

import java.time.LocalDate


data class Coleccion(
    val id_coleccion: Int?,
    val precioCompra_coleccion: Double,
    var vecesJugado_coleccion: Int,
    var ultimaVezJugado_coleccion: LocalDate?,
    var anotacionPersonal_coleccion: String?,
    val fk_usuario_tiene_coleccion: Int,
    val fk_juego_en_coleccion: Int
)
