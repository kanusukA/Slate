package com.example.the_schedulaing_application.domain.Cases

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.example.the_schedulaing_application.R
import com.example.the_schedulaing_application.domain.Klinder
import com.example.the_schedulaing_application.domain.kTime
import com.example.the_schedulaing_application.ui.theme.eventMaroon
import com.example.the_schedulaing_application.ui.theme.eventMarron80
import com.example.the_schedulaing_application.ui.theme.eventRed
import java.util.Date
import kotlin.enums.EnumEntries

// TODO OPTIMIZE THIS GOOD
class SlateEvent(
    eventName: String,
    eventDescription: String,
    caseType: CaseType
) {

    private val klinder = Klinder.getInstance()

    private var _eventName = mutableStateOf(eventName)
    val eventName: State<String> = _eventName

    private var _eventDescription = mutableStateOf(eventDescription)
    val eventDescription: State<String> = _eventDescription

    private var _caseType = mutableStateOf(caseType)
    val caseType: State<CaseType> = _caseType

    // EVENT MARK
    fun getCaseSpecificColor(): Color {
        return when (_caseType.value) {
            is CaseType.CaseDuration -> eventRed
            is CaseType.CaseRepeatable -> eventMaroon
            is CaseType.CaseSingleton -> eventMarron80
        }
    }

    fun getEventIcon(): Int {
        return when (_caseType.value) {
            is CaseType.CaseDuration -> R.drawable.caseduration_100_icon
            is CaseType.CaseRepeatable -> R.drawable.caserepeatable_icon
            is CaseType.CaseSingleton -> R.drawable.casesingleton_icon
        }
    }

    fun getEventString(): String {
        return when(_caseType.value){
            is CaseType.CaseDuration -> "Duration"
            is CaseType.CaseRepeatable -> "Repeating"
            is CaseType.CaseSingleton -> "Instance"
        }
    }

    fun getTimeLeft(): kTime {
        return when (_caseType.value) {
            is CaseType.CaseDuration -> {
                getMillisToTimeLeft(((_caseType.value as CaseType.CaseDuration).fromEpoch - System.currentTimeMillis()))
            }

            is CaseType.CaseRepeatable -> {
                getNextRepeatableTimeLeft((_caseType.value as CaseType.CaseRepeatable).caseRepeatableType)
            }

            is CaseType.CaseSingleton -> {
                getMillisToTimeLeft((_caseType.value as CaseType.CaseSingleton).epochTimeMilli - System.currentTimeMillis())
            }
        }
    }

    fun getDateStringFromKTime(time: kTime): String {
        return if (time.date == 1) {
            time.date.toString() + " Day"
        } else if (time.date > 0) {
            time.date.toString() + " Days"
        } else if (time.hour > 0 && time.min > 0) {
            time.hour.toString() + " Hour " + time.min + " Min"
        } else if (time.hour > 0) {
            time.hour.toString() + " Hour"
        } else if (time.min > 0) {
            time.min.toString() + " Min"
        } else if (time.sec > 0) {
            time.sec.toString() + " Sec"
        } else {
            "Now"
        }
    }

    private fun getNextRepeatableTimeLeft(caseRepeatableType: CaseRepeatableType): kTime {
        when (caseRepeatableType) {
            is CaseRepeatableType.Daily -> {
                return if (caseRepeatableType.timeOfDay - klinder.currentDateTimeMillis() < 0) {
                    getMillisToTimeLeft((caseRepeatableType.timeOfDay + 86400000) - klinder.currentDateTimeMillis())
                } else {
                    getMillisToTimeLeft(caseRepeatableType.timeOfDay - klinder.currentDateTimeMillis())
                }
            }

            is CaseRepeatableType.Weekly -> {

                val currentDayInt = klinder.getDate().dayInt
                val selectWeek = klinder.getNextActiveWeekDay(caseRepeatableType.selectWeeks)
                var weekDiff = 0

                weekDiff = if (selectWeek.ordinal+1 < currentDayInt){
                    (7 - currentDayInt) + selectWeek.ordinal+1
                } else{
                    (selectWeek.ordinal+1) - currentDayInt
                }

                return getMillisToTimeLeft(
                    klinder.getTimeDifference(
                        to = klinder.getTimeAdd(
                            add = kTime(date = weekDiff)
                        )
                    ) - klinder.currentDateTimeMillis()
                )

            }

            is CaseRepeatableType.Monthly -> {
                return getMillisToTimeLeft(
                    klinder.getTimeDifference(
                        to = klinder.getTimeAdd(
                            add = kTime(month = 1)
                        ).apply { date = caseRepeatableType.selectDates.min() }
                    )
                )
            }

            // TODO OPTIMIZE THIS
            is CaseRepeatableType.Yearly -> {

                var nextMonth = caseRepeatableType.selectMonths[0]
                var year = Klinder.getInstance().getYear().yearInt
                var date = caseRepeatableType.date.min()

                caseRepeatableType.selectMonths.forEach {
                    val monthDiff = it - Klinder.getInstance().getMonth().monthInt
                    // Add Date
                    if (monthDiff == 0 && caseRepeatableType.date.max() >= Klinder.getInstance().getDate().dateInt){
                        caseRepeatableType.date.forEach {
                            date = caseRepeatableType.date.max()
                            val dateDiff = it - Klinder.getInstance().getDate().dateInt
                            if(dateDiff > 0 && dateDiff < date - Klinder.getInstance().getDate().dateInt){
                                date = it
                            }
                        }
                        nextMonth = it
                    }

                    if (monthDiff > 0 && monthDiff < nextMonth - Klinder.getInstance().getMonth().monthInt ) {
                        nextMonth = it
                    }

                }

                if(nextMonth < Klinder.getInstance().getMonth().monthInt){
                    nextMonth = caseRepeatableType.selectMonths.min()
                    year += 1
                }



                return getMillisToTimeLeft(
                    klinder.getTimeDifference(
                        to = kTime(
                            date =  date,
                            month = nextMonth,
                            year = year
                        )
                    )
                )

            }

            is CaseRepeatableType.YearlyEvent -> {

                return if (caseRepeatableType.month >= klinder.getMonth().monthInt) {
                    getMillisToTimeLeft(
                        klinder.getTimeDifference(
                            to = klinder.getTimeAdd(add = kTime()).apply {
                                date = caseRepeatableType.date
                                month = caseRepeatableType.month
                            }
                        )
                    )
                } else {
                    getMillisToTimeLeft(
                        klinder.getTimeDifference(
                            to = klinder.getTimeAdd(
                                add = kTime(
                                    year = 1
                                ).apply {
                                    date = caseRepeatableType.date
                                    month = caseRepeatableType.month
                                }
                            )
                        )
                    )
                }
            }
        }
    }

    // Send TimeMillis From CurrentTime
    private fun getMillisToTimeLeft(timeMillis: Long): kTime {

        return if (timeMillis / 1000 <= 60) {
            kTime(sec = ((timeMillis / 1000).toInt()))
        } else if (timeMillis / 60000 <= 60) {
            kTime(
                min = (timeMillis / 60000).toInt(),
                sec = ((timeMillis % 60000) / 1000).toInt()
            )
        } else if (timeMillis / 3600000 <= 24) {
            kTime(
                hour = (timeMillis / 3600000).toInt(),
                min = ((timeMillis % 3600000) / 60000).toInt()
            )
        } else {
            kTime(
                date = (timeMillis / 86400000).toInt()
            )
        }
    }

    private fun getMillisToTimeLeft(timeMillis: Int): String {
        return if (timeMillis / 1000 <= 60) {
            (timeMillis / 1000).toString() + " Sec"
        } else if (timeMillis / 60000 <= 60) {
            (timeMillis / 60000).toString() + " Min"
        } else if (timeMillis / 3600000 <= 24) {
            (timeMillis / 3600000).toString() + " Hour"
        } else {
            (timeMillis / 86400000).toString() + " Days"
        }
    }

    fun copy(
        eventName: String = _eventName.value,
        eventDescription: String = _eventDescription.value,
        caseType: CaseType = _caseType.value
    )= SlateEvent(
        eventName,
        eventDescription,
        caseType
    )


}

sealed class CaseType {
    data class CaseSingleton(val epochTimeMilli: Long) : CaseType()
    data class CaseRepeatable(val caseRepeatableType: CaseRepeatableType) : CaseType()
    data class CaseDuration(val fromEpoch: Long, val toEpoch: Long) : CaseType()
}


sealed class CaseRepeatableType {
    data class Daily(val timeOfDay: Long) : CaseRepeatableType()

    // EnumEntries : Works as list of Enums
    data class Weekly(val selectWeeks: List<SlateWeeks>) : CaseRepeatableType()
    data class Monthly(val selectDates: List<Int>) : CaseRepeatableType()

    // Repeats Monthly Different from Yearly Event
    data class Yearly(val date: List<Int>, val selectMonths: List<Int>) : CaseRepeatableType()

    // For Yearly Events like Birthdays
    // not in long set in date and month as it is not affected by the year
    data class YearlyEvent(val date: Int, val month: Int) : CaseRepeatableType()
}

enum class SlateWeeks {
    SUNDAY,
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY

}