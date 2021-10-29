package nesty.anzhy.exchangeapp

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import dagger.hilt.android.AndroidEntryPoint
import nesty.anzhy.exchangeapp.databinding.FragmentDetailBinding
import nesty.anzhy.exchangeapp.utils.*
import nesty.anzhy.exchangeapp.utils.Constants.Companion.BASE_CURRENCY
import nesty.anzhy.exchangeapp.utils.Constants.Companion.DATE_FROM
import nesty.anzhy.exchangeapp.utils.Constants.Companion.DATE_TO
import nesty.anzhy.exchangeapp.viewmodel.MainViewModel


@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var mainViewModel: MainViewModel
    private lateinit var currency: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        currency = arguments?.get("CurrencyToShow").toString()

        val dateTo = convertTimestampToDateQuery(System.currentTimeMillis())
        val dateFrom = returnDateSevenDayAgo()
        binding.ratesChart.setNoDataText("No exchange rate data is available for the selected currency.");

        ratesChartSetup()

        requestData(dateFrom, dateTo, "USD")

        return binding.root
    }

    private fun requestData(
        dateFrom: String,
        dateTo: String,
        baseCurrency: String
    ) {
        mainViewModel.getHistoricalRate(applyQuery(dateFrom, dateTo, baseCurrency))
        mainViewModel.exchangeHistoricalResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    response.data?.let {
                        showHistoryRatesChart(it.map)
                    }
                }
                is NetworkResult.Error -> {
                    Log.e("Error", response.message.toString())
                }
            }
        })
    }

    private fun applyQuery(
        dateFrom: String,
        dateTo: String,
        baseCurrency: String
    ): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[Constants.APIKEY] = Constants.API_KEY
        queries[DATE_FROM] = dateFrom
        queries[DATE_TO] = dateTo
        queries[BASE_CURRENCY] = baseCurrency
        return queries
    }

    private fun ratesChartSetup() {
        binding.ratesChart.isDragEnabled = true
        binding.ratesChart.setScaleEnabled(true)
        binding.ratesChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.ratesChart.axisLeft.textColor = Color.WHITE
        binding.ratesChart.axisRight.textColor = Color.WHITE
        binding.ratesChart.xAxis.textColor = Color.WHITE
        binding.ratesChart.xAxis.labelRotationAngle = -45f
        binding.ratesChart.xAxis.labelCount = 5
        binding.ratesChart.legend.textColor = ContextCompat.getColor(requireContext(), R.color.purple_200)
    }

    private fun showHistoryRatesChart(map: HashMap<String, HashMap<String, Double>>) {
        val historyRatesResponse = map.toSortedMap()
        val historyRates = historyRatesResponse.values

        val values = arrayListOf<Entry>()

        repeat(7) { i ->
            values.add(Entry(i.toFloat(), historyRates.elementAt(i)[currency]!!.toFloat()))
        }

        val historyRatesDates = arrayListOf<String>()

        repeat(7) { i ->
            historyRatesDates.add(convertDateToMonth(historyRatesResponse.keys.elementAt(i)))
        }

        val lineDataSet = LineDataSet(values,  "$currency Rates with base USD")
        lineDataSet.fillAlpha = 110
        lineDataSet.color = Color.RED
        lineDataSet.valueTextColor = ContextCompat.getColor(requireContext(), R.color.purple_200)
        lineDataSet.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
        lineDataSet.setDrawFilled(true)
        lineDataSet.fillColor = Color.GRAY

        val dataSet = arrayListOf<ILineDataSet>()
        dataSet.add(lineDataSet)

        val xAxis = binding.ratesChart.xAxis
        xAxis.valueFormatter = XAxisValueFormatter(historyRatesDates)

        val lineData = LineData(dataSet)
        lineData.setDrawValues(true)

        binding.ratesChart.data = lineData
        binding.ratesChart.description.isEnabled = false
        binding.ratesChart.axisRight.isEnabled = false
        binding.ratesChart.invalidate()
    }

    private class XAxisValueFormatter(private val values: List<String>) : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            return values.elementAt(value.toInt())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}