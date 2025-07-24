package edu.ucne.registrotecnicos.presentation.cliente

import edu.ucne.registrotecnicos.data.local.entity.ClienteEntity

data class ClienteUiState(
    val clienteId: Int? = null,
    val nombres: String? = null,
    val whatsApp: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val clientes: List<ClienteEntity> = emptyList(),
    val inputError: String? = null
)
