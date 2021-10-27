package nesty.anzhy.exchangeapp.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import nesty.anzhy.exchangeapp.models.Query
import kotlin.collections.HashMap

class ExchangeTypeConverter {
    var gson = Gson()

    @TypeConverter
    fun queryToString(query: Query): String {
        return gson.toJson(query)
    }

    @TypeConverter
    fun stringToQuery(data: String): Query {
        val listType = object : TypeToken<Query>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun stringToMap(value: String): HashMap<String, Double> {
        return gson.fromJson(value, object : TypeToken<HashMap<String, Double>>() {}.type)
    }

    @TypeConverter
    fun mapToString(value: HashMap<String, Double>?): String {
        return if (value == null) "" else gson.toJson(value)
    }
}