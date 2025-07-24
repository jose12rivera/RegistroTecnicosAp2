package edu.ucne.composedemo.di


import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import edu.ucne.composedemo.data.remote.ClienteApi
import edu.ucne.registrotecnicos.data.di.MoshiCliente
import edu.ucne.registrotecnicos.data.di.OkHttpClientCliente
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ClienteApiModule {

    private const val BASE_URL = "https://ap2cliente-cadvhsa8bfdgcmb7.eastus2-01.azurewebsites.net/"

    @Provides
    @Singleton
    @MoshiCliente
    fun provideMoshiCliente(): Moshi =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

    @Provides
    @Singleton
    @OkHttpClientCliente
    fun provideOkHttpClientCliente(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideClienteApi(
        @MoshiCliente moshi: Moshi,
        @OkHttpClientCliente client: OkHttpClient
    ): ClienteApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(ClienteApi::class.java)
    }
}
