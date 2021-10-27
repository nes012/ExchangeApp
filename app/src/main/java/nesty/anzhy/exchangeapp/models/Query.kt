package nesty.anzhy.exchangeapp.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Query(

    @field:SerializedName("base_currency")
    val baseCurrency: String? = null,

    @field:SerializedName("timestamp")
    val timestamp: Int? = null
) : Parcelable

