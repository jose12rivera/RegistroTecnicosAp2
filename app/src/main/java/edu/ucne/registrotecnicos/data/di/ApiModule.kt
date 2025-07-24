package edu.ucne.registrotecnicos.data.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import edu.ucne.composedemo.data.remote.MedicinasApi
import edu.ucne.registrotecnicos.data.di.MoshiMedicinas
import edu.ucne.registrotecnicos.data.di.OkHttpClientMedicinas
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    private const val BASE_URL = "http://registromedicina2025.somee.com/"

    @Provides
    @Singleton
    @MoshiMedicinas
    fun provideMoshiMedicinas(): Moshi =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .add(DateAdapter())
            .build()

    @Provides
    @Singleton
    @OkHttpClientMedicinas
    fun provideOkHttpClientMedicinas(): OkHttpClient {
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
    fun provideMedicinasApi(
        @MoshiMedicinas moshi: Moshi,
        @OkHttpClientMedicinas client: OkHttpClient
    ): MedicinasApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(MedicinasApi::class.java)
    }
}

