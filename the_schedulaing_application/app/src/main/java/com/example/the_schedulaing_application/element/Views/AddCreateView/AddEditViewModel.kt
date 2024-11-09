package com.example.the_schedulaing_application.element.Views.AddCreateView

import androidx.lifecycle.ViewModel
import com.example.the_schedulaing_application.data.viewModels.MainRealmViewModel
import com.example.the_schedulaing_application.domain.Cases.CaseRepeatableType
import com.example.the_schedulaing_application.domain.Cases.CaseType
import com.example.the_schedulaing_application.domain.Cases.SlateEvent
import com.example.the_schedulaing_application.domain.Klinder
import com.example.the_schedulaing_application.element.components.caseTypeComponents.CaseDurationComponent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class AddEditViewModel(
    private val realmViewModel: MainRealmViewModel
) : ViewModel() {

    private var _eventName = MutableStateFlow("")
    val eventName: StateFlow<String> = _eventName

    private var _eventDescription = MutableStateFlow("")
    val eventDescription: StateFlow<String> = _eventDescription

    private var _eventCaseType: MutableStateFlow<CaseType> =
        MutableStateFlow(CaseType.CaseSingleton(System.currentTimeMillis()))
    val eventCaseType: StateFlow<CaseType> = _eventCaseType


    fun setEventName(name: String) {
        _eventName.update { name }
    }

    fun setEventDescription(description: String) {
        _eventDescription.update { description }
    }

    fun setEventCaseType(caseType: CaseType) {
        _eventCaseType.update { caseType }
    }

    fun setEventCaseType(int: Int) {
        _eventCaseType.update {
            when (int) {
                0 -> {
                    CaseType.CaseSingleton(System.currentTimeMillis())
                }

                1 -> {
                    CaseType.CaseDuration(System.currentTimeMillis(), System.currentTimeMillis())
                }

                2 -> {
                    CaseType.CaseRepeatable(
                        CaseRepeatableType.Daily(Klinder.getInstance().getTimeOfDayLong())
                    )
                }

                else -> {
                    CaseType.CaseSingleton(System.currentTimeMillis())
                }
            }
        }
    }

    fun setEventCaseRepeatableType(int: Int) {
        _eventCaseType.update {
            when (int) {
                0 -> {
                    CaseType.CaseRepeatable(
                        CaseRepeatableType.Daily(Klinder.getInstance().getTimeOfDayLong())
                    )
                }

                1 -> {
                    CaseType.CaseRepeatable(
                        CaseRepeatableType.Weekly(emptyList())
                    )
                }

                2 -> {
                    CaseType.CaseRepeatable(
                        CaseRepeatableType.Monthly(emptyList())
                    )
                }

                3 -> {
                    CaseType.CaseRepeatable(
                        CaseRepeatableType.Yearly(emptyList(), emptyList())
                    )
                }

                4 -> {
                    CaseType.CaseRepeatable(
                        CaseRepeatableType.YearlyEvent(1, 1)
                    )
                }

                else -> {
                    CaseType.CaseRepeatable(
                        CaseRepeatableType.Daily(Klinder.getInstance().getTimeOfDayLong())
                    )
                }
            }
        }
    }

    fun resetValues(){
        _eventName.update { "" }
        _eventDescription.update { "" }
        _eventCaseType.update { CaseType.CaseSingleton(System.currentTimeMillis()) }
    }

    fun setValuesToDataBase() {
        realmViewModel.insertEvent(
            SlateEvent(
                eventName.value,
                eventDescription.value,
                eventCaseType.value
            )
        )
        resetValues()
    }

}