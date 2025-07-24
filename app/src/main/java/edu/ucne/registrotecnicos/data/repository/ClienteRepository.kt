package edu.ucne.registrotecnicos.data.repository

import android.util.Log
import edu.ucne.registrotecnicos.data.local.database.TecnicoDb
import edu.ucne.registrotecnicos.data.local.entity.ClienteEntity
import edu.ucne.registrotecnicos.data.remote.RemoteDataSource
import edu.ucne.registrotecnicos.data.remote.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ClienteRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val db: TecnicoDb
) {

    suspend fun save(cliente: ClienteEntity) {
        db.clienteDao().save(cliente)
    }

    suspend fun find(id: Int): ClienteEntity? {
        return db.clienteDao().find(id)
    }

    suspend fun delete(cliente: ClienteEntity) {
        db.clienteDao().delete(cliente)
    }

    fun getAll(): Flow<Resource<List<ClienteEntity>>> = flow {
        emit(Resource.Loading())

        try {
            // Emitir datos locales primero (offline-first)
            val localData = db.clienteDao().getAll().first()
            emit(Resource.Success(localData))

            // Obtener datos remotos
            val remoteClientes = remoteDataSource.getClientes()

            // Guardar/actualizar en local
            remoteClientes.forEach {
                db.clienteDao().save(
                    ClienteEntity(
                        clienteId = it.clienteId,
                        nombres = it.nombres,
                        whatsApp = it.whatsApp
                    )
                )
            }

            // Emitir datos actualizados desde local
            val updatedData = db.clienteDao().getAll().first()
            emit(Resource.Success(updatedData))

        } catch (e: Exception) {
            Log.e("ClienteRepository", "Error sincronizando datos", e)
            emit(Resource.Error(e.message ?: "Error desconocido"))
        }
    }
}
