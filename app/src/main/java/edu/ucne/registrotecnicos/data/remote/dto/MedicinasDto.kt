package edu.ucne.registrotecnicos.data.remote.dto

data class MedicinasDto(
    val medicinaId: Int,
    val descripcion: String? = null,
    val monto: Double
)
