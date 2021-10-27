package nesty.anzhy.exchangeapp.utils

import org.joda.time.DateTime
import org.joda.time.Minutes
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

fun convertTimestampToTime(timestamp: Long?): String {
    if (timestamp == null) return ""
    val stamp = Timestamp(timestamp * 1000)
    val date = Date(stamp.time)
    val pattern = "dd-MMM-yyyy, HH:mm:ss"
    val sdf = SimpleDateFormat(pattern, Locale.getDefault())
    sdf.timeZone = TimeZone.getDefault()
    return sdf.format(date)
}

fun differenceInDaysBetweenTwoDateTime(timestamp: Long): Int {
    val start: DateTime = DateTime(timestamp * 1000);
    val end: DateTime = DateTime(System.currentTimeMillis())
    return Minutes.minutesBetween(start, end).minutes
}