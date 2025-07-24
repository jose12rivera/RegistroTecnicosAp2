package edu.ucne.registrotecnicos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import edu.ucne.registrotecnicos.data.remote.dto.ClienteDto

@Entity(tableName = "Clientes")
data class ClienteEntity(
    @PrimaryKey
    var clienteId: Int? = null,
    val nombres: String = "",
    val whatsApp: String = ""
)

fun ClienteEntity.toDto(): ClienteDto {
    return ClienteDto(
        clienteId = this.clienteId ?: 0,
        nombres = this.nombres,
        whatsApp = this.whatsApp
    )
}
