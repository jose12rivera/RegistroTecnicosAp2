package edu.ucne.registrotecnicos.presentation.medicina

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrotecnicos.data.remote.Resource
import edu.ucne.registrotecnicos.data.remote.dto.MedicinasDto
import edu.ucne.registrotecnicos.data.repository.MedicinaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedicinaViewModel @Inject constructor(
    private val medicinaRepository: MedicinaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MedicinaUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getMedicinas()
    }

    fun validarCampos(): Boolean {
        val descripcion = _uiState.value.descripcion
        val monto = _uiState.value.monto

        return when {
            descripcion.isNullOrBlank() -> {
                _uiState.update { it.copy(inputError = "La descripción no puede estar vacía") }
                false
            }
            monto <= 0.0 -> {
                _uiState.update { it.copy(inputError = "El monto debe ser mayor que cero") }
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
                medicinaRepository.createMedicina(
                    MedicinasDto(
                        descripcion = currentState.descripcion.orEmpty(),
                        monto = currentState.monto
                    )
                )
                limpiarCampos()
                getMedicinas()
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
                medicinaRepository.updateMedicina(
                    MedicinasDto(
                        medicinaId = currentState.medicinaId ?: 0,
                        descripcion = currentState.descripcion.orEmpty(),
                        monto = currentState.monto
                    )
                )
                limpiarCampos()
                getMedicinas()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = e.localizedMessage ?: "Error al actualizar")
                }
            }
        }
    }

    fun getMedicinas() {
        viewModelScope.launch {
            medicinaRepository.getMedicinas().collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true, errorMessage = null)
                        }
                    }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                medicinas = result.data ?: emptyList(),
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

    fun setDescripcion(value: String) {
        _uiState.update { it.copy(descripcion = value) }
    }

    fun setMonto(value: Double) {
        _uiState.update { it.copy(monto = value) }
    }

    fun setMedicinaId(id: Int) {
        _uiState.update { it.copy(medicinaId = id) }
    }

    fun limpiarCampos() {
        _uiState.update {
            it.copy(
                medicinaId = null,
                descripcion = null,
                monto = 0.0,
                inputError = null
            )
        }
    }
}

data class MedicinaUiState(
    val medicinaId: Int? = null,
    val descripcion: String? = null,
    val monto: Double = 0.0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val medicinas: List<MedicinasDto> = emptyList(),
    val inputError: String? = null
)
