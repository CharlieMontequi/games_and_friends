package com.example.gamesfriends.model.datos

import java.time.LocalDate
import java.util.Date

data class Historial(
    val id_historial: Int?,
    val nombre_historial: String,
    val fecha_historial: LocalDate?,
    val numeroPersonas_historial: Int,
    val usuario_historial: Int
)
