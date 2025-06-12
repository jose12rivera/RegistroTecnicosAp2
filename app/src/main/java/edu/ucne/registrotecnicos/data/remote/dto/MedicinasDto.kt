package edu.ucne.registrotecnicos.data.remote.dto

data class MedicinasDto(
    val medicinaId: Int,
    val nombre: String? = null,
    val descripcion: String? = null,
    val monto: Double
)
