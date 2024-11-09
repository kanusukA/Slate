package com.example.the_schedulaing_application.element.Views.calendar


import android.animation.ValueAnimator
import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.DecayAnimation
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.snapTo
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.the_schedulaing_application.domain.Cases.CaseRepeatableType
import com.example.the_schedulaing_application.domain.Cases.CaseType
import com.example.the_schedulaing_application.domain.Cases.SlateEvent
import com.example.the_schedulaing_application.domain.kMonth
import com.example.the_schedulaing_application.element.Views.calendar.dateDetail.DateDetails
import com.example.the_schedulaing_application.element.components.eventMark.EventMark
import com.example.the_schedulaing_application.ui.theme.LexendFamily
import com.example.the_schedulaing_application.ui.theme.Pink20
import com.example.the_schedulaing_application.ui.theme.Yellow20
import com.example.the_schedulaing_application.ui.theme.eventBlue
import kotlinx.coroutines.delay
import kotlin.time.Duration

enum class CalDragAnchors {
    CENTER,
    TOP,
    BOTTOM
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CalenderView(
    viewModel: CalViewModel
) {

    // Local
    val density = LocalDensity.current

    // ViewModel
    val month by viewModel.month.collectAsStateWithLifecycle()
    val year by viewModel.year.collectAsStateWithLifecycle()
    val dateBoxData by viewModel.monthDateBoxData.collectAsStateWithLifecycle()
    val totalDateBoxes by viewModel.totalDateBoxes.collectAsStateWithLifecycle()
    val kMonths = viewModel.getKMonths()

    // View
    val monthScrollState = rememberLazyListState()
    var monthScrollVisibility: Boolean by remember {
        mutableStateOf(true)
    }
    var showDetailView by remember {
        mutableStateOf(false)
    }
    var selectedDate by remember {
        mutableStateOf(DateBoxData(27, emptyList()))
    }

    val swipeDraggable = remember {

        AnchoredDraggableState(
            initialValue = CalDragAnchors.CENTER,
            positionalThreshold = { totalDistance: Float -> totalDistance * 0.5f },
            velocityThreshold = { with(density) { 100.dp.toPx() } },
            decayAnimationSpec = splineBasedDecay(density),
            snapAnimationSpec = tween()
            ).apply {
            updateAnchors(
                DraggableAnchors {
                    CalDragAnchors.CENTER at 0f
                    CalDragAnchors.TOP at 400f
                    CalDragAnchors.BOTTOM at -400f
                }
            )
        }
    }

    LaunchedEffect(key1 = swipeDraggable.currentValue) {

        if (swipeDraggable.currentValue == CalDragAnchors.BOTTOM) {
            swipeDraggable.snapTo(CalDragAnchors.CENTER)
            viewModel.changeToNextMonth()
        } else if (swipeDraggable.currentValue == CalDragAnchors.TOP) {
            swipeDraggable.snapTo(CalDragAnchors.CENTER)
            viewModel.changeToPreviousMonth()
        }
        monthScrollState.animateScrollToItem(month.monthInt)
    }


    /*LaunchedEffect(key1 = monthScrollInteractionSource) {
        monthScrollInteractionSource.interactions.collect{interaction ->
            when(interaction){
               is DragInteraction.Start -> println("Drag me Daddy")
               is DragInteraction.Stop -> {
                   println("Yo")
                   if (monthScrollState.firstVisibleItemScrollOffset >
                       monthScrollState.layoutInfo.visibleItemsInfo[0].size / 4
                       &&
                       monthScrollState.layoutInfo.visibleItemsInfo.size > 1
                   ) {
                       println("This")
                       monthScrollState.animateScrollToItem(monthScrollState.firstVisibleItemIndex+1)
                   }else if(monthScrollState.firstVisibleItemScrollOffset <
                       monthScrollState.layoutInfo.visibleItemsInfo[0].size / 4){
                       println("That")
                       monthScrollState.animateScrollToItem(monthScrollState.firstVisibleItemIndex)
                   }

                   viewModel.changeToMonth(monthScrollState.firstVisibleItemIndex)

               }
            }
        }
    }*/

    LaunchedEffect(key1 = monthScrollState.isScrollInProgress) {

        if (!monthScrollState.isScrollInProgress && monthScrollState.firstVisibleItemScrollOffset != 0) {

                if (monthScrollState.firstVisibleItemScrollOffset >
                    monthScrollState.layoutInfo.visibleItemsInfo[0].size / 4
                    &&
                    monthScrollState.firstVisibleItemIndex + 1 <= 11
                        ) {
                    viewModel.changeToMonth(monthScrollState.firstVisibleItemIndex + 1)
                    monthScrollState.animateScrollToItem(monthScrollState.firstVisibleItemIndex + 1)
                } else if (monthScrollState.firstVisibleItemScrollOffset <
                    monthScrollState.layoutInfo.visibleItemsInfo[0].size / 4
                ) {
                    viewModel.changeToMonth(monthScrollState.firstVisibleItemIndex)
                    monthScrollState.animateScrollToItem(monthScrollState.firstVisibleItemIndex)
                }

        }
        if(!monthScrollState.isScrollInProgress && monthScrollState.firstVisibleItemIndex == 0 && month.monthInt != 0 ){
            viewModel.changeToMonth(0)
            monthScrollState.animateScrollToItem(0)
        }

    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        /*Column {

            repeat(7){
                Box(modifier = Modifier.height(90.dp),
                    contentAlignment = Alignment.BottomStart
                ){
                    Text(
                        modifier = Modifier,
                        text = viewModel.getWeekStr(it + 1),
                        //fontFamily = LexendFamily,
                        fontWeight = FontWeight.Black,
                        fontSize = 58.sp,
                        color = eventBlue.copy()
                    )
                }
            }
        }*/

        // Date Boxes
        Column(modifier = Modifier.fillMaxWidth()) {

            Text(
                modifier = Modifier.clickable {
                    viewModel.changeToMonth(1)
                },
                text = year.yearInt.toString(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                color = Pink20,

            )

            LazyRow(
                state = monthScrollState,
                ) {
                items(12) { index ->
                    val animatedTextColor = animateColorAsState(targetValue =
                        if(index==month.monthInt){
                            Pink20
                        }else if (monthScrollVisibility){
                            Pink20.copy(0.3f)
                        }else{
                            Color.Transparent
                        }
                    )
                    Text(
                        text = kMonths[index],
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Black,
                        color = animatedTextColor.value
                    )

                    if (index == 11) {
                        Spacer(modifier = Modifier.width(224.dp))
                    } else {
                        Spacer(modifier = Modifier.width(24.dp))
                    }

                }
            }

            // Lazy Grid is not updating Fix it, then work on the month bar visibility and interaction.
            AnimatedContent(targetState = showDetailView){show ->
                if(!show){
                    LazyHorizontalGrid(
                        modifier = Modifier
                            .height(600.dp)
                            .align(Alignment.CenterHorizontally)
                            .anchoredDraggable(
                                state = swipeDraggable,
                                orientation = Orientation.Vertical
                            ),
                        rows = GridCells.Fixed(7),
                        userScrollEnabled = false
                    ) {
                        items(
                            totalDateBoxes,
                        ) { index ->

                            if (index < month.firstDayInMonth) {
                                DateBox(
                                    dateBoxData = DateBoxData(0, emptyList()),
                                    emptyBox = true
                                )
                            } else {
                                DateBox(
                                    modifier = Modifier.clickable {
                                        selectedDate = dateBoxData.datesData[index - month.firstDayInMonth]
                                        showDetailView = true
                                    },
                                    dateBoxData = dateBoxData.datesData[index - month.firstDayInMonth],
                                    emptyBox = false
                                )
                            }
                        }
                    }
                }else{
                    DateDetails(dateBox = dateBoxData.datesData
                        , onInitDateBox = selectedDate
                    )
                }
            }
        }


    }

}


@Composable
fun DateBox(
    modifier: Modifier = Modifier,
    dateBoxData: DateBoxData,
    emptyBox: Boolean,
    tinyBox: Boolean = false,
) {

    val density = LocalDensity.current

    val boxHeightSize: Float = remember {
        with(density) { 80.dp.toPx() }
    }

    val boxSize by remember(tinyBox) {
        mutableStateOf(
            if (tinyBox) {
                70.dp
            } else {
                80.dp
            }
        )
    }

    val animatedBoxSize = animateDpAsState(targetValue = boxSize)

    Box(
        modifier = modifier
            .requiredSize(75.dp, animatedBoxSize.value)
            .padding(
                top = 2.dp,
                start = 2.dp,
                bottom = 2.dp
            )
            .clip(shape = MaterialTheme.shapes.large)
            .background(Yellow20)

    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 2.dp)
        ) {
            if (!emptyBox) {
                Text(
                    dateBoxData.date.toString(),
                    fontSize = 28.sp,
                    fontFamily = LexendFamily,
                    fontWeight = FontWeight.Bold,
                    color = Pink20
                )

                if (dateBoxData.events.isNotEmpty()) {

                    AnimatedVisibility(
                        visible = !tinyBox,
                        enter = slideInVertically(
                            animationSpec = tween(700)
                        ) {
                            boxHeightSize.toInt()
                        },
                        exit = slideOutVertically(
                            animationSpec = tween(700)
                        ) {
                            boxHeightSize.toInt()
                        },

                        ) {
                        Column(
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            repeat(dateBoxData.events.size) {
                                EventMark(event = dateBoxData.events[it], false)
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
    val calViewModel = CalViewModel()
    val event1 = SlateEvent(
        "Repeat",
        "This is an example description",
        caseType = CaseType.CaseRepeatable(
            CaseRepeatableType.YearlyEvent(
                27, 9
            )
        )
    )
    val event2 = SlateEvent(
        "This is an Example",
        "This is an  example description",
        caseType = CaseType.CaseSingleton(
            1732645800000
        )
    )
    CalenderView(
        viewModel =
        calViewModel
    )
}