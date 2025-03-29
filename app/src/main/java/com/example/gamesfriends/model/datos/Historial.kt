package com.example.gamesfriends.model.datos

import java.sql.Date

data class Historial(
    val id_historial: Int?,
    val nombre_historial: String,
    val fecha_historial: Date,
    val numeroPersonas_historial: Int,
    val usuario_historial: Int,
    val juegosUsados_historial: Array<Int>
)
