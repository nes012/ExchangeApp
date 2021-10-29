package nesty.anzhy.exchangeapp.utils

class Constants {
    companion object {
        const val API_KEY = "29632140-35b0-11ec-959c-1b0d60c65405"
        const val BASE_URL = "https://freecurrencyapi.net/api/v2/"

        //Room db
        const val EXCHANGE_DB = "exchange_database"
        const val EXCHANGE_TABLE = "exchange_table"

        //API query keys
        const val APIKEY = "apikey"
        const val DATE_FROM = "date_from"
        const val DATE_TO = "date_to"
        const val BASE_CURRENCY = "base_currency"

        //refresh data
        const val REFRESH_INTERVAL_MINUTES = 10
    }
}