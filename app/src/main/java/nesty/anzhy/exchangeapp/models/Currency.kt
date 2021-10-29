package nesty.anzhy.exchangeapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Currency(
    val name: String,
    val value:Double
) : Parcelable, Comparable<Currency> {
    override fun compareTo(other: Currency): Int {
        return this.name.compareTo(other.name)
    }
}