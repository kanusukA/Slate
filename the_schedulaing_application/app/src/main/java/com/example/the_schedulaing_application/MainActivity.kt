package com.example.the_schedulaing_application

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.the_schedulaing_application.data.conversions.fromRealmEventToSlateEvent
import com.example.the_schedulaing_application.data.conversions.fromSlateEventToRealmEvent
import com.example.the_schedulaing_application.data.viewModels.MainRealmViewModel
import com.example.the_schedulaing_application.domain.Cases.CaseRepeatableType
import com.example.the_schedulaing_application.domain.Cases.CaseType
import com.example.the_schedulaing_application.domain.Cases.SlateEvent
import com.example.the_schedulaing_application.element.Views.AddCreateView.AddEditViewModel
import com.example.the_schedulaing_application.element.Views.AddCreateView.AddEventView
import com.example.the_schedulaing_application.element.components.eventMark.EventBox
import com.example.the_schedulaing_application.ui.theme.The_schedulaing_applicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val mainRealmViewModel: MainRealmViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            The_schedulaing_applicationTheme {
                val savedSlateEvents = mainRealmViewModel.events.collectAsStateWithLifecycle()
                /*LaunchedEffect(key1 = true) {
                    val event = SlateEvent(
                        "this",
                        "IsIt",
                        caseType = CaseType.CaseRepeatable(CaseRepeatableType.YearlyEvent(
                            27,11
                        ))
                    )
                    val realmEvent = fromSlateEventToRealmEvent(event,event.caseType.value)
                    println("Realm Event ${realmEvent.name} ${realmEvent.description}")
                    println("Case Type ${realmEvent.caseType}")
                    when(realmEvent.caseType){
                        0 -> println(realmEvent.caseSingleton?.epochTimeMilli)
                        1 -> {
                            println(realmEvent.caseDuration?.fromEpoch)
                            println(realmEvent.caseDuration?.toEpoch)
                        }
                        2 -> println(realmEvent.caseDaily?.timeOfDay)
                        3 -> println(realmEvent.caseWeekly?.weeks?.toList())
                        4 -> println(realmEvent.caseMonthly?.days?.toList())
                        5 -> println(realmEvent.caseYearly?.days?.toList())
                        6 -> {
                            println(realmEvent.caseYearlyEvent?.date)
                            println(realmEvent.caseYearlyEvent?.month)
                        }
                        else -> println("error")
                    }

                    val slateEvent = fromRealmEventToSlateEvent(realmEvent)
                    println(slateEvent.eventName)
                    println(slateEvent.eventDescription)
                    println(slateEvent.caseType)

                }*/
                // Main Background Surface
                Surface {
                    Column {

                        AddEventView(viewModel = AddEditViewModel(mainRealmViewModel))
                        LazyColumn {
                            items(savedSlateEvents.value){slateEvent ->
                                EventBox(event = slateEvent)
                            }
                        }
                    }


                }

            }
        }
    }
}






