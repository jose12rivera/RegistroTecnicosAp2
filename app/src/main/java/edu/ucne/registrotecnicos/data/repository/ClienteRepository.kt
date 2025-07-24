package edu.ucne.registrotecnicos.data.repository


import android.util.Log
import edu.ucne.registrotecnicos.data.remote.RemoteDataSource
import edu.ucne.registrotecnicos.data.remote.Resource
import edu.ucne.registrotecnicos.data.remote.dto.ClienteDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class ClienteRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) {
    fun getClientes(): Flow<Resource<List<ClienteDto>>> = flow {
        try {
            emit(Resource.Loading())
            val clientes = remoteDataSource.getClientes()
            emit(Resource.Success(clientes))
        } catch (e: HttpException) {
            Log.e("Retrofit Error", "Error de conexión: ${e.message}", e)
            emit(Resource.Error("Error de internet: ${e.message}"))
        } catch (e: Exception) {
            Log.e("Error desconocido", "Excepción: ${e.message}", e)
            emit(Resource.Error("Unknown error: ${e.message}"))
        }
    }

    suspend fun getCliente(id: Int) = remoteDataSource.getCliente(id)

    suspend fun createCliente(cliente: ClienteDto) = remoteDataSource.createCliente(cliente)

    suspend fun updateCliente(cliente: ClienteDto) = remoteDataSource.updateCliente(cliente.clienteId, cliente)

    suspend fun deleteCliente(id: Int) = remoteDataSource.deleteCliente(id)
}
