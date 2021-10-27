package nesty.anzhy.exchangeapp.data.network

import nesty.anzhy.exchangeapp.models.ExchangeResponse
import retrofit2.Response
import retrofit2.http.*

interface ExchangeApi {
    @GET("latest")
    suspend fun getExchangeRates(
        @QueryMap queries: Map<String, String>
    ): Response<ExchangeResponse>
}