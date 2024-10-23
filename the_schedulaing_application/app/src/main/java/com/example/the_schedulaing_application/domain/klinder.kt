package com.example.the_schedulaing_application.domain

import androidx.annotation.Nullable
import com.example.the_schedulaing_application.domain.Cases.SlateWeeks
import java.util.Calendar
import java.util.Locale
import kotlin.math.min


// Singleton Class
class Klinder private constructor() {

    companion object {
        @Volatile
        private var instance: Klinder? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: Klinder().also { instance = it }
        }
    }

    private val _calendar = Calendar.getInstance();

    private val _date: kDate = kDate();
    private val _month: kMonth = kMonth();
    private val _year: kYear = kYear();

    val kMonths: List<String> = listOf(
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December"
    )
    val kWeeks: List<String> = listOf(
        "Sunday",
        "Monday",
        "Tuesday",
        "Wednesday",
        "Thursday",
        "Friday",
        "Saturday"
    )

    init {
        _updateDate()
        _updateMonth()
        _updateYear()
    }

    fun getYear(): kYear {
        return _year;
    }

    fun getDate(): kDate {
        return _date
    }

    fun getMonth(): kMonth {
        return _month
    }

    fun getKTime(): kTime{
        return kTime(
            date = _calendar.get(Calendar.DATE),
            month = _calendar.get(Calendar.MONTH),
            year = _calendar.get(Calendar.YEAR),
            hour = _calendar.get(Calendar.HOUR_OF_DAY),
            min = _calendar.get(Calendar.MINUTE),
            sec = _calendar.get(Calendar.SECOND)
        )
    }
    /*fun getMonth(month: Int): kMonth{
        _calendar.set(Calendar.MONTH, month)
        _calendar.set(Calendar.DATE,1)
        val rValue =
            kMonth(
                monthInt = _calendar.get(Calendar.MONTH),
                daysInMonth = _calendar.getActualMaximum(Calendar.DATE),
                monthStr = kMonths[month],
                firstDayInMonth = _calendar.get(Calendar.DAY_OF_WEEK) - 1
            )
        _calendar.timeInMillis = System.currentTimeMillis()
        return rValue
    }
    fun getNextPrevMonth(fromMonth: Int,by: Int): kMonth{
        _calendar.set(Calendar.MONTH,fromMonth)
        _calendar.add(Calendar.MONTH,by)
        _calendar.set(Calendar.DATE,1)
        val rValue =
            kMonth(
                monthInt = _calendar.get(Calendar.MONTH),
                daysInMonth = _calendar.getActualMaximum(Calendar.DATE),
                monthStr = kMonths[_calendar.get(Calendar.MONTH)],
                firstDayInMonth = _calendar.get(Calendar.DAY_OF_WEEK) - 1
            )
        _calendar.timeInMillis = System.currentTimeMillis()
        return rValue
    }*/

    fun setMonth(monthInt: Int) {
        _calendar.set(Calendar.MONTH, monthInt);
        _updateDate();
        _updateMonth();
    }

    // Progresses the calendar with provided time
    fun addTo(time: kTime): kTime{
        _calendar.add(Calendar.DATE, time.date)
        _calendar.add(Calendar.MONTH, time.month)
        _calendar.add(Calendar.YEAR, time.year)
        _calendar.add(Calendar.HOUR_OF_DAY, time.hour)
        _calendar.add(Calendar.MINUTE, time.min)
        _calendar.add(Calendar.SECOND, time.sec)

        _updateDate()
        _updateMonth()
        _updateYear()

        return getKTime()
    }

    // Helper Functions

    fun getFirstDayOfTheMonth(): Int {
        _calendar.set(Calendar.DATE, 1)
        val dow = _calendar.get(Calendar.DAY_OF_WEEK)
        _calendar.timeInMillis = System.currentTimeMillis()
        return dow
    }

    fun currentDateTimeMillis(): Long {
        _calendar.set(Calendar.HOUR_OF_DAY, 0)
        _calendar.set(Calendar.MINUTE, 0)
        _calendar.set(Calendar.SECOND, 0)
        val rValue = System.currentTimeMillis() - _calendar.timeInMillis
        _calendar.timeInMillis = System.currentTimeMillis()
        return rValue
    }

    /*fun getNextWeekTimeMillis(week: Int): Long{
        val weekDayDiffer = if (_date.dayInt - week > 0){
            _date.dayInt - week
        }else{
            week - _date.dayInt
        }
        return (currentDateTimeMillis() + (weekDayDiffer * 86400000)).toLong()
    }*/

    fun getNextActiveWeekDay(weeks: List<SlateWeeks>): SlateWeeks{
        val currentDay = _date.dayInt
        for (i in currentDay..currentDay + 6){
            weeks.forEach {
                if(i == it.ordinal){
                    return it
                }
            }
        }
        return SlateWeeks.THURSDAY
    }

    //Conversion Functions
    fun conWeekIntToWeekStr(week: Int): String{
        return kWeeks[week]
    }
    fun conWeekStrToWeekInt(week: String): Int { kWeeks.forEachIndexed { index, s ->
            if (s == week){
                return index
            }
        }
        return 0
    }

    fun conMonthIntToMonthStr(month: Int): String {
        return  kMonths[month]
    }
    fun conMonthStrToMonthInt(month: String): Int{
        kMonths.forEachIndexed { index, s ->
            if (s == month){return index}
        }
        return 0
    }

    // Time Functions

    fun timeMillisTokTime(timeMillis: Long): kTime {
        _calendar.timeInMillis = timeMillis
        val rValue = getTimeAdd(add = kTime())
        _calendar.timeInMillis = System.currentTimeMillis()
        return rValue
    }

    // if from is null currentTime is taken
    fun getTimeDifference(from: kTime? = null, to: kTime): Long {
        var rValue: Long
        _calendar.set(
            to.year,
            to.month,
            to.date,
            to.hour,
            to.min,
            to.sec
        )
        if (from == null) {
            rValue = _calendar.timeInMillis - System.currentTimeMillis()

        } else {
            val toTimeMilli = _calendar.timeInMillis
            _calendar.set(
                from.year,
                from.month,
                from.date,
                from.hour,
                from.min,
                from.sec
            )
            rValue = toTimeMilli - System.currentTimeMillis()
        }
        _calendar.timeInMillis = System.currentTimeMillis()
        return rValue
    }

    // if to is null currentTime is Take  and value to added to it
    fun getTimeAdd(to: kTime? = null, add: kTime): kTime {
        if (to != null) {
            _calendar.set(to.date, to.month, to.year, to.hour, to.min, to.sec)
        }
        addTo(add)
        val rValue = getKTime()
        _calendar.timeInMillis = System.currentTimeMillis()
        return rValue
    }

    private fun _updateDate() {
        _date.dateInt = _calendar.get(Calendar.DATE);
        _date.dateStr =
            _calendar.getDisplayName(Calendar.DATE, Calendar.LONG_FORMAT, Locale.ENGLISH) ?: "NULL"
        _date.dayInt = _calendar.get(Calendar.DAY_OF_WEEK)
        _date.dayStr =
            _calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG_FORMAT, Locale.ENGLISH)
                ?: "NULL"
    }

    private fun _updateMonth() {
        _month.monthInt = _calendar.get(Calendar.MONTH)
        _month.monthStr = _calendar.getDisplayName(Calendar.MONTH, Calendar.LONG_FORMAT, Locale.ENGLISH) ?: "NULL"
        _month.daysInMonth = _calendar.getActualMaximum(Calendar.DATE)
        _calendar.set(Calendar.DATE,1)
        _month.firstDayInMonth = _calendar.get(Calendar.DAY_OF_WEEK) - 1
        _calendar.set(Calendar.DATE,_date.dateInt)

    }

    private fun _updateYear() {
        _year.yearInt = _calendar.get(Calendar.YEAR)
    }

}

data class kTime(
    var date: Int = 0,
    var month: Int = 0,
    var year: Int = 0,
    var hour: Int = 0,
    var min: Int = 0,
    var sec: Int = 0
)

data class kDate(
    var dateInt: Int = 0,
    var dateStr: String = "",
    var dayInt: Int = 0,
    var dayStr: String = ""
)

data class kMonth(
    var monthInt: Int = 0,
    var daysInMonth: Int = 0,
    var firstDayInMonth: Int = 0,
    var monthStr: String = ""
)

data class kYear(
    var yearInt: Int = 0,
)

//  0 - 11


