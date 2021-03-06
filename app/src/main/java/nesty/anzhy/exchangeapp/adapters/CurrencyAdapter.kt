package nesty.anzhy.exchangeapp.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import nesty.anzhy.exchangeapp.R
import nesty.anzhy.exchangeapp.databinding.CurrencyItemLayoutBinding
import nesty.anzhy.exchangeapp.models.Currency
import nesty.anzhy.exchangeapp.utils.ExchangeRateDiffUtil
import java.math.BigDecimal
import java.math.RoundingMode

class CurrencyAdapter(
    val fragment: Fragment
) : RecyclerView.Adapter<CurrencyAdapter.VH>() {

    private var data = arrayListOf<Currency>()

    class VH(val binding: CurrencyItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: CurrencyItemLayoutBinding =
            CurrencyItemLayoutBinding.inflate(inflater, parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val data = data[position]
        holder.binding.txtLatestExchangeRate.text = BigDecimal(data.value).setScale(2, RoundingMode.UP).toString()
        holder.binding.txtCurrencyName.text = data.name

        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("CurrencyToShow", data.name)
            NavHostFragment.findNavController(fragment)
                .navigate(R.id.action_exchangeRateFragment_to_detailFragment, bundle)
        }
    }

    override fun getItemCount(): Int = data.size

    fun setData(newData: ArrayList<Currency>) {
        val exchangeDiffUtil = ExchangeRateDiffUtil(data, newData)
        val diffUtilResult = DiffUtil.calculateDiff(exchangeDiffUtil)
        this.data = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }
}


