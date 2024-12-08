package com.example.the_schedulaing_application.element.Views.calendar


import androidx.lifecycle.ViewModel
import com.example.the_schedulaing_application.data.viewModels.MainRealmViewModel
import com.example.the_schedulaing_application.element.Navigation.NavConductorViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class CalViewModel @Inject constructor(
    val navConductorViewModel: NavConductorViewModel,
    private val mainRealmViewModel: MainRealmViewModel
): ViewModel() {

    val monthEvents = navConductorViewModel.searchMonth.map { month ->
        val thisMonth = month
        mainRealmViewModel.slateEvents.value.filter {
            it.getNextTime().month == thisMonth
        }
    }

}

data class CalDateBoxData(
    var datesData: MutableList<DateBoxData> = mutableListOf()
)