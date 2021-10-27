package nesty.anzhy.exchangeapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import nesty.anzhy.exchangeapp.data.database.entities.ExchangeEntity

@Database
    (
    entities = [ExchangeEntity::class],
    version = 1,
    exportSchema = false
)

@TypeConverters(ExchangeTypeConverter::class)
abstract class ExchangeDatabase : RoomDatabase() {
    abstract fun exchangeDao(): ExchangeDao
}