package dev.adan.pc2_picon.model

import java.util.Date

data class MovModel(
    val descripcion: String = "",
    val fecha: Date,
    val monto: Double = 0.0
)
