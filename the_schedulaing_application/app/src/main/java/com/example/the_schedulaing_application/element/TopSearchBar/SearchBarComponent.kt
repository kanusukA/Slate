package com.example.the_schedulaing_application.element.TopSearchBar

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.snapTo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.the_schedulaing_application.R
import com.example.the_schedulaing_application.custom.ScaleIndication
import com.example.the_schedulaing_application.custom.ScaleWithCircleIndication
import com.example.the_schedulaing_application.domain.Cases.CaseType
import com.example.the_schedulaing_application.domain.Klinder
import com.example.the_schedulaing_application.element.Navigation.FunctionViewPages
import com.example.the_schedulaing_application.element.Views.calendar.CalDragAnchors
import com.example.the_schedulaing_application.ui.theme.LexendFamily
import com.example.the_schedulaing_application.ui.theme.SlateColorScheme


@Composable
fun SearchBarComponent(
    modifier: Modifier = Modifier
) {

    val viewModel = hiltViewModel<SearchBarViewModel>()

    val textType by viewModel.navConductorViewModel.searchBarText.collectAsStateWithLifecycle()
    val showSearchBar by viewModel.showSearchBar.collectAsStateWithLifecycle()
    val searchBarState by viewModel.searchBarState.collectAsStateWithLifecycle()
    val searchBarMonthInt by viewModel.navConductorViewModel.searchMonth.collectAsStateWithLifecycle()
    val showFilterBar by viewModel.navConductorViewModel.showFilterBar.collectAsStateWithLifecycle()
    val functionViewPage by viewModel.navConductorViewModel.functionViewPage.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = searchBarState) {
        when (searchBarState) {
            SearchBarState.CALENDAR_PAGE -> {
                viewModel.showFilterBar(false)
            }

            else -> {}
        }
    }


    val animatedFilterPosition = animateDpAsState(
        targetValue = if (showFilterBar) {
            92.dp
        } else {
            24.dp
        },
        animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow)
    )

    // Filter Bar
    AnimatedVisibility(
        visible = showFilterBar,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Surface(
            modifier = Modifier.padding(
                top = animatedFilterPosition.value,
                start = 24.dp,
                end = 24.dp
            ),
            color = Color.Transparent,
            shape = RoundedCornerShape(28.dp),
            shadowElevation = 6.dp
        ) {

            if(searchBarState == SearchBarState.FUNCTION_PAGE){
                FuncViewFilterBar(
                  modifier = Modifier,
                    selectedFunctionPage = functionViewPage,
                    onChangeFuncViewPage = { viewModel.onChangeFunctionPage(it) }
                )
            } else{
                TestSearchFilterBar(
                    modifier = Modifier,
                    textType,
                    onChangeEventTextType = { viewModel.changeSearchEventTextType(it) }
                )
            }

        }
    }

    // Search Bar Background
    Box(
        modifier = modifier
            .height(160.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White,
                        Color.Transparent
                    )
                )
            )
            .padding(top = 24.dp, start = 12.dp, end = 12.dp),
        contentAlignment = Alignment.TopCenter,
    ) {

        // Calendar Buttons
        Box(
            modifier = Modifier
                .height(58.dp)
                .shadow(4.dp, RoundedCornerShape(24.dp))
                .clip(RoundedCornerShape(24.dp))
        ) {

            // Main Search Bar Row
            Row(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .background(
                        SlateColorScheme.surfaceContainerHigh,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .clip(RoundedCornerShape(24.dp)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                // Search Text Field and Text
                AnimatedContent(targetState = showSearchBar, label = "") { searchState ->
                    if (searchState) {

                        TextField(
                            modifier = Modifier.fillMaxWidth(0.7f),
                            value = textType.text,
                            onValueChange = {
                                viewModel.changeSearchEventTextType(
                                    SearchBarEventTextType.Searching(
                                        it
                                    )
                                )
                            },
                            colors = TextFieldDefaults.colors().copy(
                                focusedContainerColor = SlateColorScheme.surfaceContainerHigh,
                                unfocusedContainerColor = SlateColorScheme.surfaceContainerHigh,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            textStyle = TextStyle(
                                fontFamily = LexendFamily,
                                fontSize = variableTextSize(24, textType.text.length),
                                fontWeight = FontWeight.Black,
                                color = SlateColorScheme.onSurface
                            ),
                            maxLines = 1,
                            placeholder = {
                                Text(
                                    text = "Search Events",
                                    fontFamily = LexendFamily,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Black,
                                    color = SlateColorScheme.onSurface
                                )
                            }
                        )

                    } else {
                        when (searchBarState) {
                            SearchBarState.HOME_PAGE -> {
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth(0.7f)
                                        .padding(start = 24.dp),
                                    text = textType.text,
                                    fontFamily = LexendFamily,
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Black,
                                    color = SlateColorScheme.onSurface
                                )
                            }

                            SearchBarState.CALENDAR_PAGE -> {
                                ScrollableMonths(
                                    selectedMonth = searchBarMonthInt,
                                    onChangeMonth = { viewModel.changeMonth(it) }
                                )
                            }

                            SearchBarState.FUNCTION_PAGE -> {
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth(0.7f)
                                        .padding(start = 24.dp),
                                    text = "All Functions",
                                    fontFamily = LexendFamily,
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Black,
                                    color = SlateColorScheme.onSurface
                                )
                            }

                            else -> {}
                        }
                    }
                }

                // Right Side Buttons
                Row(
                    modifier = Modifier.background(
                        color = SlateColorScheme.secondaryContainer,
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(48.dp)
                            .clip(RectangleShape)
                            .clickable(
                                interactionSource = null,
                                indication = ScaleWithCircleIndication
                            ) {
                                if (showSearchBar) {
                                    viewModel.changeSearchEventTextType(SearchBarEventTextType.All())
                                } else {
                                    viewModel.changeSearchEventTextType(
                                        SearchBarEventTextType.Searching(
                                            ""
                                        )
                                    )
                                    viewModel.showFilterBar(false)
                                }

                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (showSearchBar && searchBarState == SearchBarState.HOME_PAGE) {
                            Image(
                                painter = painterResource(id = R.drawable.cross_icon_24px),
                                colorFilter = ColorFilter.tint(Color.Black),
                                contentDescription = ""
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.search_icon_24px),
                                colorFilter = ColorFilter.tint(Color.Black),
                                contentDescription = ""
                            )
                        }
                    }

                    VerticalDivider(modifier = Modifier.padding(vertical = 4.dp))

                    // function arrow rotation
                    val funcArrowPos = animateFloatAsState(if(showFilterBar){90f}else{-90f})

                    AnimatedVisibility(visible = searchBarState == SearchBarState.HOME_PAGE || searchBarState == SearchBarState.FUNCTION_PAGE) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(48.dp)
                                .clip(RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp))
                                .clickable (
                                    interactionSource = null,
                                    indication = ScaleWithCircleIndication
                                )
                                {
                                    viewModel.showFilterBar(!showFilterBar && (searchBarState == SearchBarState.HOME_PAGE || searchBarState == SearchBarState.FUNCTION_PAGE))

                                },
                            contentAlignment = Alignment.Center
                        ) {

                            AnimatedContent(targetState = searchBarState){state ->
                                when(state){
                                    SearchBarState.HOME_PAGE -> {
                                        Image(
                                            painter = painterResource(id = R.drawable.filter_icon_24px),
                                            colorFilter = ColorFilter.tint(Color.Black),
                                            contentDescription = "Filter"
                                        )
                                    }
                                    SearchBarState.FUNCTION_PAGE -> {
                                        Image(
                                            modifier = Modifier.rotate(funcArrowPos.value),
                                            painter = painterResource(id = R.drawable.previous_icon_24px),
                                            colorFilter = ColorFilter.tint(Color.Black),
                                            contentDescription = "Function Pages"
                                        )
                                    }
                                    else -> {}
                                }
                            }

                        }
                    }
                }

            }

            AnimatedVisibility(
                modifier = Modifier.align(Alignment.CenterStart),
                visible = searchBarState == SearchBarState.CALENDAR_PAGE,
                enter = slideInHorizontally { -it },
                exit = slideOutHorizontally { -it }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(48.dp)
                        .background(SlateColorScheme.secondaryContainer)
                        .clip(RoundedCornerShape(topStart = 24.dp, bottomStart = 24.dp))
                        .clickable(
                            interactionSource = null,
                            indication = ScaleWithCircleIndication
                        ) { viewModel.previousMonth() },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.previous_icon_24px),
                        colorFilter = ColorFilter.tint(Color.Black),
                        contentDescription = ""
                    )
                }
            }

            AnimatedVisibility(
                modifier = Modifier.align(Alignment.CenterEnd),
                visible = searchBarState == SearchBarState.CALENDAR_PAGE,
                enter = slideInHorizontally { it },
                exit = slideOutHorizontally { it }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(48.dp)
                        .background(SlateColorScheme.secondaryContainer)
                        .clip(RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp))
                        .clickable(
                            interactionSource = null,
                            indication = ScaleWithCircleIndication
                        ) { viewModel.nextMonth() },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.next_icon_24px),
                        colorFilter = ColorFilter.tint(Color.Black),
                        contentDescription = ""
                    )
                }
            }
        }

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ScrollableMonths(
    selectedMonth: Int,
    onChangeMonth: (Int) -> Unit
) {

    val density = LocalDensity.current

    val monthScrollState = rememberLazyListState()

    LaunchedEffect(key1 = selectedMonth) {
        monthScrollState.animateScrollToItem(selectedMonth)
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

    LaunchedEffect(key1 = monthScrollState.isScrollInProgress) {

        if (!monthScrollState.isScrollInProgress && monthScrollState.firstVisibleItemScrollOffset != 0) {

            if (monthScrollState.firstVisibleItemScrollOffset >
                monthScrollState.layoutInfo.visibleItemsInfo[0].size / 4
                &&
                monthScrollState.firstVisibleItemIndex + 1 <= 11
            ) {
                onChangeMonth(monthScrollState.firstVisibleItemIndex + 1)
                monthScrollState.animateScrollToItem(monthScrollState.firstVisibleItemIndex + 1)
            } else if (monthScrollState.firstVisibleItemScrollOffset <
                monthScrollState.layoutInfo.visibleItemsInfo[0].size / 4
            ) {
                onChangeMonth(monthScrollState.firstVisibleItemIndex)
                monthScrollState.animateScrollToItem(monthScrollState.firstVisibleItemIndex)
            }

        }
        if (!monthScrollState.isScrollInProgress && monthScrollState.firstVisibleItemIndex == 0 && selectedMonth != 0) {
            onChangeMonth(0)
            monthScrollState.animateScrollToItem(0)
        }

    }

    LazyRow(
        modifier = Modifier.anchoredDraggable(
            swipeDraggable,
            Orientation.Horizontal
        ),
        horizontalArrangement = Arrangement.SpaceEvenly,
        state = monthScrollState,

        ) {
        items(Klinder.getInstance().kMonths.size) { index ->

            val textColor by remember(selectedMonth) {
                mutableStateOf(
                    if (selectedMonth == index) {
                        SlateColorScheme.onSurface.copy(alpha = 1f)
                    } else {
                        SlateColorScheme.onSurface.copy(alpha = 0.4f)
                    }
                )
            }

            val animatedColor = animateColorAsState(
                targetValue = textColor,
                animationSpec = tween(500, delayMillis = 100)
            )

            Box(
                modifier = Modifier.fillParentMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = Klinder.getInstance().kMonths[index],
                    fontFamily = LexendFamily,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    color = animatedColor.value
                )
            }
        }

        item {
            Spacer(modifier = Modifier.width(20.dp))
        }
    }

}

@Composable
private fun FuncViewFilterBar(
    modifier: Modifier = Modifier,
    selectedFunctionPage: FunctionViewPages,
    onChangeFuncViewPage: (page: FunctionViewPages) -> Unit
){

    val functionPages = remember {
        mutableStateListOf(
            FunctionViewPages.FunctionsPage,
            FunctionViewPages.UserPage
        )
    }

    Row (
        modifier = modifier.fillMaxWidth()
            .height(48.dp)
            .background(SlateColorScheme.surface, shape = RoundedCornerShape(28.dp))
            .clip(RoundedCornerShape(28.dp))
        ,

        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ){
        functionPages.forEach{ page ->
            when(page){
                FunctionViewPages.FunctionsPage -> {
                    Box (
                        modifier = Modifier.size(36.dp)
                            .clip(CircleShape)
                            .background(color = SlateColorScheme.secondaryContainer)
                            .clickable(
                                interactionSource = null,
                                indication = ScaleWithCircleIndication
                            ){
                                onChangeFuncViewPage(FunctionViewPages.FunctionsPage)
                            },
                        contentAlignment = Alignment.Center
                    ){
                        Text("()")
                    }

                }
                FunctionViewPages.UserPage -> {

                    Box (
                        modifier = Modifier.size(36.dp)
                            .clip(CircleShape)
                            .background(color = SlateColorScheme.secondaryContainer)
                            .clickable(
                                interactionSource = null,
                                indication = ScaleWithCircleIndication
                            ){
                                onChangeFuncViewPage(FunctionViewPages.UserPage)
                            },
                        contentAlignment = Alignment.Center
                    ){
                        Text("Us")
                    }

                }
            }
        }
    }

}

@Composable
private fun TestSearchFilterBar(
    modifier: Modifier = Modifier,
    selectedEventTextType: SearchBarEventTextType,
    onChangeEventTextType: (event: SearchBarEventTextType) -> Unit
) {

    val caseEventTypes = remember {
        mutableStateListOf(
            SearchBarEventTextType.Singleton(),
            SearchBarEventTextType.Duration(),
            SearchBarEventTextType.Repeating()
        )
    }

    val repeatingEventType = remember {
        mutableStateListOf(
            SearchBarEventTextType.Daily(),
            SearchBarEventTextType.Weekly(),
            SearchBarEventTextType.Monthly(),
            SearchBarEventTextType.Yearly(),
            SearchBarEventTextType.YearlyEvents()
        )
    }

    val eventType = remember {
        caseEventTypes
    }


    var showRepeatingType by remember {
        mutableStateOf(false)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(color = SlateColorScheme.surface, RoundedCornerShape(28.dp))
            .clip(RoundedCornerShape(28.dp)),
    ) {
        val animatedSize =
            animateDpAsState(targetValue = if (showRepeatingType) 48.dp else 0.dp)
        androidx.compose.animation.AnimatedVisibility(
            visible = showRepeatingType,
            enter = slideInHorizontally { -it },
            exit = slideOutHorizontally { -it }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(animatedSize.value)
                    .background(SlateColorScheme.secondaryContainer)
                    .clickable(
                        interactionSource = null,
                        indication = ScaleIndication
                    ) {
                        showRepeatingType = false
                        eventType.clear()
                        eventType.add(SearchBarEventTextType.Singleton())
                        eventType.add(SearchBarEventTextType.Duration())
                        eventType.add(SearchBarEventTextType.Repeating())
                        onChangeEventTextType(SearchBarEventTextType.All())

                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.back_icon_24px),
                    contentDescription = ""
                )
            }
        }

        LazyRow(
            modifier = modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(SlateColorScheme.surface, RoundedCornerShape(28.dp))
                .clip(RoundedCornerShape(28.dp)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            items(eventType) { events ->

                val iconIndex by remember(eventType.size) {
                    mutableIntStateOf(
                        when (events) {
                            is SearchBarEventTextType.Daily -> {
                                R.drawable.daily_icon_24px
                            }

                            is SearchBarEventTextType.Duration -> {
                                R.drawable.caseduration_100_icon
                            }

                            is SearchBarEventTextType.Monthly -> {
                                R.drawable.monthly_icon_24px
                            }

                            is SearchBarEventTextType.Repeating -> {
                                R.drawable.caserepeatable_icon
                            }

                            is SearchBarEventTextType.Singleton -> {
                                R.drawable.casesingleton_icon
                            }

                            is SearchBarEventTextType.Weekly -> {
                                R.drawable.weekly_icon_24px
                            }

                            is SearchBarEventTextType.Yearly -> {
                                R.drawable.yearly_icon_24px
                            }

                            is SearchBarEventTextType.YearlyEvents -> {
                                R.drawable.yearly_event_icon_24px
                            }

                            else -> {
                                R.drawable.casesingleton_icon
                            }
                        }
                    )
                }

                val animSelectColBase = animateColorAsState(
                    if (selectedEventTextType == events){
                    SlateColorScheme.secondary
                    }else{
                    SlateColorScheme.secondaryContainer
                    }
                )
                val animSelectIconCol = animateColorAsState(
                    if (selectedEventTextType == events){
                        SlateColorScheme.surface
                    }else{
                        Color.Black
                    }
                )

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(animSelectColBase.value)
                        .clickable(
                            interactionSource = null,
                            indication = ScaleIndication
                        ) {
                            if(selectedEventTextType == events){
                                onChangeEventTextType(SearchBarEventTextType.All())
                            }else{
                                when (events) {
                                    is SearchBarEventTextType.Daily -> {
                                        onChangeEventTextType(SearchBarEventTextType.Daily())
                                    }

                                    is SearchBarEventTextType.Duration -> {
                                        onChangeEventTextType(SearchBarEventTextType.Duration())
                                    }

                                    is SearchBarEventTextType.Monthly -> {
                                        onChangeEventTextType(SearchBarEventTextType.Monthly())
                                    }

                                    is SearchBarEventTextType.Repeating -> {
                                        showRepeatingType = true
                                        eventType.clear()
                                        repeatingEventType.forEach { it ->
                                            eventType.add(it)
                                        }
                                        onChangeEventTextType(SearchBarEventTextType.Repeating())
                                    }

                                    is SearchBarEventTextType.Singleton -> {
                                        onChangeEventTextType(SearchBarEventTextType.Singleton())
                                    }

                                    is SearchBarEventTextType.Weekly -> {
                                        onChangeEventTextType(SearchBarEventTextType.Weekly())
                                    }

                                    is SearchBarEventTextType.Yearly -> {
                                        onChangeEventTextType(SearchBarEventTextType.Yearly())
                                    }

                                    is SearchBarEventTextType.YearlyEvents -> {
                                        onChangeEventTextType(SearchBarEventTextType.YearlyEvents())
                                    }

                                    else -> {
                                        onChangeEventTextType(SearchBarEventTextType.All())
                                    }
                                }
                            }


                        },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        modifier = Modifier.size(28.dp),
                        painter = painterResource(id = iconIndex),
                        colorFilter = ColorFilter.tint(animSelectIconCol.value),
                        contentDescription = ""
                    )
                }
            }
        }
    }
}


private fun variableTextSize(baseSize: Int, textSize: Int): TextUnit {
    return if (textSize > 15) {
        (baseSize - 4).sp
    } else {
        baseSize.sp
    }
}

@Preview
@Composable
fun PreviewSearchBarComponent() {
    SearchBarComponent()
}