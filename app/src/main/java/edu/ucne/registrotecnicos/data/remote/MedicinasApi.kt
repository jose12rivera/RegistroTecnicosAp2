package edu.ucne.composedemo.data.remote


import edu.ucne.registrotecnicos.data.remote.dto.MedicinasDto
import retrofit2.http.*

interface MedicinasApi {
    @GET("api/Medicinas")
    suspend fun getMedicinas(): List<MedicinasDto>

    @GET("api/Medicinas/{id}")
    suspend fun getMedicina(@Path("id") id: Int): MedicinasDto

    @POST("api/Medicinas")
    suspend fun createMedicina(@Body medicinaDto: MedicinasDto): MedicinasDto

    @PUT("api/Medicinas/{id}")
    suspend fun updateMedicina(@Path("id") id: Int, @Body medicinaDto: MedicinasDto): MedicinasDto

    @DELETE("api/Medicinas/{id}")
    suspend fun deleteMedicina(@Path("id") id: Int)
}
