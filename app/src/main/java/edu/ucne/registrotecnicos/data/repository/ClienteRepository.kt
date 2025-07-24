package edu.ucne.registrotecnicos.data.repository

import android.util.Log
import edu.ucne.registrotecnicos.data.local.database.TecnicoDb
import edu.ucne.registrotecnicos.data.local.entity.ClienteEntity
import edu.ucne.registrotecnicos.data.local.entity.toDto
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
        try {
            if (cliente.clienteId != null && cliente.clienteId != 0) {
                remoteDataSource.updateCliente(cliente.clienteId!!, cliente.toDto())
            } else {
                val nuevo = remoteDataSource.createCliente(cliente.toDto())
                cliente.clienteId = nuevo.clienteId
            }
        } catch (e: Exception) {
            Log.e("ClienteRepository", "Error al sincronizar con API (save)", e)
        }

        db.clienteDao().save(cliente)
    }

    suspend fun delete(cliente: ClienteEntity) {
        try {
            cliente.clienteId?.let {
                remoteDataSource.deleteCliente(it)
            }
        } catch (e: Exception) {
            Log.e("ClienteRepository", "Error al sincronizar con API (delete)", e)
        }

        db.clienteDao().delete(cliente)
    }

    suspend fun find(id: Int): ClienteEntity? {
        return db.clienteDao().find(id)
    }

    fun getAll(): Flow<Resource<List<ClienteEntity>>> = flow {
        emit(Resource.Loading())

        try {
            // Emitir datos locales primero
            val localData = db.clienteDao().getAll().first()
            emit(Resource.Success(localData))

            // Obtener y guardar datos remotos
            val remoteClientes = remoteDataSource.getClientes()

            remoteClientes.forEach {
                db.clienteDao().save(
                    ClienteEntity(
                        clienteId = it.clienteId,
                        nombres = it.nombres,
                        whatsApp = it.whatsApp
                    )
                )
            }

            val updatedData = db.clienteDao().getAll().first()
            emit(Resource.Success(updatedData))
        } catch (e: Exception) {
            Log.e("ClienteRepository", "Error sincronizando datos", e)
            emit(Resource.Error(e.message ?: "Error desconocido"))
        }
    }
}
