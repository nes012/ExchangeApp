package nesty.anzhy.exchangeapp.data.network

import nesty.anzhy.exchangeapp.models.ExchangeResponse
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val exchangeApi: ExchangeApi
) {

    suspend fun getExchangeRates(queries: Map<String, String>): Response<ExchangeResponse> {
        return exchangeApi.getExchangeRates(queries)
    }

}