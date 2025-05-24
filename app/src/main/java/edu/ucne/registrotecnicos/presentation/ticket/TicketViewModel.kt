package edu.ucne.registrotecnicos.presentation.ticket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrotecnicos.data.local.entity.TecnicoEntity
import edu.ucne.registrotecnicos.data.local.entity.TicketEntity
import edu.ucne.registrotecnicos.data.repository.TecnicoRepository
import edu.ucne.registrotecnicos.data.repository.TicketRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
class TicketViewModel @Inject constructor(
    private val ticketRepository: TicketRepository,
    private val tecnicoRepository: TecnicoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState get() = _uiState.asStateFlow()

    init {
        getTickets()
        getTecnicos()
    }

    fun saveTicket() {
        viewModelScope.launch {
            // Validate the form
            if (_uiState.value.cliente.isBlank() ||
                _uiState.value.asunto.isBlank() ||
                _uiState.value.fecha.isBlank() ||
                _uiState.value.tecnicoId == null ||
                _uiState.value.prioridadId == null
            ) {
                _uiState.update {
                    it.copy(mensajeError = "Todos los campos son obligatorios.", mensajeExito = null)
                }
                return@launch
            }

            try {
                // Save the ticket
                ticketRepository.saveTicket(_uiState.value.toEntity())
                // Update UI for success
                _uiState.update {
                    it.copy(mensajeExito = "Ticket guardado con éxito.", mensajeError = null)
                }
                nuevoTicket()
            } catch (e: Exception) {
                // Handle error during ticket save
                _uiState.update {
                    it.copy(mensajeError = "Error al guardar el ticket: ${e.message}", mensajeExito = null)
                }
            }
        }
    }

    fun nuevoTicket() {
        _uiState.update {
            it.copy(
                ticketId = null,
                fecha = "",
                cliente = "",
                asunto = "",
                descripcion = "",
                tecnicoId = null,
                prioridadId = null,
                mensajeError = null,
                mensajeExito = null
            )
        }
    }

    fun selectTicket(ticketId: Int) {
        viewModelScope.launch {
            if (ticketId > 0) {
                val ticket = ticketRepository.find(ticketId)
                _uiState.update {
                    it.copy(
                        ticketId = ticket?.ticketId,
                        fecha = ticket?.fecha ?: "",
                        cliente = ticket?.cliente ?: "",
                        asunto = ticket?.asunto ?: "",
                        descripcion = ticket?.descripcion ?: "",
                        tecnicoId = ticket?.tecnicoId,
                        prioridadId = ticket?.prioridadId
                    )
                }
            }
        }
    }

    fun deleteTicket() {
        viewModelScope.launch {
            try {
                ticketRepository.delete(_uiState.value.toEntity())
                // On success
                _uiState.update {
                    it.copy(mensajeExito = "Ticket eliminado con éxito.", mensajeError = null)
                }
                nuevoTicket()
            } catch (e: Exception) {
                // On error
                _uiState.update {
                    it.copy(mensajeError = "Error al eliminar el ticket: ${e.message}", mensajeExito = null)
                }
            }
        }
    }

    fun getTickets() {
        viewModelScope.launch {
            ticketRepository.getAll().collect { tickets ->
                _uiState.update {
                    it.copy(tickets = tickets)
                }
            }
        }
    }

    fun getTecnicos() {
        viewModelScope.launch {
            tecnicoRepository.getAll().collect { tecnicos ->
                _uiState.update {
                    it.copy(tecnicos = tecnicos)
                }
            }
        }
    }

    fun onFechaChange(fecha: String) {
        _uiState.update {
            it.copy(fecha = fecha)
        }
    }

    fun onClienteChange(cliente: String) {
        _uiState.update {
            it.copy(cliente = cliente)
        }
    }

    fun onAsuntoChange(asunto: String) {
        _uiState.update {
            it.copy(asunto = asunto)
        }
    }

    fun onDescripcionChange(descripcion: String) {
        _uiState.update {
            it.copy(descripcion = descripcion)
        }
    }

    fun onTecnicoIdChange(tecnicoId: Int?) {
        _uiState.update {
            it.copy(tecnicoId = tecnicoId)
        }
    }

    fun onPrioridadIdChange(newValue: Int?) {
        _uiState.value = _uiState.value.copy(prioridadId = newValue)
    }

    data class UiState(
        val ticketId: Int? = null,
        val fecha: String = "",
        val cliente: String = "",
        val asunto: String = "",
        val descripcion: String = "",
        val tecnicoId: Int? = null,
        val prioridadId: Int? = null,
        val mensajeError: String? = null,
        val mensajeExito: String? = null,
        val tickets: List<TicketEntity> = emptyList(),
        val tecnicos: List<TecnicoEntity> = emptyList()
    )

    // Convert UiState to TicketEntity
    fun UiState.toEntity() = TicketEntity(
        ticketId = ticketId,
        fecha = fecha,
        cliente = cliente,
        asunto = asunto,
        descripcion = descripcion,
        tecnicoId = tecnicoId ?: 1,
        prioridadId = prioridadId ?: 1
    )
}