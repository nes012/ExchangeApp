package nesty.anzhy.exchangeapp.data.database

import kotlinx.coroutines.flow.Flow
import nesty.anzhy.exchangeapp.data.database.entities.ExchangeEntity
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val exchangeDao: ExchangeDao
) {
    fun readExchangeRate(): Flow<List<ExchangeEntity>> {
        return exchangeDao.readExchangeRate()
    }

    suspend fun insertExchangeRate(exchangeEntity: ExchangeEntity) {
        exchangeDao.insertExchangeRate(exchangeEntity)
    }
}