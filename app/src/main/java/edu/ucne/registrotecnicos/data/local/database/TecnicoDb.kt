package edu.ucne.registrotecnicos.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import edu.ucne.registrotecnicos.data.local.dao.MensajeDao
import edu.ucne.registrotecnicos.data.local.dao.TecnicoDao
import edu.ucne.registrotecnicos.data.local.dao.TicketDao
import edu.ucne.registrotecnicos.data.local.entity.MensajeEntity
import edu.ucne.registrotecnicos.data.local.entity.TecnicoEntity
import edu.ucne.registrotecnicos.data.local.entity.TicketEntity

@Database(
    entities = [
        TecnicoEntity::class,
        TicketEntity::class,
        MensajeEntity::class
    ],
    version = 13,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TecnicoDb : RoomDatabase() {
    abstract fun tecnicoDao(): TecnicoDao
    abstract fun ticketDao(): TicketDao
    abstract fun mensajeDao(): MensajeDao
}