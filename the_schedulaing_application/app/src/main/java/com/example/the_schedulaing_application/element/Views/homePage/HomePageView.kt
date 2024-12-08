package com.example.the_schedulaing_application.element.Views.homePage

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.the_schedulaing_application.domain.Cases.CaseRepeatableType
import com.example.the_schedulaing_application.domain.Cases.CaseType
import com.example.the_schedulaing_application.domain.Cases.SlateWeeks
import com.example.the_schedulaing_application.domain.Cases.getDateStringFromKTime
import com.example.the_schedulaing_application.domain.Klinder
import com.example.the_schedulaing_application.domain.getKClock
import com.example.the_schedulaing_application.element.components.eventMark.EventBox
import com.example.the_schedulaing_application.element.components.eventMark.EventBoxDaily
import com.example.the_schedulaing_application.element.components.eventMark.EventBoxDuration
import com.example.the_schedulaing_application.element.components.eventMark.EventBoxMonthly
import com.example.the_schedulaing_application.element.components.eventMark.EventBoxSingleton
import com.example.the_schedulaing_application.element.components.eventMark.EventBoxWeekly
import com.example.the_schedulaing_application.element.components.eventMark.EventBoxYearly
import com.example.the_schedulaing_application.element.components.eventMark.EventBoxYearlyEvent
import com.example.the_schedulaing_application.ui.theme.LexendFamily
import com.example.the_schedulaing_application.ui.theme.SlateColorScheme

@Composable
fun HomePageView(
    modifier: Modifier = Modifier
) {
    val homepage = hiltViewModel<HomePageViewModel>()

    val realmEvents by homepage.events.collectAsStateWithLifecycle(initialValue = emptyList())
    val showingFilterBar by homepage.filterBarState.collectAsStateWithLifecycle()

    val topPadding by remember(showingFilterBar){
        mutableStateOf(
            if(showingFilterBar){
                160.dp
            }else{
                100.dp
            }
        )
    }

    val animatedTopPadding = animateDpAsState(targetValue = topPadding)

    if (realmEvents.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "No Events",
                fontFamily = LexendFamily,
                fontSize = 48.sp,
                fontWeight = FontWeight.Black,
                color = SlateColorScheme.onSurface.copy(alpha = 0.2f)
            )
        }
    } else {
        // Infinite Column height fix it

            LazyColumn(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                item {
                    Spacer(modifier = Modifier.height(animatedTopPadding.value))
                }

                items(realmEvents.size) { index ->
                    val event = realmEvents[index]

                    EventBox(event = event,
                        onDeleteEvent = { homepage.onEventDelete(event) },
                        onEditEvent = {}
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(100.dp))
                }

            }

    }

}


@Preview
@Composable
fun PreviewHomePageView() {
    HomePageView()
}