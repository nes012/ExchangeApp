package nesty.anzhy.exchangeapp.viewmodel
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import nesty.anzhy.exchangeapp.data.Repository
import nesty.anzhy.exchangeapp.data.database.entities.ExchangeEntity
import nesty.anzhy.exchangeapp.models.DateResponse
import nesty.anzhy.exchangeapp.models.ExchangeResponse
import nesty.anzhy.exchangeapp.models.Query
import nesty.anzhy.exchangeapp.utils.NetworkResult
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {
    var networkStatus = false
    var backOnline = false

    /**RETROFIT */
    val exchangeResponse: MutableLiveData<NetworkResult<ExchangeResponse>> = MutableLiveData()
    fun getExchangeRate(queries: Map<String, String>) = viewModelScope.launch {
        getExchangeSafeCall(queries)
    }

    private suspend fun getExchangeSafeCall(queries: Map<String, String>) {
        exchangeResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val data = repository.remote.getExchangeRates(queries)
                exchangeResponse.value = handleExchangeRateResponse(data)

                val exchangeResponse = exchangeResponse.value!!.data
                if (exchangeResponse != null) {
                    val currency: HashMap<String, Double> = exchangeResponse.currency!!
                    val query: Query = exchangeResponse.query!!
                    offlineCacheExchangeRate(currency, query)
                }
            } catch (e: Exception) {
                exchangeResponse.value = NetworkResult.Error("Currency not found.")
            }
        } else {
            exchangeResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }

    val exchangeHistoricalResponse: MutableLiveData<NetworkResult<DateResponse>> = MutableLiveData()
    fun getHistoricalRate(queries: Map<String, String>) = viewModelScope.launch {
        getExchangeHistoricalSafeCall(queries)
    }
    private suspend fun getExchangeHistoricalSafeCall(queries: Map<String, String>) {
        exchangeHistoricalResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val data = repository.remote.getHistoricalRates(queries)
                exchangeHistoricalResponse.value = handleExchangeHistoricalRateResponse(data)
                // val exchangeHistoricalResponse = exchangeHistoricalResponse.value!!.data
            } catch (e: Exception) {
                exchangeHistoricalResponse.value = NetworkResult.Error("History not found.")
            }
        } else {
            exchangeHistoricalResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }

    private fun handleExchangeHistoricalRateResponse(response: Response<DateResponse>): NetworkResult<DateResponse> {
        return when {
            response.isSuccessful -> {
                val data = response.body()
                NetworkResult.Success(data!!)
            }
            else -> {
                NetworkResult.Error(response.message())
            }
        }
    }

    private fun handleExchangeRateResponse(response: Response<ExchangeResponse>): NetworkResult<ExchangeResponse>? {
        return when {
            response.isSuccessful -> {
                val data = response.body()
                NetworkResult.Success(data!!)
            }
            else -> {
                NetworkResult.Error(response.message())
            }
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

    fun showNetworkStatus() {
        if (!networkStatus) {
            Toast.makeText(getApplication(), "No Internet Connection.", Toast.LENGTH_SHORT).show()
        } else if (networkStatus) {
            if (backOnline) {
                Toast.makeText(getApplication(), "We're back online.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun offlineCacheExchangeRate(
        currency: HashMap<String, Double>, query: Query
    ) {
        val exchangeEntity =
            ExchangeEntity(currency, query.baseCurrency!!, query.timestamp!!.toLong())
        insertExchangeRate(exchangeEntity)
    }

    /**ROOM DB */
    val readExchangeRateFromDb: LiveData<List<ExchangeEntity>> =
        repository.local.readExchangeRate().asLiveData()

    fun insertExchangeRate(exchangeEntity: ExchangeEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertExchangeRate(exchangeEntity)
        }
}



