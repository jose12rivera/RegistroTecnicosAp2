package edu.ucne.registrotecnicos.presentation.medicina


import edu.ucne.registrotecnicos.data.remote.dto.MedicinasDto
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrotecnicos.data.remote.Resource

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
        return !_uiState.value.nombre.isNullOrBlank()
    }

    fun update() {
        viewModelScope.launch {
            medicinaRepository.updateMedicina(
                MedicinasDto(
                    medicinaId = _uiState.value.medicinaId ?: 0,
                    nombre = _uiState.value.nombre ?: "",
                    descripcion = _uiState.value.descripcion,
                    monto = _uiState.value.monto
                )
            )
        }
    }

    fun getMedicinas() {
        viewModelScope.launch {
            medicinaRepository.getMedicinas().collectLatest { result ->
                when (result) {
                    is Resource.Loading -> _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                    is Resource.Success -> _uiState.update {
                        it.copy(medicinas = result.data ?: emptyList(), isLoading = false, errorMessage = null)
                    }
                    is Resource.Error -> _uiState.update {
                        it.copy(errorMessage = result.message ?: "Error desconocido", isLoading = false)
                    }
                }
            }
        }
    }
}

data class MedicinaUiState(
    val medicinaId: Int? = null,
    val nombre: String? = null,
    val descripcion: String? = null,
    val monto: Double = 0.0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val medicinas: List<MedicinasDto> = emptyList()
)
