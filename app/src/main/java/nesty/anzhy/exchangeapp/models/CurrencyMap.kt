package nesty.anzhy.exchangeapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CurrencyMap(
    val currencyMap: HashMap<String, Double>
):Parcelable