package edu.ucne.registrotecnicos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Clientes")
data class ClienteEntity(
    @PrimaryKey
    val clienteId: Int? = null,
    val nombres: String = "",
    val whatsApp: String = ""
)
