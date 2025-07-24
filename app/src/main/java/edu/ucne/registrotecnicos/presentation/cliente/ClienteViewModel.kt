package edu.ucne.registrotecnicos.presentation.cliente

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrotecnicos.data.remote.Resource
import edu.ucne.registrotecnicos.data.remote.dto.ClienteDto
import edu.ucne.registrotecnicos.data.repository.ClienteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClienteViewModel @Inject constructor(
    private val clienteRepository: ClienteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ClienteUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getClientes()
    }

    fun validarCampos(): Boolean {
        val nombres = _uiState.value.nombres
        val whatsApp = _uiState.value.whatsApp

        return when {
            nombres.isNullOrBlank() -> {
                _uiState.update { it.copy(inputError = "El nombre no puede estar vacío") }
                false
            }
            whatsApp.isNullOrBlank() -> {
                _uiState.update { it.copy(inputError = "WhatsApp no puede estar vacío") }
                false
            }
            else -> {
                _uiState.update { it.copy(inputError = null) }
                true
            }
        }
    }

    fun create() {
        val currentState = _uiState.value
        if (!validarCampos()) return

        viewModelScope.launch {
            try {
                clienteRepository.createCliente(
                    ClienteDto(
                        clienteId = 0,
                        nombres = currentState.nombres.orEmpty(),
                        whatsApp = currentState.whatsApp.orEmpty()
                    )
                )
                limpiarCampos()
                getClientes()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = e.localizedMessage ?: "Error al guardar")
                }
            }
        }
    }

    fun update() {
        val currentState = _uiState.value
        if (!validarCampos()) return

        viewModelScope.launch {
            try {
                clienteRepository.updateCliente(
                    ClienteDto(
                        clienteId = currentState.clienteId ?: 0,
                        nombres = currentState.nombres.orEmpty(),
                        whatsApp = currentState.whatsApp.orEmpty()
                    )
                )
                limpiarCampos()
                getClientes()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = e.localizedMessage ?: "Error al actualizar")
                }
            }
        }
    }

    fun getClientes() {
        viewModelScope.launch {
            clienteRepository.getClientes().collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true, errorMessage = null)
                        }
                    }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                clientes = result.data ?: emptyList(),
                                isLoading = false,
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                errorMessage = result.message ?: "Error desconocido",
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }

    fun setNombres(value: String) {
        _uiState.update { it.copy(nombres = value) }
    }

    fun setWhatsApp(value: String) {
        _uiState.update { it.copy(whatsApp = value) }
    }

    fun setClienteId(id: Int) {
        _uiState.update { it.copy(clienteId = id) }
    }

    fun limpiarCampos() {
        _uiState.update {
            it.copy(
                clienteId = null,
                nombres = null,
                whatsApp = null,
                inputError = null
            )
        }
    }
}
