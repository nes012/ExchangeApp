package nesty.anzhy.exchangeapp.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExchangeResponse(
	@field:SerializedName("data")
	val currency: HashMap<String, Double>? = null,

	@field:SerializedName("query")
	val query: Query? = null
) : Parcelable

