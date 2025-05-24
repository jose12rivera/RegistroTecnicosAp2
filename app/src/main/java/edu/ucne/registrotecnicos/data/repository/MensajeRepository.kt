package edu.ucne.registrotecnicos.data.repository

import edu.ucne.registrotecnicos.data.local.database.TecnicoDb
import edu.ucne.registrotecnicos.data.local.entity.MensajeEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MensajeRepository @Inject constructor(
    private val tecnicoDb: TecnicoDb
){
    suspend fun saveMensaje(mensaje: MensajeEntity){
        tecnicoDb.mensajeDao().save(mensaje)
    }

    suspend fun find(id: Int): MensajeEntity?{
        return tecnicoDb.mensajeDao().find(id)
    }

    suspend fun delete(mensaje: MensajeEntity){
        return tecnicoDb.mensajeDao().delete(mensaje)
    }

    suspend fun getAll(): Flow<List<MensajeEntity>>{
        return tecnicoDb.mensajeDao().getAll()
    }
}