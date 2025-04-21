package com.example.gamesfriends.model.datos

import java.time.LocalDate


data class Coleccion(
    var id_coleccion: Int? = null,
    var precioCompra_coleccion: Double = 0.0,
    var vecesJugado_coleccion: Int = 0,
    var ultimaVezJugado_coleccion: LocalDate? = null,
    var anotacionPersonal_coleccion: String? = null,
    var fk_usuario_tiene_coleccion: Int = 0,
    var fk_juego_en_coleccion: Int? = null
)
