package com.example.the_schedulaing_application.element.Views.calendar.dateDetail

import android.graphics.Paint.Align
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.layout.positionOnScreen
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.the_schedulaing_application.Shapes.CalEventColumnShape.shapeAnimation.EventColumnShapeAnimation
import com.example.the_schedulaing_application.custom.ScaleIndication
import com.example.the_schedulaing_application.domain.Cases.CaseRepeatableType
import com.example.the_schedulaing_application.domain.Cases.CaseType
import com.example.the_schedulaing_application.domain.Cases.SlateEvent
import com.example.the_schedulaing_application.domain.Klinder
import com.example.the_schedulaing_application.domain.kMonth
import com.example.the_schedulaing_application.element.Views.calendar.CalViewModel
import com.example.the_schedulaing_application.element.Views.calendar.DateBox
import com.example.the_schedulaing_application.element.Views.calendar.DateBoxData
import com.example.the_schedulaing_application.element.components.eventMark.EventBox
import com.example.the_schedulaing_application.ui.theme.Pink20
import com.example.the_schedulaing_application.ui.theme.SlateColorScheme
import com.example.the_schedulaing_application.ui.theme.Yellow20
import java.util.Calendar


@Composable
fun DateDetails(
    modifier: Modifier = Modifier,
) {

    val viewModel = hiltViewModel<CalViewModel>()

    val events by viewModel.monthEvents.collectAsStateWithLifecycle(initialValue = emptyList())
    val selectedDate by viewModel.navConductorViewModel.dateDetailDate.collectAsStateWithLifecycle()
    val month by viewModel.navConductorViewModel.searchMonth.collectAsStateWithLifecycle()
    val year by viewModel.navConductorViewModel.searchYearInt.collectAsStateWithLifecycle()

    val selectedMonth by remember(month, year) {
        mutableStateOf(Klinder.getInstance().getMonth(month, year))
    }

    var selectedDateBoxPos by remember {
        mutableFloatStateOf(0f)
    }



    val listState = rememberLazyListState()

    LaunchedEffect(key1 = true) {
        listState.animateScrollToItem(selectedDate - 1)
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {

        LazyRow(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            verticalAlignment = Alignment.Bottom,
            state = listState,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = events,
                key = {items -> items.date}
            ) { event ->

                val itemOffsetY by remember(selectedDate) {
                    mutableStateOf(
                        if (selectedDate == event.date) {
                            20.dp
                        } else {
                            0.dp
                        }
                    )
                }

                val animatedItemOffsetY = animateDpAsState(targetValue = itemOffsetY)

                DateBox(
                    modifier = Modifier
                        .offset(y = animatedItemOffsetY.value)
                        .requiredSize(72.dp, 90.dp)
                        .onGloballyPositioned {
                            if (event.date == selectedDate) {
                                selectedDateBoxPos = it.positionInWindow().x
                            }
                        }
                        .clickable(
                            interactionSource = null,
                            indication = null
                        ) {
                            if (selectedDate != event.date) {
                                viewModel.navConductorViewModel.selectedDate(event.date)
                            }
                        },
                    event,
                    selected = event.date == selectedDate
                )
            }

            item {
                Spacer(modifier = Modifier.width(24.dp))
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        val eventColumnShapeAnimation = EventColumnShapeAnimation().animationShapeAsState(
            targetPos = selectedDateBoxPos,
            indentCornerRadius = 40.dp,
            indentWidth = 92.dp,
            boxCornerRadius = 40.dp,
            selected = true,
            startPadding = 0.dp,
            itemNotVisible = { TODO() }
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 400.dp)
                .background(
                    SlateColorScheme.surfaceContainerLow,
                    shape = eventColumnShapeAnimation.value
                )
        ) {

            // FIx Transitions and Next date time
            if(events.size >= selectedDate){
                AnimatedContent(
                    targetState = selectedDate,
                    transitionSpec = {
                        fadeIn(tween(300)) + slideInVertically { -it } togetherWith
                                fadeOut(tween(300)) + slideOutVertically { -it }
                    }
                ) { selDate ->
                    LazyColumn(
                        modifier = Modifier,
                        contentPadding = PaddingValues(12.dp)
                    ) {
                        items(events[selDate].events) { event ->
                            if (selDate == event.getNextTime().date) {

                                EventBox(
                                    event = event,
                                    onDeleteEvent = {},
                                    onEditEvent = {}
                                )

                                Spacer(modifier = Modifier.height(6.dp))
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
fun PreviewDateDetails() {
    DateDetails()
}