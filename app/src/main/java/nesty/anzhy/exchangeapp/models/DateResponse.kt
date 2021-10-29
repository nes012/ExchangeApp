package nesty.anzhy.exchangeapp.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlin.collections.HashMap

@Parcelize
data class DateResponse(
    @field:SerializedName("query")
    val queryDate: QueryDate? = null,

    @field:SerializedName("data")
    val map: HashMap<String, HashMap<String, Double>>,

) : Parcelable


