package edu.ucne.registrotecnicos.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import edu.ucne.registrotecnicos.data.local.dao.ClienteDao
import edu.ucne.registrotecnicos.data.local.dao.MensajeDao
import edu.ucne.registrotecnicos.data.local.dao.TecnicoDao
import edu.ucne.registrotecnicos.data.local.dao.TicketDao
import edu.ucne.registrotecnicos.data.local.entity.ClienteEntity
import edu.ucne.registrotecnicos.data.local.entity.MensajeEntity
import edu.ucne.registrotecnicos.data.local.entity.TecnicoEntity
import edu.ucne.registrotecnicos.data.local.entity.TicketEntity

@Database(
    entities = [
        TecnicoEntity::class,
        TicketEntity::class,
        MensajeEntity::class,
        ClienteEntity::class // Agregado
    ],
    version = 14,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TecnicoDb : RoomDatabase() {
    abstract fun tecnicoDao(): TecnicoDao
    abstract fun ticketDao(): TicketDao
    abstract fun mensajeDao(): MensajeDao
    abstract fun clienteDao(): ClienteDao // Agregado
}