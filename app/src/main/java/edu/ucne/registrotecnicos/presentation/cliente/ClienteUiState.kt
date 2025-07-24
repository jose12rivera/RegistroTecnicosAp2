package edu.ucne.registrotecnicos.presentation.cliente

import edu.ucne.registrotecnicos.data.remote.dto.ClienteDto

data class ClienteUiState(
    val clienteId: Int? = null,
    val nombres: String? = null,
    val whatsApp: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val clientes: List<ClienteDto> = emptyList(),
    val inputError: String? = null
)
