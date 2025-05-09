package com.example.the_schedulaing_application.element.Navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavBackStackEntry
import com.example.the_schedulaing_application.domain.Klinder
import com.example.the_schedulaing_application.domain.kMonth
import com.example.the_schedulaing_application.element.TopSearchBar.SearchBarEventTextType
import com.example.the_schedulaing_application.element.TopSearchBar.SearchBarState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NavConductorViewModel(): ViewModel() {

    private var _navPage: MutableStateFlow<NavRoutes> = MutableStateFlow(NavRoutes.HomePage)
    val navPage: StateFlow<NavRoutes> = _navPage

    // Search Bar
    // Search Bar Events for Home Page
    private var _searchBarEventTextType: MutableStateFlow<SearchBarEventTextType> =
        MutableStateFlow(SearchBarEventTextType.All())
    val searchBarText: StateFlow<SearchBarEventTextType> = _searchBarEventTextType

    private var _showFilterBar = MutableStateFlow(false)
    val showFilterBar = _showFilterBar.asStateFlow()

    private var _searchMonth = MutableStateFlow(Klinder.getInstance().getMonth().monthInt)
    val searchMonth = _searchMonth.asStateFlow()

    private var _searchYearInt = MutableStateFlow(Klinder.getInstance().getYear().yearInt)
    val searchYearInt: StateFlow<Int> = _searchYearInt

    private var _dateDetailDate = MutableStateFlow(0)
    val dateDetailDate: StateFlow<Int> =  _dateDetailDate

    fun changeSearchBarState(state: SearchBarEventTextType){
        _searchBarEventTextType.update { state }
    }

    fun changeSearchMonth(month: Int){
        _searchMonth.update { month }
    }

    fun changeSearchYear(year: Int){
        _searchYearInt.update { year }
    }

    fun changeNavPage(page: NavRoutes){
        _navPage.update { page }
    }
    fun changeNavPage(page: String){
        _navPage.update {
            when(page){
                NavRoutes.HomePage.route -> { NavRoutes.HomePage}
                NavRoutes.CalendarPage.route -> NavRoutes.CalendarPage
                NavRoutes.FunctionPage.route -> NavRoutes.FunctionPage
                NavRoutes.AddEditPage.route -> NavRoutes.AddEditPage
                NavRoutes.CalendarDetailPage.route -> NavRoutes.CalendarDetailPage
                else -> NavRoutes.HomePage
            }
        }

    }

    fun selectedDate( date: Int){
        _dateDetailDate.update { date }
    }

    fun setFilterBarVisibility(show: Boolean){
        _showFilterBar.update { show }
    }

    // Search Bar Filter for Function View
    private var _functionViewPage = MutableStateFlow<FunctionViewPages>(FunctionViewPages.FunctionsPage)
    val functionViewPage = _functionViewPage.asStateFlow()

    fun changeFuncViewPage(page: FunctionViewPages){
        _functionViewPage.update { page }
    }

}

