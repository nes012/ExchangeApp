package nesty.anzhy.exchangeapp.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class QueryDate(
    @field:SerializedName("base_currency")
    val baseCurrency: String? = null,
    @field:SerializedName("date_from")
    val date_from: String? = null,
    @field:SerializedName("date_to")
    val date_to: String? = null,
    @field:SerializedName("timestamp")
    val timestamp: Long? = null

) : Parcelable
