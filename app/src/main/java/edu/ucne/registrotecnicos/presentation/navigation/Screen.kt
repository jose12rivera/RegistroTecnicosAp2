package edu.ucne.registrotecnicos.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    data object HomeScreen : Screen()

    @Serializable
    data object TecnicoList : Screen()

    @Serializable
    data class Tecnico(val tecnicoId: Int) : Screen()

    @Serializable
    data class EditTecnico(val tecnicoId: Int) : Screen()

    @Serializable
    data class DeleteTecnico(val tecnicoId: Int) : Screen()

    @Serializable
    data object TicketList : Screen()


    @Serializable
    data class Ticket(val ticketId: Int) : Screen()

    @Serializable
    data class EditTicket(val ticketId: Int) : Screen()

    @Serializable
    data class DeleteTicket(val ticketId: Int) : Screen()


    @Serializable
    data class Mensaje(val ticketId: Int) : Screen()
    @Serializable
    data object MedicinaList : Screen()

    @Serializable
    data class Medicina(val medicinaId: Int) : Screen()




}