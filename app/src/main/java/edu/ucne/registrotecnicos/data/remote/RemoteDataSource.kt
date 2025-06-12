package edu.ucne.registrotecnicos.data.remote

import edu.ucne.composedemo.data.remote.MedicinasApi
import edu.ucne.registrotecnicos.data.remote.dto.MedicinasDto
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val medicinasApi: MedicinasApi
) {
    suspend fun getMedicinas(): List<MedicinasDto> = medicinasApi.getMedicinas()

    suspend fun getMedicina(id: Int): MedicinasDto = medicinasApi.getMedicina(id)

    suspend fun createMedicina(medicina: MedicinasDto): MedicinasDto = medicinasApi.createMedicina(medicina)

    suspend fun updateMedicina(id: Int, medicina: MedicinasDto): MedicinasDto = medicinasApi.updateMedicina(id, medicina)

    suspend fun deleteMedicina(id: Int) = medicinasApi.deleteMedicina(id)
}
