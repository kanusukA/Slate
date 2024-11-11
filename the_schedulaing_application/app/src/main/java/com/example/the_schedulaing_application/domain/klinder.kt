package com.example.the_schedulaing_application.domain

import androidx.annotation.Nullable
import com.example.the_schedulaing_application.domain.Cases.SlateWeeks
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import java.time.Clock
import java.time.temporal.TemporalAdjusters
import java.time.temporal.TemporalField
import java.time.temporal.TemporalQueries
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

    fun clockFlow() = flow<Long> {
        var startMillie = System.currentTimeMillis()
        while(true){
            delay(300)
            if(System.currentTimeMillis()/1000 != startMillie){
                emit(System.currentTimeMillis())
                break
            }
        }
        while(true){
            delay(1000)
            emit(System.currentTimeMillis())
        }
    }

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

    fun reset(){
        _calendar.timeInMillis = System.currentTimeMillis()
    }

    fun isLeapYear(): Boolean{
        return (getYear().yearInt % 4 == 0 && getYear().yearInt % 100 != 0) || getYear().yearInt % 400 == 0
    }

    fun getKClock(): kClock {
        return kClock(
            _calendar.get(Calendar.HOUR_OF_DAY),
            _calendar.get(Calendar.MINUTE),
            _calendar.get(Calendar.SECOND)
        )
    }

    fun getTimeOfDayLong(): Long{
        return ((((_calendar.get(Calendar.HOUR_OF_DAY).toLong() * 60) + _calendar.get(Calendar.MINUTE)) * 60) + _calendar.get(Calendar.SECOND)) * 1000
    }

    fun getKTime(): kTime{
        return kTime(
            date = _calendar.get(Calendar.DATE),
            day = _calendar.getDisplayName(Calendar.DAY_OF_WEEK,Calendar.LONG_FORMAT, Locale.ENGLISH) ?: "",
            month = _calendar.get(Calendar.MONTH),
            monthStr = _calendar.getDisplayName(Calendar.MONTH,Calendar.LONG_FORMAT, Locale.ENGLISH) ?: "",
            year = _calendar.get(Calendar.YEAR),
            hour = _calendar.get(Calendar.HOUR_OF_DAY),
            min = _calendar.get(Calendar.MINUTE),
            sec = _calendar.get(Calendar.SECOND)
        )
    }

    fun getKTime(timeMillis: Long): kTime{
        _calendar.timeInMillis = timeMillis
        val rValue = kTime(
            date = _calendar.get(Calendar.DATE),
            day = _calendar.getDisplayName(Calendar.DAY_OF_WEEK,Calendar.LONG_FORMAT, Locale.ENGLISH) ?: "",
            month = _calendar.get(Calendar.MONTH),
            monthStr = _calendar.getDisplayName(Calendar.MONTH,Calendar.LONG_FORMAT, Locale.ENGLISH) ?: "",
            year = _calendar.get(Calendar.YEAR),
            hour = _calendar.get(Calendar.HOUR_OF_DAY),
            min = _calendar.get(Calendar.MINUTE),
            sec = _calendar.get(Calendar.SECOND)
        )
        _calendar.timeInMillis = System.currentTimeMillis()

        return rValue
    }

    fun getDaysInMonth(month: Int): Int{
        _calendar.set(Calendar.MONTH,month)
        val rValue =  _calendar.getActualMaximum(Calendar.DATE)
        reset()
        return rValue

    }

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

    fun getWeekOnDate(date: Int, month: Int, year: Int): String{
        _calendar.set(year, month, date)
        val rWeek = _calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG_FORMAT, Locale.ENGLISH)
            ?: "NULL"
        _calendar.timeInMillis = System.currentTimeMillis()
        return rWeek
    }

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
    var day: String = "",
    var month: Int = 0,
    var monthStr: String = "",
    var year: Int = 0,
    var hour: Int = 0,
    var min: Int = 0,
    var sec: Int = 0,

)

data class kClock(
    var hour: Int,
    var min: Int,
    var sec: Int,
    val timeOfDay: Long =
        ((((hour.toLong() * 60) + min) * 60) + sec ) * 1000
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


