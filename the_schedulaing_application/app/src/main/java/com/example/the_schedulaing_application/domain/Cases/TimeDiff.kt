package com.example.the_schedulaing_application.domain.Cases

import com.example.the_schedulaing_application.domain.Klinder
import com.example.the_schedulaing_application.domain.kTime

fun caseSingletonTimeDiff(epochTime: Long): kTime{
    return getMillisToTimeLeft(epochTime - System.currentTimeMillis())
}

fun caseDurationTimeDiff(fromEpochTime: Long, toEpochTime: Long): kTime{
    return if(fromEpochTime >= System.currentTimeMillis()){
        getMillisToTimeLeft(fromEpochTime - System.currentTimeMillis())
    }else{
        getMillisToTimeLeft(toEpochTime - System.currentTimeMillis())
    }
}

fun caseDailyTimeDiff(dayTimeMille: Long):kTime{
    return if(Klinder.getInstance().getTimeOfDayLong() > dayTimeMille){
        getMillisToTimeLeft( 86400000 - (Klinder.getInstance().getTimeOfDayLong() - dayTimeMille))
    }else{
        getMillisToTimeLeft( dayTimeMille - Klinder.getInstance().getTimeOfDayLong())
    }

}

fun caseWeeklyTimeDiff(weeks: List<SlateWeeks>): kTime{
    val nextWeekDay = Klinder.getInstance().getNextActiveWeekDay(weeks)
    return if (nextWeekDay.ordinal+1 < Klinder.getInstance().getDate().dayInt){
        getMillisToTimeLeft(((7 - Klinder.getInstance().getDate().dayInt) + nextWeekDay.ordinal+1) * 86400000L)
    } else{
        getMillisToTimeLeft(((nextWeekDay.ordinal+1) - Klinder.getInstance().getDate().dayInt) * 86400000L)
    }
}

// Must be a sorted List
fun caseMonthlyTimeDiff(dates: List<Int>): kTime{
    val klinder = Klinder.getInstance()
    if(dates.last() <= klinder.getDate().dateInt){
        return getMillisToTimeLeft(
            klinder.getTimeDifference(
                to = klinder.getTimeAdd(
                    add = kTime(month = 1)
                ).apply { date = dates.first() }
            )
        )
    }else{
        dates.forEach {
            if(it > klinder.getDate().dateInt){
                return getMillisToTimeLeft(
                    (it - klinder.getDate().dateInt) * 86400000L
                )
            }
        }
    }
    return kTime()
}

// Must be sorted Lists
fun caseYearlyTimeDiff(dates: List<Int> , months: List<Int>): kTime{
    val klinder = Klinder.getInstance()
    if(months.last() >= klinder.getMonth().monthInt){
        // This year
        months.forEach {
            if (it == klinder.getMonth().monthInt){
                // This month
               return caseMonthlyTimeDiff(dates)
            }else if(it > klinder.getMonth().monthInt){
                return getMillisToTimeLeft(
                    klinder.getTimeDifference(
                        to = klinder.getKTime().apply {
                            month = it
                            date = dates.first()
                        }
                    )
                )
            }
        }

    }else{
        // Next year
        return getMillisToTimeLeft(
            klinder.getTimeDifference(
                to = klinder.getTimeAdd(
                    add = kTime(year = 1)
                ).apply { date = dates.first()
                    month = months.first()
                }
            )
        )
    }
    return kTime()
}

fun caseYearlyEventTimeDiff(date: Int, month: Int): kTime{
    val klinder = Klinder.getInstance()

    return if (klinder.getKTime().date < date && klinder.getKTime().month < month){
        kTime(date = date, month =  month, year = klinder.getYear().yearInt + 1)
    }else{
        kTime(date = date, month =  month, year = klinder.getYear().yearInt)
    }

}


fun getDateStringFromKTime(
    time: kTime,
    showHourMin: Boolean = false
): String {
    return if (time.date == 1) {
        time.date.toString() + " Day"
    } else if (time.date > 0) {
        time.date.toString() + " Days"
    } else if (time.hour > 0 && time.min > 0 && showHourMin) {
        time.hour.toString() + " Hours " + time.min + " Min"
    } else if (time.hour > 0) {
        time.hour.toString() + " Hours"
    } else if (time.min > 0) {
        time.min.toString() + " Min"
    } else if (time.sec > 0) {
        time.sec.toString() + " Sec"
    } else {
        "Now"
    }
}


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