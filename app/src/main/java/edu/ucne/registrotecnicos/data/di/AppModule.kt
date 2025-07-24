package edu.ucne.registrotecnicos.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.ucne.registrotecnicos.data.local.database.TecnicoDb
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTecnicoDb(@ApplicationContext applicationContext: Context): TecnicoDb =
        Room.databaseBuilder(
            applicationContext,
            TecnicoDb::class.java,
            "Tecnico.db"
        ).fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideTecnicoDao(appDataDb: TecnicoDb) = appDataDb.tecnicoDao()

    @Provides
    @Singleton
    fun provideTicketDao(appDataDb: TecnicoDb) = appDataDb.ticketDao()

    @Provides
    @Singleton
    fun provideMensajeDao(appDataDb: TecnicoDb) = appDataDb.mensajeDao()

    @Provides
    @Singleton
    fun provideClienteDao(appDataDb: TecnicoDb) = appDataDb.clienteDao() // Agregado
}
