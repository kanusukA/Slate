package com.example.the_schedulaing_application.element.BottomControlBar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.the_schedulaing_application.SharedViews.AddEditSharedEvent
import com.example.the_schedulaing_application.data.viewModels.MainRealmViewModel
import com.example.the_schedulaing_application.domain.Cases.CaseType
import com.example.the_schedulaing_application.domain.Cases.SlateEvent
import com.example.the_schedulaing_application.element.Navigation.NavConductorViewModel
import com.example.the_schedulaing_application.element.Navigation.NavRoutes
import com.example.the_schedulaing_application.element.TopSearchBar.SearchBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class BottomNavControlBarViewModel @Inject constructor(
    private val realmViewModel: MainRealmViewModel,
    private val addEditSharedEvent: AddEditSharedEvent,
    private val navConductorViewModel: NavConductorViewModel
): ViewModel(){

    fun changeBottomNavBarPage(page: NavRoutes){
        navConductorViewModel.changeNavPage(page)
    }

    fun onClickCancel(){
        navConductorViewModel.changeNavPage(NavRoutes.HomePage)
        addEditSharedEvent.setCaseType(CaseType.CaseSingleton(System.currentTimeMillis()))
        addEditSharedEvent.setName("")
        addEditSharedEvent.setDescription("")
    }

    fun onClickSave(){
        realmViewModel.insertEvent(
            SlateEvent(
                id = addEditSharedEvent.id,
                name = addEditSharedEvent.slateEventName.value,
                description = addEditSharedEvent.slateEventDescription.value,
                inputCaseType = addEditSharedEvent.slateEventCaseType.value
            )
        )
        onClickCancel()
    }

}
