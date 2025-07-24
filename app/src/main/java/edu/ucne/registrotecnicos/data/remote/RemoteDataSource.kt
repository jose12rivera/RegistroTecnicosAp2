package edu.ucne.registrotecnicos.data.remote

import edu.ucne.composedemo.data.remote.ClienteApi
import edu.ucne.composedemo.data.remote.MedicinasApi
import edu.ucne.registrotecnicos.data.remote.dto.ClienteDto
import edu.ucne.registrotecnicos.data.remote.dto.MedicinasDto
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val medicinasApi: MedicinasApi,
    private val clienteApi: ClienteApi
) {
    suspend fun getMedicinas(): List<MedicinasDto> = medicinasApi.getMedicinas()

    suspend fun getMedicina(id: Int): MedicinasDto = medicinasApi.getMedicina(id)

    suspend fun createMedicina(medicina: MedicinasDto): MedicinasDto = medicinasApi.createMedicina(medicina)

    suspend fun updateMedicina(id: Int, medicina: MedicinasDto): MedicinasDto = medicinasApi.updateMedicina(id, medicina)

    suspend fun deleteMedicina(id: Int) = medicinasApi.deleteMedicina(id)

    suspend fun getClientes(): List<ClienteDto> = clienteApi.getClientes()

    suspend fun getCliente(id: Int): ClienteDto = clienteApi.getCliente(id)

    suspend fun createCliente(cliente: ClienteDto): ClienteDto = clienteApi.createCliente(cliente)

    suspend fun updateCliente(id: Int, cliente: ClienteDto): ClienteDto = clienteApi.updateCliente(id, cliente)

    suspend fun deleteCliente(id: Int) = clienteApi.deleteCliente(id)
}
