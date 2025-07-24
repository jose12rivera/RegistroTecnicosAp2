package edu.ucne.registrotecnicos.presentation.cliente

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrotecnicos.data.local.entity.ClienteEntity
import edu.ucne.registrotecnicos.data.remote.Resource
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

    fun saveCliente() {
        if (!validarCampos()) return

        val currentState = _uiState.value
        viewModelScope.launch {
            try {
                val cliente = ClienteEntity(
                    clienteId = currentState.clienteId,
                    nombres = currentState.nombres.orEmpty(),
                    whatsApp = currentState.whatsApp.orEmpty()
                )
                clienteRepository.save(cliente)
                getClientes()
                limpiarCampos()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message ?: "Error al guardar cliente") }
            }
        }
    }

    fun deleteCliente() {
        val currentState = _uiState.value
        viewModelScope.launch {
            try {
                val cliente = ClienteEntity(
                    clienteId = currentState.clienteId,
                    nombres = currentState.nombres.orEmpty(),
                    whatsApp = currentState.whatsApp.orEmpty()
                )
                clienteRepository.delete(cliente)
                getClientes()
                limpiarCampos()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message ?: "Error al eliminar cliente") }
            }
        }
    }

    fun getClientes() {
        viewModelScope.launch {
            clienteRepository.getAll().collectLatest { result ->
                when (result) {
                    is Resource.Loading -> _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                    is Resource.Success -> _uiState.update {
                        it.copy(
                            clientes = result.data ?: emptyList(),
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                    is Resource.Error -> _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message ?: "Error desconocido"
                        )
                    }
                }
            }
        }
    }

    fun selectCliente(id: Int) {
        viewModelScope.launch {
            val cliente = clienteRepository.find(id)
            cliente?.let {
                _uiState.update {
                    it.copy(
                        clienteId = cliente.clienteId,
                        nombres = cliente.nombres,
                        whatsApp = cliente.whatsApp
                    )
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

    fun limpiarCampos() {
        _uiState.update {
            it.copy(
                clienteId = null,
                nombres = null,
                whatsApp = null,
                inputError = null,
                errorMessage = null
            )
        }
    }
}
