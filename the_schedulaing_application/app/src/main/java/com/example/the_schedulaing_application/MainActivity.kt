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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.the_schedulaing_application.data.conversions.fromRealmEventToSlateEvent
import com.example.the_schedulaing_application.data.conversions.fromSlateEventToRealmEvent
import com.example.the_schedulaing_application.data.fb.GoogleSignInClient
import com.example.the_schedulaing_application.data.viewModels.MainRealmViewModel
import com.example.the_schedulaing_application.domain.Cases.CaseRepeatableType
import com.example.the_schedulaing_application.domain.Cases.CaseType
import com.example.the_schedulaing_application.domain.Cases.SlateEvent
import com.example.the_schedulaing_application.domain.Cases.SlateWeeks
import com.example.the_schedulaing_application.domain.Klinder
import com.example.the_schedulaing_application.element.Navigation.NavConductor
import com.example.the_schedulaing_application.element.Views.AddCreateView.AddEditViewModel
import com.example.the_schedulaing_application.element.Views.AddCreateView.AddEventView
import com.example.the_schedulaing_application.element.components.eventMark.EventBox
import com.example.the_schedulaing_application.element.components.eventMark.EventBoxDaily
import com.example.the_schedulaing_application.element.components.eventMark.EventBoxDuration
import com.example.the_schedulaing_application.element.components.eventMark.EventBoxMonthly
import com.example.the_schedulaing_application.element.components.eventMark.EventBoxSingleton
import com.example.the_schedulaing_application.element.components.eventMark.EventBoxWeekly
import com.example.the_schedulaing_application.element.components.eventMark.EventBoxYearly
import com.example.the_schedulaing_application.ui.theme.The_schedulaing_applicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            The_schedulaing_applicationTheme {
                val googleAuthUiClient = GoogleSignInClient(LocalContext.current)

                NavConductor(googleAuthUiClient)

            }
        }
    }
}






