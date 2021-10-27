package nesty.anzhy.exchangeapp.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import nesty.anzhy.exchangeapp.data.database.entities.ExchangeEntity

@Dao
interface ExchangeDao {
    @Query("SELECT*FROM exchange_table")
    fun readExchangeRate(): Flow<List<ExchangeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExchangeRate(exchangeEntity: ExchangeEntity)
}