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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.mongodb.kbson.ObjectId
import java.util.Date
import kotlin.enums.EnumEntries

// TODO OPTIMIZE THIS GOOD
class SlateEvent(
    name: String,
    description: String,
    inputCaseType: CaseType
) {

    private val klinder = Klinder.getInstance()

    var id: ObjectId = ObjectId()

    var eventName = name

    var eventDescription = description

    var caseType: CaseType = inputCaseType

    private var _completed = MutableStateFlow(false)
    val completed = _completed.asStateFlow()


    init {
        getTimeLeft()
    }

    // EVENT MARK
    fun getCaseSpecificColor(): Color {
        return when (caseType) {
            is CaseType.CaseDuration -> eventRed
            is CaseType.CaseRepeatable -> eventMaroon
            is CaseType.CaseSingleton -> eventMarron80
        }
    }

    fun getEventIcon(): Int {
        return when (caseType) {
            is CaseType.CaseDuration -> R.drawable.caseduration_100_icon
            is CaseType.CaseRepeatable -> R.drawable.caserepeatable_icon
            is CaseType.CaseSingleton -> R.drawable.casesingleton_icon
        }
    }


    fun getEventString(): String {
        return when(caseType){
            is CaseType.CaseDuration -> "Duration"
            is CaseType.CaseRepeatable -> "Repeating"
            is CaseType.CaseSingleton -> "Instance"
        }
    }

    fun getNextTime(): kTime{
        return klinder.getTimeAdd(add = getTimeLeft().apply { date += 1 })
    }

    fun getTimeLeft(): kTime {
        if (completed.value){
            return kTime()
        }
        val case: CaseType = caseType
        return when (case) {
            is CaseType.CaseDuration -> {
                if(case.toEpoch < System.currentTimeMillis()){
                    _completed.update { true }
                }
                caseDurationTimeDiff(case.fromEpoch,case.toEpoch)
            }

            is CaseType.CaseRepeatable -> {
                when(case.caseRepeatableType){
                    is CaseRepeatableType.Daily -> {
                        caseDailyTimeDiff(case.caseRepeatableType.timeOfDay)
                    }
                    is CaseRepeatableType.Monthly -> {
                        caseMonthlyTimeDiff(case.caseRepeatableType.selectDates)
                    }
                    is CaseRepeatableType.Weekly -> {
                        caseWeeklyTimeDiff(case.caseRepeatableType.selectWeeks)
                    }
                    is CaseRepeatableType.Yearly -> {
                        caseYearlyTimeDiff(case.caseRepeatableType.date,case.caseRepeatableType.selectMonths)
                    }
                    is CaseRepeatableType.YearlyEvent -> {
                        caseYearlyEventTimeDiff(case.caseRepeatableType.date,case.caseRepeatableType.month)
                    }
                }
            }

            is CaseType.CaseSingleton -> {
                if(case.epochTimeMilli < System.currentTimeMillis()) {
                    _completed.update { true }
                }
                caseSingletonTimeDiff(case.epochTimeMilli)
            }
        }
    }

    fun copy(
        name: String = eventName,
        description: String = eventDescription,
        inputCaseType: CaseType = caseType
    )= SlateEvent(
        name, description, inputCaseType
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