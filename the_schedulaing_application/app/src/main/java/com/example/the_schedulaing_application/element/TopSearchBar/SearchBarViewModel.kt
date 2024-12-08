package com.example.the_schedulaing_application.element.TopSearchBar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.the_schedulaing_application.domain.Klinder
import com.example.the_schedulaing_application.element.Navigation.NavConductorViewModel
import com.example.the_schedulaing_application.element.Navigation.NavRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SearchBarViewModel @Inject constructor(
    val navConductorViewModel: NavConductorViewModel
) : ViewModel() {

    val searchBarState: StateFlow<SearchBarState> = navConductorViewModel.navPage.map {
        when(it){
            NavRoutes.HomePage-> {SearchBarState.HOME_PAGE}
            NavRoutes.AddEditPage -> {SearchBarState.ADD_EDIT_PAGE}
            NavRoutes.CalendarPage -> {SearchBarState.CALENDAR_PAGE}
            NavRoutes.FunctionPage -> {SearchBarState.FUNCTION_PAGE}
            NavRoutes.CalendarDetailPage -> { SearchBarState.CALENDAR_PAGE}
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = SearchBarState.HOME_PAGE
    )


    private var _showSearchBar = MutableStateFlow(false)
    val showSearchBar: StateFlow<Boolean> = _showSearchBar


    // HomePage

    fun changeSearchEventTextType(eventType: SearchBarEventTextType){
        navConductorViewModel.changeSearchBarState(eventType)
        when(eventType){
            is SearchBarEventTextType.Searching -> {_showSearchBar.update { true }}
            else -> {_showSearchBar.update { false }}
        }
    }

    // Calendar Functions

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

    fun changeMonth(month: Int){
        navConductorViewModel.changeSearchMonth(month)
    }

    fun showFilterBar(show: Boolean){
     navConductorViewModel.setFilterBarVisibility(show)
    }



}

sealed class SearchBarEventTextType(val text: String) {
    data class Singleton(val str: String = "Singleton") : SearchBarEventTextType(str)
    data class Duration(val str: String = "Duration") : SearchBarEventTextType(str)
    data class Repeating(val str: String = "Repeating") : SearchBarEventTextType(str)
    data class Daily(val str: String = "Daily") : SearchBarEventTextType(str)
    data class Weekly(val str: String = "Weekly") : SearchBarEventTextType(str)
    data class Monthly(val str: String = "Monthly") : SearchBarEventTextType(str)
    data class Yearly(val str: String = "Yearly") : SearchBarEventTextType(str)
    data class YearlyEvents(val str: String = "Events") : SearchBarEventTextType(str)
    data class All(val str: String = "All Events") : SearchBarEventTextType(str)
    data class Searching(val str: String) : SearchBarEventTextType(str)
    data class Calender(val month: String) : SearchBarEventTextType(month)
}

enum class SearchBarFilterType {
    ASCENDING,
    DESCENDING,
    CALENDAR
}

enum class SearchBarState {
    HOME_PAGE,
    CALENDAR_PAGE,
    ADD_EDIT_PAGE,
    FUNCTION_PAGE
}