package edu.ucne.registrotecnicos.data.repository

import android.util.Log
import edu.ucne.registrotecnicos.data.remote.RemoteDataSource
import edu.ucne.registrotecnicos.data.remote.Resource
import edu.ucne.registrotecnicos.data.remote.dto.MedicinasDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class MedicinaRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) {
    fun getMedicinas(): Flow<Resource<List<MedicinasDto>>> = flow {
        try {
            emit(Resource.Loading())
            val medicinas = remoteDataSource.getMedicinas()
            emit(Resource.Success(medicinas))
        } catch (e: HttpException) {
            Log.e("Retrofit No connection", "Error de conexión ${e.message}", e)
            emit(Resource.Error("Error de internet ${e.message}"))
        } catch (e: Exception) {
            Log.e("Retrofit Unknown", "Error desconocido ${e.message}", e)
            emit(Resource.Error("Unknown error ${e.message}"))
        }
    }

    suspend fun getMedicina(id: Int) = remoteDataSource.getMedicina(id)

    suspend fun createMedicina(medicina: MedicinasDto) = remoteDataSource.createMedicina(medicina)

    suspend fun updateMedicina(medicina: MedicinasDto) = remoteDataSource.updateMedicina(medicina.medicinaId, medicina)

    suspend fun deleteMedicina(id: Int) = remoteDataSource.deleteMedicina(id)
}

