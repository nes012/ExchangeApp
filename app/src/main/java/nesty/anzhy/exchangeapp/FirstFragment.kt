package nesty.anzhy.exchangeapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import nesty.anzhy.exchangeapp.adapters.CurrencyAdapter
import nesty.anzhy.exchangeapp.data.database.entities.ExchangeEntity
import nesty.anzhy.exchangeapp.databinding.FragmentFirstBinding
import nesty.anzhy.exchangeapp.models.Currency
import nesty.anzhy.exchangeapp.utils.*
import nesty.anzhy.exchangeapp.utils.Constants.Companion.APIKEY
import nesty.anzhy.exchangeapp.utils.Constants.Companion.API_KEY
import nesty.anzhy.exchangeapp.viewmodel.MainViewModel
import java.util.*
import kotlin.collections.HashMap

@AndroidEntryPoint
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    private lateinit var networkListener: NetworkListener

    private lateinit var mainViewModel: MainViewModel
    private val mAdapter: CurrencyAdapter by lazy { CurrencyAdapter(this@FirstFragment) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        _binding = FragmentFirstBinding.inflate(inflater, container, false)

        lifecycleScope.launchWhenStarted {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext())
                .collect { status ->
                    Log.d("NetworkListener", status.toString())
                    mainViewModel.networkStatus = status
                    mainViewModel.showNetworkStatus()
                    readDatabase()
                }
        }

        binding.fabRefreshData.setOnClickListener {
            readDatabase()
        }

        setupRecyclerView()

        return binding.root
    }

    private fun requestApiData() {
        mainViewModel.getExchangeRate(applyQuery())
        mainViewModel.exchangeResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    response.data?.let {
                        val data = response.data
                        Log.e("ApiRequestSuccess", data.toString())
                        binding.txtLastUpdate.text = "Last update: "
                        binding.txtTime.text =
                            convertTimestampToTime(data.query?.timestamp?.toLong())
                        val map = it.currency!!
                        //set data to our adapter
                        setDataToAdapter(map)
                        val baseCurrency = data.query?.baseCurrency!!
                        val timestamp = data.query.timestamp?.toLong()!!
                        //set data to room library
                        mainViewModel.insertExchangeRate(
                            ExchangeEntity(
                                map,
                                baseCurrency,
                                timestamp
                            )
                        )
                    }
                }
                is NetworkResult.Error -> {
                    loadDataFromCache()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun setDataToAdapter(map: HashMap<String, Double>) {
        val list = arrayListOf<Currency>()
        map.forEach { (k, v) ->
            val currency = Currency(k, v)
            list.add(currency)
        }
        list.sort()
        mAdapter.setData(list)
    }

    private fun readDatabase() {
        mainViewModel.readExchangeRateFromDb.observeOnce(viewLifecycleOwner, { database ->
            if (database.isNotEmpty()) {
                binding.txtTime.text = convertTimestampToTime(database.last().timestamp)
                binding.txtBaseCurrency.text = "Base currency: " + database.last().baseCurrency
                Log.e("ExchangeRate", "readDatabase called!")
                val map = database.last().currency
                //set data to adapter
                setDataToAdapter(map)
                checkIfTenMinutesPastToRetrieveNewData(database.last().timestamp)
            } else {
                requestApiData()
            }
        })
    }

    //in this method we will check if 10 min past from the previous data.
    private fun checkIfTenMinutesPastToRetrieveNewData(timestamp: Long) {
        val minutesDiff = differenceInMinutesBetweenTwoDateTime(timestamp)
        Log.e("minutesDiff", "$minutesDiff min last from previous data")
        if (minutesDiff >= 30) {
            requestApiData()
        } else {
            Toast.makeText(
                requireContext(),
                "there are no new data to show",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun loadDataFromCache() {
        //we will use this method if there are some network error.
        lifecycleScope.launch {
            mainViewModel.readExchangeRateFromDb.observe(viewLifecycleOwner, { database ->
                if (database.isNotEmpty()) {
                    binding.txtTime.text = convertTimestampToTime(database.last().timestamp)
                    val map = database.last().currency
                    //set data to adapter
                    setDataToAdapter(map)
                }
            })
        }
    }

    private fun applyQuery(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[APIKEY] = API_KEY
        return queries
    }

    private fun setupRecyclerView() {
        binding.recyclerViewCurrency.adapter = mAdapter
        binding.recyclerViewCurrency.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}