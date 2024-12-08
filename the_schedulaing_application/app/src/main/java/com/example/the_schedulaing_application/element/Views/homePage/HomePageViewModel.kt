package com.example.the_schedulaing_application.element.Views.homePage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.the_schedulaing_application.data.conversions.fromRealmEventToSlateEvent
import com.example.the_schedulaing_application.data.viewModels.MainRealmViewModel
import com.example.the_schedulaing_application.domain.Cases.SlateEvent
import com.example.the_schedulaing_application.element.Navigation.NavConductorViewModel
import com.example.the_schedulaing_application.element.TopSearchBar.SearchBarEventTextType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomePageViewModel @Inject constructor(
    private val realmViewModel: MainRealmViewModel,
    private val navConductorViewModel: NavConductorViewModel
) : ViewModel() {

    val filterBarState = navConductorViewModel.showFilterBar

    val events =
        navConductorViewModel.searchBarText.combine(realmViewModel.realmEvents) { search, events ->
            fromRealmEventToSlateEvent(
                when (search) {
                is SearchBarEventTextType.Daily -> events.filter { it.caseType == 2 }
                is SearchBarEventTextType.Duration -> events.filter { it.caseType == 1 }
                is SearchBarEventTextType.Monthly -> events.filter { it.caseType == 4 }
                is SearchBarEventTextType.Repeating -> events.filter { it.caseType == 2 }
                is SearchBarEventTextType.Searching -> events.filter { it.name.contains(search.str) }
                is SearchBarEventTextType.Singleton -> events.filter { it.caseType == 0 }
                is SearchBarEventTextType.Weekly -> events.filter { it.caseType == 3 }
                is SearchBarEventTextType.Yearly -> events.filter { it.caseType == 5 }
                is SearchBarEventTextType.YearlyEvents -> events.filter { it.caseType == 6 }
                else -> events
            }
            )
        }


    fun onEventDelete(event: SlateEvent) {
        realmViewModel.deleteEvent(event)
    }

}