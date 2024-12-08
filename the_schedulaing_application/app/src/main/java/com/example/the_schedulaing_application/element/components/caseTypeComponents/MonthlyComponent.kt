package com.example.the_schedulaing_application.element.components.caseTypeComponents

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.the_schedulaing_application.custom.ScaleIndication
import com.example.the_schedulaing_application.ui.theme.Beige20
import com.example.the_schedulaing_application.ui.theme.Pink20
import com.example.the_schedulaing_application.ui.theme.SlateColorScheme
import com.example.the_schedulaing_application.ui.theme.eventBlue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

@Composable
fun MonthlyComponent(
    modifier: Modifier = Modifier,
    boxSize: Dp = 28.dp,
    fontSize: TextUnit = 16.sp,
    inputDate: List<Int> = emptyList(),
    onSelectedDates: (selectedDates: List<Int>) -> Unit = {},
    takeInput: Boolean = false
) {

    val dates = remember {
        if(inputDate.isNotEmpty()){
            inputDate.toMutableStateList()
        }else{
            mutableStateListOf()
        }
    }

    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(7),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        items(31) { index ->

            var textColor by remember {
                mutableStateOf(SlateColorScheme.onSecondaryContainer)
            }

            var color by remember {
                mutableStateOf(
                    if (dates.contains(index + 1)) {
                        textColor = SlateColorScheme.surfaceContainerLow
                        SlateColorScheme.secondary
                    } else {
                        textColor = SlateColorScheme.onSecondaryContainer
                        SlateColorScheme.secondaryContainer
                    }
                )
            }


            val animatedColor = animateColorAsState(targetValue = color, label = "")
            val animatedTextColor = animateColorAsState(targetValue = textColor)

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .requiredSize(boxSize)
                        .background(color = animatedColor.value, CircleShape)
                        .clickable(
                            interactionSource = null,
                            indication = if (takeInput) {
                                ScaleIndication
                            } else {
                                null
                            }
                        ) {
                            if (takeInput) {
                                if(dates.contains(index+1)){
                                    textColor = SlateColorScheme.onSecondaryContainer
                                    color = SlateColorScheme.secondaryContainer
                                    dates.remove(index+1)
                                }else{
                                    textColor = SlateColorScheme.surfaceContainerLow
                                    color = SlateColorScheme.secondary
                                    dates.add(index+1)
                                    dates.sort() // Adds 3-4 millis at most
                                }
                            }
                            onSelectedDates(dates)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (index + 1).toString(),
                        fontSize = fontSize,
                        fontWeight = FontWeight.Black,
                        color = animatedTextColor.value
                    )
                }
            }
        }
    }

}



@Preview
@Composable
fun PreviewMonthlyComponent() {
    MonthlyComponent(
        onSelectedDates = {},
        takeInput = true
    )
}