package com.example.the_schedulaing_application.element.Views.calendar



import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.the_schedulaing_application.domain.Cases.CaseRepeatableType
import com.example.the_schedulaing_application.domain.Cases.CaseType
import com.example.the_schedulaing_application.domain.Cases.SlateEvent
import com.example.the_schedulaing_application.domain.Klinder
import com.example.the_schedulaing_application.domain.kMonth
import com.example.the_schedulaing_application.element.Navigation.NavRoutes
import com.example.the_schedulaing_application.element.Views.calendar.dateDetail.DateDetails
import com.example.the_schedulaing_application.element.components.eventMark.EventMark
import com.example.the_schedulaing_application.ui.theme.LexendFamily
import com.example.the_schedulaing_application.ui.theme.Pink20
import com.example.the_schedulaing_application.ui.theme.SlateColorScheme
import com.example.the_schedulaing_application.ui.theme.Yellow20
import com.example.the_schedulaing_application.ui.theme.eventBlue
import kotlinx.coroutines.delay
import kotlin.time.Duration

enum class CalDragAnchors {
    CENTER,
    TOP,
    BOTTOM
}
// Date Detail view
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CalenderView(
    modifier: Modifier = Modifier,
    navController: NavController
) {

    val viewModel = hiltViewModel<CalViewModel>()

    // ViewModel
    val month by viewModel.navConductorViewModel.searchMonth.collectAsStateWithLifecycle()
    val year by viewModel.navConductorViewModel.searchYearInt.collectAsStateWithLifecycle()

    val stateMonth by  remember(month) {
        mutableStateOf(Klinder.getInstance().getMonth(month,year))
    }

    val events by viewModel.monthEvents.collectAsStateWithLifecycle()


    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Lazy Grid is not updating Fix it, then work on the month bar visibility and interaction.
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            userScrollEnabled = false,
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Faulty
            items(stateMonth.firstDayInMonth) { index ->
                DateBox(
                    modifier = Modifier
                        .requiredSize(52.dp, 82.dp),
                    dateBoxObj = DateBoxObj(0, "")
                )
            }

            items(
                items = events,
                key = { item -> item.date }
            ) { event ->

                DateBox(
                    modifier = Modifier
                        .requiredSize(52.dp, 82.dp)
                        .clickable {
                            viewModel.navConductorViewModel.selectedDate(event.date)
                            navController.navigate(NavRoutes.CalendarDetailPage.route)
                        },
                    event
                )

            }
        }
    }




}

data class DateBoxObj(
    val date: Int,
    val dateStr: String,
    val events: List<SlateEvent> = emptyList()
)

@Composable
fun DateBox(
    modifier: Modifier = Modifier,
    dateBoxObj: DateBoxObj,
    fontSize: TextUnit = 18.sp,
    selected: Boolean = false, // Hides Event Marks | Used for date Detail view
) {

    Box(
        modifier = modifier
            .clip(shape = MaterialTheme.shapes.large)
            .background(SlateColorScheme.surfaceContainerLow)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp)
        ) {

            Text(
                dateBoxObj.dateStr,
                fontSize = fontSize,
                fontFamily = LexendFamily,
                fontWeight = FontWeight.Bold,
                color = SlateColorScheme.onSurface
            )

            androidx.compose.animation.AnimatedVisibility(
                modifier = Modifier.align(Alignment.End),
                visible = !selected,
                enter = slideInVertically { it * 2 },
                exit = slideOutVertically { it * 2 }
            ){
                Spacer(modifier = Modifier.height(4.dp))

                if (dateBoxObj.events.isNotEmpty()) {
                    Column(
                        horizontalAlignment = Alignment.End
                    ) {
                        dateBoxObj.events.forEach {
                            if (dateBoxObj.date == it.getNextTime().date) {
                                EventMark(
                                    event = it,
                                    collapse = false
                                )
                            }
                        }
                    }

                }
            }

        }

    }
}

@Preview
@Composable
fun previewCalender() {
    //CalenderView()
}