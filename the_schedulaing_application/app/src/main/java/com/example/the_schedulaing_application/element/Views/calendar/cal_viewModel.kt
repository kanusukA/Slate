package com.example.the_schedulaing_application.element.Views.calendar


import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.the_schedulaing_application.domain.Cases.CaseRepeatableType
import com.example.the_schedulaing_application.domain.Cases.CaseType
import com.example.the_schedulaing_application.domain.Cases.SlateEvent
import com.example.the_schedulaing_application.domain.Klinder
import com.example.the_schedulaing_application.domain.kMonth
import com.example.the_schedulaing_application.domain.kTime
import com.example.the_schedulaing_application.domain.kYear
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.util.Date
import javax.inject.Inject


class CalViewModel : ViewModel() {

    private val klinder = Klinder.getInstance()

    private var _tinyBoxes = mutableStateOf(false);
    val tinyBox: State<Boolean> = _tinyBoxes

    private var _month = MutableStateFlow(klinder.getMonth())
    val month: StateFlow<kMonth> = _month

    private var _year = MutableStateFlow(klinder.getYear())
    val year: StateFlow<kYear> = _year

    private var _monthDateBoxData = MutableStateFlow(CalDateBoxData())
    val monthDateBoxData: StateFlow<CalDateBoxData> = _monthDateBoxData

    private var _totalDateBoxes = MutableStateFlow(_month.value.firstDayInMonth + _month.value.daysInMonth)
    val totalDateBoxes: StateFlow<Int> = _totalDateBoxes

    init {
        refreshCalendarData()
    }

    fun changeToNextMonth(){
        klinder.addTo(kTime(month = 1))
        refreshCalendarData()
    }
    fun changeToPreviousMonth(){ // change Year
        klinder.addTo(kTime(month = -1))
        refreshCalendarData()
    }
    fun changeToMonth(month: Int){
        klinder.setMonth(month)
        refreshCalendarData()
    }

    fun refreshCalendarData(){

        _month.update { klinder.getMonth() }
        _year.update { klinder.getYear() }

        _totalDateBoxes.update { _month.value.firstDayInMonth + _month.value.daysInMonth }

        if (_monthDateBoxData.value.datesData.size > _month.value.daysInMonth){
            _monthDateBoxData.update {
                it.apply {
                    datesData = datesData.slice(IntRange(0,_month.value.daysInMonth - 1)).toMutableList()
                }
            }
        } else {
            _monthDateBoxData.update {
                it.apply {
                    val initSize = datesData.size
                    for (i in 1.._month.value.daysInMonth-_monthDateBoxData.value.datesData.size){
                        datesData.add(DateBoxData( initSize + i, emptyList()))
                    }
                }
            }
        }

    }

    fun getKMonths(): List<String>{
        return klinder.kMonths
    }

}

data class CalDateBoxData(
    var datesData: MutableList<DateBoxData> = mutableListOf()
)