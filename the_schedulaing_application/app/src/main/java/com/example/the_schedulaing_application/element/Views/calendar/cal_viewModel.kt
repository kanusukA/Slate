package com.example.the_schedulaing_application.element.Views.calendar


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.the_schedulaing_application.data.viewModels.MainRealmViewModel
import com.example.the_schedulaing_application.domain.Klinder
import com.example.the_schedulaing_application.element.Navigation.NavConductorViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CalViewModel @Inject constructor(
    val navConductorViewModel: NavConductorViewModel,
    private val mainRealmViewModel: MainRealmViewModel
): ViewModel() {

    private val klinder = Klinder.getInstance()



    // create list of month with events
    val monthEvents = navConductorViewModel.searchMonth.combine(mainRealmViewModel.slateEvents) { month, slateEvents ->
        val thisMonth = month
        val dateObjs = mutableListOf<DateBoxObj>()
        val events = mainRealmViewModel.slateEvents.value.filter {
            it.getNextTime().month == thisMonth
        }

        for (i in 1..klinder.getDaysInMonth(month)){
            val dateBoxObj = DateBoxObj(
                i,
                i.toString(),
                events.filter { it.getNextTime().date == i }
            )
            dateObjs.add(dateBoxObj)
        }

        dateObjs.toList()

    }.stateIn(
        viewModelScope,
        started = SharingStarted.Eagerly,
        emptyList()
    )

    val selectedEvents = monthEvents.map {
        it.first { date -> date.date == navConductorViewModel.dateDetailDate.value }.events
    }.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        emptyList()
    )

    fun nextMonth(){
        if(navConductorViewModel.searchMonth.value > 10){
            navConductorViewModel.changeSearchMonth(0)
        }else{
            navConductorViewModel.changeSearchMonth(navConductorViewModel.searchMonth.value + 1)
        }

    }

    fun previousMonth(){
        if(navConductorViewModel.searchMonth.value < 1){
            navConductorViewModel.changeSearchMonth(11)
        }else{
            navConductorViewModel.changeSearchMonth(navConductorViewModel.searchMonth.value - 1)
        }
    }

}

data class CalDateBoxData(
    var datesData: MutableList<DateBoxData> = mutableListOf()
)