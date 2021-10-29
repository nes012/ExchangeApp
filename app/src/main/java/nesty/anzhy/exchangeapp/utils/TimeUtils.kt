package nesty.anzhy.exchangeapp.utils

import org.joda.time.DateTime
import org.joda.time.Minutes
import org.joda.time.format.DateTimeFormat
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

fun convertTimestampToDateQuery(timestamp: Long?): String {
    if (timestamp == null) return ""
    val stamp = Timestamp(timestamp)
    val date = Date(stamp.time)
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    sdf.timeZone = TimeZone.getDefault()
    return sdf.format(date)
}

fun returnDateSevenDayAgo(): String {
    val now = DateTime.now()
    val weekAgo = now.minusWeeks(1)
    val weekAgoStart = weekAgo.withTimeAtStartOfDay().plusDays(1)
    val format = DateTimeFormat.forPattern("yyyy-MM-dd")
    return format.print(weekAgoStart)
}


fun convertDateToMonth(dateString: String): String {
    val date = DateTime(dateString)
    val format = DateTimeFormat.forPattern("dd MMM")
    return format.print(date)
}

fun differenceInMinutesBetweenTwoDateTime(timestamp: Long): Int {
    val start = DateTime(timestamp * 1000);
    val end = DateTime(System.currentTimeMillis())
    return Minutes.minutesBetween(start, end).minutes
}