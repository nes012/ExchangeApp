package nesty.anzhy.exchangeapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import nesty.anzhy.exchangeapp.databinding.CurrencyItemLayoutBinding
import nesty.anzhy.exchangeapp.models.Currency

class CurrencyAdapter : RecyclerView.Adapter<CurrencyAdapter.VH>() {

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
        holder.binding.txtLatestExchangeRate.text = data.value.toString()
        holder.binding.txtCurrencyName.text = data.name
    }

    override fun getItemCount(): Int = data.size

    fun setData(newData: ArrayList<Currency>) {
        this.data = newData
        notifyDataSetChanged()
    }
}


