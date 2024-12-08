package com.example.the_schedulaing_application.data.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.the_schedulaing_application.SlateApplication
import com.example.the_schedulaing_application.data.conversions.fromRealmEventToSlateEvent
import com.example.the_schedulaing_application.data.conversions.fromSlateEventToRealmEvent
import com.example.the_schedulaing_application.data.objects.Event
import com.example.the_schedulaing_application.domain.Cases.SlateEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainRealmViewModel @Inject constructor(

): ViewModel() {

    private val realm = SlateApplication.realm

    val slateEvents = realm
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

    val realmEvents = realm
        .query<Event>()
        .asFlow()
        .map {
            it.list.toList()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )






    fun insertEvent(event: SlateEvent) = viewModelScope.launch {
        realm.write {
            val realmEvent = fromSlateEventToRealmEvent(event,event.caseType)
            copyToRealm(realmEvent, updatePolicy = UpdatePolicy.ALL)
        }
    }

    fun deleteEvent(event: SlateEvent) {
        viewModelScope.launch{
            realm.write {
                val realmEvent = realm.query<Event>("id == $0",event.id).first().find() ?: return@write
                val latestEvent = findLatest(realmEvent) ?: return@write
                delete(latestEvent)
            }
        }
    }

}