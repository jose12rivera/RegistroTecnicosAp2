package edu.ucne.composedemo.data.remote

import edu.ucne.registrotecnicos.data.remote.dto.ClienteDto
import retrofit2.http.*

interface ClienteApi {
    @GET("api/Clientes")
    suspend fun getClientes(): List<ClienteDto>

    @GET("api/Clientes/{id}")
    suspend fun getCliente(@Path("id") id: Int): ClienteDto

    @POST("api/Clientes")
    suspend fun createCliente(@Body clienteDto: ClienteDto): ClienteDto

    @PUT("api/Clientes/{id}")
    suspend fun updateCliente(@Path("id") id: Int, @Body clienteDto: ClienteDto): ClienteDto

    @DELETE("api/Clientes/{id}")
    suspend fun deleteCliente(@Path("id") id: Int)
}
