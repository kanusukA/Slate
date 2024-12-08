package com.example.the_schedulaing_application.SharedViews

import com.example.the_schedulaing_application.domain.Cases.CaseType
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class AddEditSharedEvent (){

    private var _slateEventName = MutableStateFlow("")
    val slateEventName: StateFlow<String> = _slateEventName

    private var _slateEventDescription = MutableStateFlow("")
    val slateEventDescription: StateFlow<String> = _slateEventDescription

    private var _slateEventCaseType: MutableStateFlow<CaseType> =
        MutableStateFlow(CaseType.CaseSingleton(System.currentTimeMillis()))
    val slateEventCaseType: StateFlow<CaseType> = _slateEventCaseType

    fun setName(str: String){
        _slateEventName.update { str }
    }

    fun setDescription(str: String){
        _slateEventDescription.update { str }
    }

    fun setCaseType(case: CaseType){
        _slateEventCaseType.update { case }
    }

}