package com.example.the_schedulaing_application.element.calendar


import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.the_schedulaing_application.domain.Cases.CaseRepeatableType
import com.example.the_schedulaing_application.domain.Cases.CaseType
import com.example.the_schedulaing_application.domain.Cases.SlateEvent
import com.example.the_schedulaing_application.domain.Klinder
import com.example.the_schedulaing_application.domain.kMonth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject


class CalViewModel : ViewModel() {

    private val klinder = Klinder.getInstance()

    private var _tinyBoxes = mutableStateOf(false);
    val tinyBox: State<Boolean> = _tinyBoxes

    private var _month = mutableStateOf(klinder.getMonth())
    val month: State<kMonth> = _month

    private var _yearInt = mutableIntStateOf(Klinder.getInstance().getYear().yearInt)
    val year: State<Int> = _yearInt

    private var _monthDateBoxData = mutableListOf<DateBoxData>()
    val monthDateBoxData: List<DateBoxData> = _monthDateBoxData

    private val _sampleEventList: MutableList<SlateEvent> = mutableListOf(
        SlateEvent("Singlton",
            "SingltonDescription",
            caseType = CaseType.CaseSingleton(Klinder.getInstance().getCurrentTimeMillis())
        ),
        SlateEvent("Duration",
            "DurationDescription",
            caseType = CaseType.CaseDuration(Klinder.getInstance().getCurrentTimeMillis(),Klinder.getInstance().getCurrentTimeMillis())
        ),
        SlateEvent("Repeatable",
            "RepeatableDescription",
            caseType = CaseType.CaseRepeatable(CaseRepeatableType.Daily(1000))
        )
    )

    fun setMonthYear(month: Int,year: Int){
        val kMonth = klinder.getMonth(month)

    }

    fun refreshCalendarData(){

    }

    fun getDateBoxData(date: Int): DateBoxData{
        // Add to Database
        return DateBoxData(date,_sampleEventList)
    }

    fun daysInThisMonth(): Int{
        return Klinder.getInstance().getMonth().daysInMonth
    }

    // From 1 - Sunday to 7 - Saturday
    fun getFirstDayOfMonth(): Int{
        return Klinder.getInstance().getFirstDayOfTheMonth()
    }
    fun getMonthStr(): String{
        return Klinder.getInstance().getMonth().monthStr;
    }

}