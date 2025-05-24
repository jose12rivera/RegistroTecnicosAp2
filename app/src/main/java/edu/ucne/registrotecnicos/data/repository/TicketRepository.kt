package edu.ucne.registrotecnicos.data.repository

import edu.ucne.registrotecnicos.data.local.database.TecnicoDb
import edu.ucne.registrotecnicos.data.local.entity.TicketEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TicketRepository @Inject constructor(
    private val tecnicoDb: TecnicoDb
){
    suspend fun saveTicket(ticket: TicketEntity){
        tecnicoDb.ticketDao().save(ticket)
    }

    suspend fun find(id: Int): TicketEntity?{
        return tecnicoDb.ticketDao().find(id)
    }

    suspend fun delete(ticket: TicketEntity){
        return tecnicoDb.ticketDao().delete(ticket)
    }

    suspend fun getAll(): Flow<List<TicketEntity>>{
        return tecnicoDb.ticketDao().getAll()
    }
}