package com.example.gamesfriends.model.datos

data class Juego(
    var idJuego: Int? = null,
    var nombreJuego: String = "",
    var descipcionJuegp: String = "",
    var minimoJugadoresJuego: Int = -1,
    var maximoJugadoresJuego: Int = -1,
    var duracionJuego: Int = -1
)
