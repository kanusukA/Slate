package com.example.the_schedulaing_application.data.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.the_schedulaing_application.SlateApplication
import com.example.the_schedulaing_application.data.conversions.fromRealmEventToSlateEvent
import com.example.the_schedulaing_application.data.conversions.fromSlateEventToRealmEvent
import com.example.the_schedulaing_application.data.objects.Event
import com.example.the_schedulaing_application.domain.Cases.SlateEvent
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainRealmViewModel :
    ViewModel() {

    private val realm = SlateApplication.realm

    val events = realm
        .query<Event>()
        .asFlow()
        .map {result ->
            result.list.map {
                fromRealmEventToSlateEvent(it)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )

    fun insertEvent(event: SlateEvent) = viewModelScope.launch {
        realm.write {
            val realmEvent = fromSlateEventToRealmEvent(event,event.caseType.value)
            copyToRealm(realmEvent, updatePolicy = UpdatePolicy.ALL)
        }
    }

}