package nesty.anzhy.exchangeapp.data.database.entities
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import nesty.anzhy.exchangeapp.utils.Constants

@Entity(tableName = Constants.EXCHANGE_TABLE)
class ExchangeEntity(
    @ColumnInfo(name = "exchange_rate")
    val currency: HashMap<String, Double>,
    @ColumnInfo(name = "base_currency")
    val baseCurrency: String,
    @ColumnInfo(name = "timestamp")
    var timestamp: Long
){
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}