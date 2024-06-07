package com.application.intercom.helper

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.application.intercom.manager.dialog.ComplaintDialog.Companion.TAG
import java.text.DateFormat.getDateInstance
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*
import java.util.concurrent.TimeUnit


fun setFormatDate(originalDate: String?): String? {
    if (originalDate != "null") {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val outputFormat = SimpleDateFormat("dd-MM-yyyy")
        val date = inputFormat.parse(originalDate)
        val formattedDate = outputFormat.format(date)
        return formattedDate
    }
    return ""

}
fun setNewFormatDate(originalDate: String?): String? {
    if (originalDate != "null") {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val outputFormat = SimpleDateFormat("yyyy-MMM-dd")
        val date = inputFormat.parse(originalDate)
        val formattedDate = outputFormat.format(date)
        return formattedDate
    }


    return ""

}
fun setNewFormat(originalDate: Date?): String? {
    if (originalDate != null) {
        val outputFormat = SimpleDateFormat("yyyy/MM/dd")
        val formattedDate = outputFormat.format(Date(originalDate.time))
        return formattedDate
    }

    return ""

}

fun isYesterdaysDate(dateString: String): Boolean {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val parsedDate = dateFormat.parse(dateString)
    val currentDate = Date()

    val parsedCalendar = Calendar.getInstance()
    parsedCalendar.time = parsedDate

    val currentCalendar = Calendar.getInstance()

    val isSameYear =
        parsedCalendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR)
    val isSameMonth =
        parsedCalendar.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH)
    val isYesterday =
        parsedCalendar.get(Calendar.DAY_OF_MONTH) == currentCalendar.get(Calendar.DAY_OF_MONTH) - 1
    Log.d("showDate", "isSameYear: $isSameYear")
    Log.d("showDate", "isSameMonth: $isSameMonth")
    Log.d("showDate", "isYesterday: $isYesterday")

    return isSameYear && isSameMonth && isYesterday

}

fun isTodayDate(dateString: String): Boolean {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val parsedDate = dateFormat.parse(dateString)
    val currentDate = Date()

    val parsedCalendar = Calendar.getInstance()
    parsedCalendar.time = parsedDate

    val currentCalendar = Calendar.getInstance()
    val isSameYear =
        parsedCalendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR)
    val isSameMonth =
        parsedCalendar.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH)
    val isSameDay =
        parsedCalendar.get(Calendar.DAY_OF_MONTH) == currentCalendar.get(Calendar.DAY_OF_MONTH)
    Log.d("showDate", "isSameYear: $isSameYear")
    Log.d("showDate", "isSameMonth: $isSameMonth")
    Log.d("showDate", "isSameDay: $isSameDay")

    return isSameYear && isSameMonth && isSameDay

}

fun dateformatmmddyyyyhhmm(time: String?): String? {
    val inputPattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    val outputPattern = "MM/dd/yyyy"
    val inputFormat = SimpleDateFormat(inputPattern)
    val outputFormat = SimpleDateFormat(outputPattern)
    var date: Date? = null
    var str: String? = null
    try {
        date = inputFormat.parse(time)
        str = outputFormat.format(date)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return str
}


fun setnewFormatDate(originalDate: String?): String? {

    val inputFormat = SimpleDateFormat("yyyy-MM-dd")
    val outputFormat = SimpleDateFormat("dd/MM/yyyy")
    val date = inputFormat.parse(originalDate)
    val formattedDate = outputFormat.format(date)

    return formattedDate
}


fun setFormatDateByStatics(originalDate: String?): String? {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd")
    val outputFormat = SimpleDateFormat("dd-MMM-yyyy")
    val date = inputFormat.parse(originalDate)
    val formattedDate = outputFormat.format(date)

    return formattedDate
}

fun parseDateyyyymmddFormat(time: String?): String? {
    val inputPattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    val outputPattern = "MM-dd-yyyy"
    val inputFormat = SimpleDateFormat(inputPattern)
    val outputFormat = SimpleDateFormat(outputPattern)
    var date: Date? = null
    var str: String? = null
    try {
        date = inputFormat.parse(time)
        str = outputFormat.format(date)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return str
}

fun formatDate(originalDate: String?): String? {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val outputFormat = SimpleDateFormat("yyyy-MM-dd")
    val date = inputFormat.parse(originalDate)
    val formattedDate = outputFormat.format(date)

    return formattedDate
}

fun getTimeDifferenceFromCurrentTime(dateString: String): Long {

    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val currentTimeString = dateFormat.format(Date(System.currentTimeMillis()))
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val currentDate = inputFormat.parse(currentTimeString)

    val testFormat = SimpleDateFormat("HH:mm:ss")
    Log.d("startCountDownTimer", "time: ${testFormat.format(currentDate)}")
    val date = inputFormat.parse(dateString)
    date.time += (5*60*60*1000) + 30*60*1000
    Log.d("startCountDownTimer", "time: ${testFormat.format(date)}")
    val newDate = date?.time
    if (date != null && currentDate != null) {
        val difference = (currentDate.time) - (newDate?:0)
        Log.d("startCountDownTimer", "difference: ${difference}")
        return kotlin.math.abs(difference)
    }
    return 10000
}

/*fun getTimeDifferenceFromCurrentTime(dateString: String): Long {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",Locale.US)
    dateFormat.timeZone = TimeZone.getTimeZone("UTC")
    val currentTimeString = dateFormat.format(Date(System.currentTimeMillis()))
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
    inputFormat.timeZone = TimeZone.getTimeZone("UTC")
    val currentDate = inputFormat.parse(currentTimeString)
    val date = inputFormat.parse(dateString)
    if (date != null && currentDate != null) {
        val difference = (currentDate.time / 1000) - (date.time / 1000)
        Log.d("startCountDownTimer", "difference: ${difference}")
        return kotlin.math.abs(difference).toLong()
    }
    return 10000
}*/

fun formatDate1(originalDate: String?): String? {

    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val outputFormat = SimpleDateFormat("dd/MM/yyyy")
    val date = inputFormat.parse(originalDate)
    date.time
    val formattedDate = outputFormat.format(date)

    return formattedDate
}


@RequiresApi(Build.VERSION_CODES.O)
fun currentTime(): String {
    val current = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
    val formatterForTime = DateTimeFormatter.ofPattern("hh:mm a")
    val formatted = current.format(formatterForTime)
    println("Current Date and Time is: $formatted")
    return formatted
}


fun setFormatTime(time: String?): String {
    val input = SimpleDateFormat("HH:mm")
    val output = SimpleDateFormat("hh:mm a")
    val originalTime: Date = input.parse(time)
    val parsetime = output.format(originalTime)

    return parsetime
}


fun convert24To12hrs(time: String?): String? {
    var convertedTime = ""
    try {
        val displayFormat = SimpleDateFormat("hh:mm a")
        val parseFormat = SimpleDateFormat("HH:mm")
        val date: Date = parseFormat.parse(time)
        convertedTime = displayFormat.format(date)
        println("convertedTime : $convertedTime")
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return convertedTime
    //output 10:23PM
}

fun parseTimeFormat(time: String?): String? {
    val inputPattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    "2022-05-06T 09:25:59.629Z"
    val outputPattern = "HH:mm"
    val inputFormat = SimpleDateFormat(inputPattern)
    val outputFormat = SimpleDateFormat(outputPattern)
    var date: Date? = null
    var str: String? = null
    try {
        date = inputFormat.parse(time)
        str = outputFormat.format(date)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return str
}

fun getCurrentDate(): String {
    val date = Calendar.getInstance().time
    val Dateformat = SimpleDateFormat("yyyy-MM-dd")
    val currentDate: String = Dateformat.format(date)

    return currentDate
}
fun getCurrentDateNew(): String {
    val date = Calendar.getInstance().time
    val Dateformat = SimpleDateFormat("dd-MM-yyyy")
    val currentDate: String = Dateformat.format(date)

    return currentDate
}
fun getCurrentDateNew2(): String {
    val date = Calendar.getInstance().time
    val Dateformat = SimpleDateFormat("dd-MMM-yyyy")
    val currentDate: String = Dateformat.format(date)

    return currentDate
}

fun getNewCurrentDate(): String {
    val date = Calendar.getInstance().time
    val Dateformat = SimpleDateFormat("yyyy-MM-dd")
    val currentDate: String = Dateformat.format(date)

    return currentDate
}

fun getNewFormateCurrentDate(): String {
    val date = Calendar.getInstance().time
    val Dateformat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val currentDate: String = Dateformat.format(date)
    return currentDate
}


fun getCurrentMonth(): String {
    val date = Calendar.getInstance().time
    val Dateformat = SimpleDateFormat("MMMM")
    val currentMonth: String = Dateformat.format(date)

    return currentMonth

}
fun getCurrentYear(): String {
    val date = Calendar.getInstance().time
    val Dateformat = SimpleDateFormat("YYYY")
    val currentMonth: String = Dateformat.format(date)

    return currentMonth

}
fun getCurrentMonthNew(): String {
    val date = Calendar.getInstance().time
    val Dateformat = SimpleDateFormat("MM")
    val currentMonth: String = Dateformat.format(date)

    return currentMonth

}

fun timeWithCurrentTime(time: String?): String {
    val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val output = SimpleDateFormat("hh:mm a")
    val originalTime: Date = input.parse(time)
    val parsetime = output.format(originalTime)

    return parsetime
}

fun localToGMT(): String {
    val date = Date()
    val sdf = getDateInstance()
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    val outputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val gmtDate = getDateInstance().parse(sdf.format(date))
    return outputFormat.format(gmtDate)
}

fun gmtToLocalDate(date: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val outputFormat = SimpleDateFormat("hh:mm a")
    val date = inputFormat.parse(date)
    val timeZone = Calendar.getInstance().timeZone.id
    val local = Date(date.time + TimeZone.getTimeZone(timeZone).getOffset(date.time))
    return outputFormat.format(local)
}

fun getLocalTime(dateString: String): String {
    val inputFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    val date = inputFormatter.parse(dateString)
    date.time += ((5 * 60 * 60) + 1800) * 1000
    val outputFormatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return outputFormatter.format(date)
}

fun getMonthOfDate(date: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val outputFormat = SimpleDateFormat("MMM")
    val date = inputFormat.parse(date)
    val formatMonth = outputFormat.format(date)
    return formatMonth
}
fun getYearMonthOfDate(date: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val outputFormat = SimpleDateFormat("MMMM - yyyy")
    val date = inputFormat.parse(date)
    val formatMonth = outputFormat.format(date)
    return formatMonth
}
fun getMonthOfDateNewFormat(date: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val outputFormat = SimpleDateFormat("MMMM")
    val date = inputFormat.parse(date)
    val formatMonth = outputFormat.format(date)
    return formatMonth
}
fun getYearofDateNew(date: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val outputFormat = SimpleDateFormat("yyyy")
    val date = inputFormat.parse(date)
    val formatMonth = outputFormat.format(date)
    return formatMonth
}

fun getMonthOfDateNew(date: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd")
    val outputFormat = SimpleDateFormat("MMMM - yyyy")
    val date = inputFormat.parse(date)
    val formatMonth = outputFormat.format(date)

    return formatMonth
}
fun getMonthOfSelectDate(date: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd")
    val outputFormat = SimpleDateFormat("MMMM")
    val date = inputFormat.parse(date)
    val formatMonth = outputFormat.format(date)

    return formatMonth
}
fun getMonthOfSelectDatenew(date: String): String {
    val inputFormat = SimpleDateFormat("yyyy/MM/dd")
    val outputFormat = SimpleDateFormat("MMMM")
    val date = inputFormat.parse(date)
    val formatMonth = outputFormat.format(date)

    return formatMonth
}

fun getMonthOfSelectYear(date: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd")
    val outputFormat = SimpleDateFormat("yyyy")
    val date = inputFormat.parse(date)
    val formatMonth = outputFormat.format(date)

    return formatMonth
}

fun getMonthOfSelectYear1(date: String): String {
    val inputFormat = SimpleDateFormat("yyyy/MM/dd")
    val outputFormat = SimpleDateFormat("yyyy")
    val date = inputFormat.parse(date)
    val formatMonth = outputFormat.format(date)

    return formatMonth
}


fun getCurrentDateAnotherFormate(): String {
    val date = Calendar.getInstance().time
    val Dateformat = SimpleDateFormat("dd-MMM-yyyy")
    val currentDate: String = Dateformat.format(date)

    return currentDate
}

fun convertLongToDate(time: Long): String {
    val date = Date(time)
    val formate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())//2022-04-27
    return formate.format(date)
}


fun convertDateTomonth(date1: Long): String? {
    val date = Date(date1)
    val month = SimpleDateFormat("MM", Locale.getDefault())
    return month.format(date)
}


fun showDateAndTime(context: Context) {
    val c = Calendar.getInstance()
    val dialog = DatePickerDialog(
        context,
        { view, year, month, dayOfMonth ->
            val _year = year.toString()
            val _month = if (month + 1 < 10) "0" + (month + 1) else (month + 1).toString()
            val _date = if (dayOfMonth < 10) "0$dayOfMonth" else dayOfMonth.toString()
            val pickedDate = "$year-$_month-$_date"

            Log.e("PickedDate: ", "Date: $pickedDate") //2022-04-27

            println(">$pickedDate")

        }, c[Calendar.YEAR], c[Calendar.MONTH], c[Calendar.MONTH]
    )
    dialog.datePicker.minDate = System.currentTimeMillis() - 1000
    dialog.show()
}

fun convertDateyyyymmdd(date: String): String {

    var localTime: String? = ""
    val myFormat = "yyyy-MM-dd"
    val sdf = SimpleDateFormat(myFormat, Locale.US)
    sdf.timeZone = TimeZone.getTimeZone("GMT")

    try {
        val date = sdf.parse(date)
        localTime = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(date)
    } catch (e: Exception) {
        // Log.e(com.streettak.util.CommonUtil.TAG, "serverTimeCalculation: " + e.message, e)
    }

    return localTime!!


}

fun getAge(date: String?): Int {
    var age = 0
    try {
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val date1 = formatter.parse(date)
        val now = Calendar.getInstance()
        val dob = Calendar.getInstance()
        dob.time = date1
        require(!dob.after(now)) { "Can't be born in the future" }
        val year1 = now[Calendar.YEAR]
        val year2 = dob[Calendar.YEAR]
        age = year1 - year2
        val month1 = now[Calendar.MONTH]
        val month2 = dob[Calendar.MONTH]
        if (month2 > month1) {
            age--
        } else if (month1 == month2) {
            val day1 = now[Calendar.DAY_OF_MONTH]
            val day2 = dob[Calendar.DAY_OF_MONTH]
            if (day2 > day1) {
                age--
            }
        }
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    Log.d("TAG", "getAge: AGE=> $age")
    return age
}

fun isStartTimeGreaterThanCurrentTime(startTime: Date): Boolean {
    val currentTime = Date()
    return startTime.after(currentTime)
}

@SuppressLint("SimpleDateFormat")
fun differenceOfTime(startTime: String, endTime: String): Long {
    val input = SimpleDateFormat("h:mm")
    val startDate = input.parse(startTime)
    val endDate = input.parse(endTime)
    val differ = (endDate?.time ?: 0) - (startDate?.time ?: 0)
    return differ
}
/*@RequiresApi(Build.VERSION_CODES.O)
fun calculateTimeDifference(startTimeStr: String, endTimeStr: String): Long {
    val startTime = LocalTime.parse(startTimeStr)
    val endTime = LocalTime.parse(endTimeStr)

    // Calculate the duration between the two times
    val duration = Duration.between(startTime, endTime)

    // Return the duration in minutes
    return duration.toMinutes()
}*/
@RequiresApi(Build.VERSION_CODES.O)
fun calculateTimeDifference(startTimeStr: String, endTimeStr: String): Long {
    // Define the pattern for the input strings
    val formatter = DateTimeFormatter.ofPattern("h:mm a")

    // Parse the start and end times using the formatter
    val startTime = LocalTime.parse(startTimeStr, formatter)
    val endTime = LocalTime.parse(endTimeStr, formatter)

    // Calculate the duration between the two times
    val duration = Duration.between(startTime, endTime)

    // Return the duration in minutes
    return duration.toMinutes()
}
fun getParseBetweenTimeStatus(firstData: String, secondDate: String): Boolean {
    val currentDateStr = getCurrentTime()
    val date1 = parseTime(firstData)
    val date2 = parseTime(secondDate)
    val current = parseTime(currentDateStr)

    if (date1 != null && date2 != null && current != null) {
        return current.after(date1) && current.before(date2) || current == date1 || current == date2
    }
    return false
}

fun parseTime(time: String): Date? {
    val dateFormatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return dateFormatter.parse(time)
}

fun getCurrentTime(): String {
    val dateFormatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return dateFormatter.format(Calendar.getInstance().time)
}

@RequiresApi(Build.VERSION_CODES.O)
fun isStartTimeGreaterThanCurrentTime(startTime: LocalDateTime): Boolean {
    val currentTime = LocalDateTime.now()
    return startTime.isAfter(currentTime)
}

@RequiresApi(Build.VERSION_CODES.O)
fun convertStringToTime(stringTime: String): LocalTime? {
    // Define the format of the input string time (e.g., "HH:mm:ss").
    val formatter = DateTimeFormatter.ofPattern("HH:mm")

    return try {
        // Parse the string into a LocalTime object using the defined formatter.
        val time = LocalTime.parse(stringTime, formatter)
        time
    } catch (e: DateTimeParseException) {
        // Handle parsing errors, if any.
        null
    }

}

/*fun calculateOnyTime(date: String?): String {
    var date1: String? = ""
    val myFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    val sdf = SimpleDateFormat(myFormat, Locale.US)
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    try {
        val date: Date = sdf.parse(date)
         date1 = SimpleDateFormat("hh:mm a").format(date)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return date1!!
}*/
fun serverTimeCalculation(serverTime: String?): String? {
    var localTime: String? = ""
    val myFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    val sdf = SimpleDateFormat(myFormat, Locale.US)
    sdf.timeZone = TimeZone.getTimeZone("GMT")
    try {
        val date = sdf.parse(serverTime)
        localTime = SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(date)
    } catch (e: java.lang.Exception) {
        Log.e(
            TAG,
            "serverTimeCalculation: " + e.message,
            e
        )
    }
    return localTime
}

fun parseDateToddMMyyyy(time: String?): String? {
    val inputPattern = "yyyy-MM-dd HH:mm:ss"
    val outputPattern = "h:mm a"
    val inputFormat = SimpleDateFormat(inputPattern)
    val outputFormat = SimpleDateFormat(outputPattern)
    var date: Date? = null
    var str: String? = null
    try {
        date = inputFormat.parse(time)
        str = outputFormat.format(date)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return str
}

fun timeSince(dateStr: String, numericDates: Boolean = false): String {
    val iso8601Format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    iso8601Format.timeZone = TimeZone.getTimeZone("UTC")
    val date = iso8601Format.parse(dateStr) ?: return ""

    val now = Calendar.getInstance().time
    val diff = now.time - date.time

    val years = TimeUnit.MILLISECONDS.toDays(diff) / 365
    val months = TimeUnit.MILLISECONDS.toDays(diff) / 30
    val weeks = TimeUnit.MILLISECONDS.toDays(diff) / 7
    val days = TimeUnit.MILLISECONDS.toDays(diff)
    val hours = TimeUnit.MILLISECONDS.toHours(diff)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(diff)

    return when {
        years >= 2 -> "$years yrs ago"
        years >= 1 -> if (numericDates) "1 yr ago" else "1 yr ago"
        months >= 2 -> "$months m ago"
        months >= 1 -> if (numericDates) "1 m ago" else "1 m ago"
        weeks >= 2 -> "$weeks w ago"
        weeks >= 1 -> if (numericDates) "1 w ago" else "1 w ago"
        days >= 2 -> "$days d ago"
        days >= 1 -> if (numericDates) "1 d ago" else "1 d ago"
        hours >= 2 -> "$hours hrs ago"
        hours >= 1 -> if (numericDates) "1 hr ago" else "1 hr ago"
        minutes >= 2 -> "$minutes mins ago"
        minutes >= 1 -> if (numericDates) "1 min ago" else "1 min ago"
        seconds >= 3 -> "$seconds secs ago"
        else -> "Just now"
    }
}











