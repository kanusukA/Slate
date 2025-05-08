package com.example.the_schedulaing_application.element.BottomControlBar

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.the_schedulaing_application.R
import com.example.the_schedulaing_application.custom.ScaleIndication
import com.example.the_schedulaing_application.custom.ScaleWithCircleIndication
import com.example.the_schedulaing_application.element.Navigation.NavRoutes
import com.example.the_schedulaing_application.element.Views.AddCreateView.AddEditViewModel
import com.example.the_schedulaing_application.ui.theme.LexendFamily
import com.example.the_schedulaing_application.ui.theme.SlateColorScheme

@Composable
fun BottomNavControlBar(
    currentPage: NavRoutes,
    modifier: Modifier = Modifier,
) {

    val viewModel = hiltViewModel<BottomNavControlBarViewModel>()

    Box(
        modifier = modifier
            .height(180.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.White
                    )
                )
            )
            .padding(bottom = 24.dp, start = 12.dp, end = 12.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = Modifier
                .height(48.dp)
                .fillMaxWidth()
                .shadow(4.dp, RoundedCornerShape(28.dp))
                .background(SlateColorScheme.surfaceContainerHigh, RoundedCornerShape(28.dp)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            when (currentPage) {

                is NavRoutes.AddEditPage -> {
                    BottomAddEditPageBar(
                        onClickCancel = {
                            viewModel.onClickCancel()
                        },
                        onClickSave = {
                            viewModel.onClickSave()
                        }
                    )
                }

                else -> {
                    BottomHomePageBar(
                        currentPage,
                        onChangePage = {
                            viewModel.changeBottomNavBarPage(it)
                        }
                    )
                }
            }

        }

    }
}

@Composable
private fun BottomAddEditPageBar(
    onClickCancel: () -> Unit,
    onClickSave: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(36.dp)
            .background(SlateColorScheme.secondary, CircleShape)
            .padding(horizontal = 12.dp)
            .clickable { onClickCancel() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Cancel",
            fontFamily = LexendFamily,
            fontWeight = FontWeight.Bold,
            color = SlateColorScheme.surfaceContainerHigh,
            fontSize = 22.sp,
        )
    }

    Box(
        modifier = Modifier
            .height(36.dp)
            .background(SlateColorScheme.secondary, CircleShape)
            .padding(horizontal = 12.dp)
            .clickable { onClickSave() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Save",
            fontFamily = LexendFamily,
            fontWeight = FontWeight.Bold,
            color = SlateColorScheme.surfaceContainerHigh,
            fontSize = 22.sp,
        )
    }

}

@Composable
private fun BottomHomePageBar(
    currentPage: NavRoutes,
    onChangePage: (NavRoutes) -> Unit
) {

    // Home Page
    val homeColor by remember(currentPage) {
        mutableStateOf(
            if (currentPage == NavRoutes.HomePage) {
                SlateColorScheme.secondary
            } else {
                SlateColorScheme.secondaryContainer
            }
        )
    }

    val animatedHomeColor = animateColorAsState(targetValue = homeColor)

    Box(
        modifier = Modifier
            .height(36.dp)
            .width(64.dp)
            .background(animatedHomeColor.value, CircleShape)
            .clip(CircleShape)
            .clickable(
                interactionSource = null,
                indication = ScaleWithCircleIndication
            ) {
                onChangePage(NavRoutes.HomePage)
            },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.home_icon_24px),
            contentDescription = "Home page",
            colorFilter = ColorFilter.tint(SlateColorScheme.surfaceContainerHigh)
        )
    }

    // Calendar Page

    val calendarColor by remember(currentPage) {
        mutableStateOf(
            if (currentPage == NavRoutes.CalendarPage) {
                SlateColorScheme.secondary
            } else {
                SlateColorScheme.secondaryContainer
            }
        )
    }

    val animatedCalendarColor = animateColorAsState(targetValue =calendarColor)

    Box(
        modifier = Modifier
            .height(36.dp)
            .width(64.dp)
            .background(animatedCalendarColor.value, CircleShape)
            .clip(CircleShape)
            .clickable(
                interactionSource = null,
                indication = ScaleWithCircleIndication
            ) {
                onChangePage(NavRoutes.CalendarPage)
            },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.calendar_icon_24px),
            contentDescription = "Home page",
            colorFilter = ColorFilter.tint(SlateColorScheme.surfaceContainerHigh)
        )
    }

    // Function Page

    val functionColor by remember(currentPage) {
        mutableStateOf(
            if (currentPage == NavRoutes.FunctionPage) {
                SlateColorScheme.secondary
            } else {
                SlateColorScheme.secondaryContainer
            }
        )
    }

    val animatedFunctionColor = animateColorAsState(targetValue = functionColor)

    Box(
        modifier = Modifier
            .height(36.dp)
            .width(64.dp)
            .background(animatedFunctionColor.value, CircleShape)
            .clip(CircleShape)
            .clickable(
                interactionSource = null,
                indication = ScaleWithCircleIndication
            ) {
                onChangePage(NavRoutes.FunctionPage)
            },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.function_icon_24px),
            contentDescription = "Home page",
            colorFilter = ColorFilter.tint(SlateColorScheme.surfaceContainerHigh)
        )
    }

}

@Preview
@Composable
fun PreviewBottomNavControlBar() {

}