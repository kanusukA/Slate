package com.example.the_schedulaing_application.element.components.caseTypeComponents

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.the_schedulaing_application.custom.ScaleIndication
import com.example.the_schedulaing_application.domain.Klinder
import com.example.the_schedulaing_application.ui.theme.LexendFamily
import com.example.the_schedulaing_application.ui.theme.SlateColorScheme

@Composable
fun YearlyComponent(
    modifier: Modifier = Modifier,
    dateBoxSize: Dp = 28.dp,
    dateFontSize: TextUnit = 16.sp,
    monthBoxSize: Dp = 28.dp,
    monthFontSize: TextUnit = 16.sp,
    inputDates: List<Int> = emptyList(),
    inputMonths: List<Int> = emptyList(),
    takeInput: Boolean = false,
    onSelectedDates: (List<Int>) -> Unit = {},
    onSelectedMonths: (List<Int>) -> Unit = {}
) {

    val selectedMonths = remember {
        if (inputMonths.isNotEmpty()) {
            inputMonths.toMutableStateList()
        } else {
            mutableStateListOf()
        }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        MonthlyComponent(
            modifier = Modifier.height(220.dp),
            boxSize = dateBoxSize,
            fontSize = dateFontSize,
            inputDate = inputDates,
            takeInput = takeInput,
            onSelectedDates = { onSelectedDates(it) }
        )

        Spacer(modifier = Modifier.height(6.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            items(12) { index ->

                var textColor by remember {
                    mutableStateOf(SlateColorScheme.onSecondaryContainer)
                }

                var color by remember {
                    mutableStateOf(
                        if (selectedMonths.contains(index + 1)) {
                            textColor = SlateColorScheme.surface
                            SlateColorScheme.secondary
                        } else {
                            textColor = SlateColorScheme.onSecondaryContainer
                            SlateColorScheme.secondaryContainer
                        }
                    )
                }

                val animatedColor = animateColorAsState(targetValue = color, label = "")
                val animatedTextColor = animateColorAsState(targetValue = textColor, label = "")

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(6.dp))

                    Box(
                        modifier = Modifier
                            .requiredWidth(60.dp)
                            .height(monthBoxSize)
                            .background(animatedColor.value, CircleShape)
                            .clickable(
                                interactionSource = null,
                                indication = if (takeInput) {
                                    ScaleIndication
                                } else {
                                    null
                                }
                            ) {
                                if (takeInput) {
                                    if (selectedMonths.contains(index + 1)) {
                                        textColor = SlateColorScheme.onSecondaryContainer
                                        color = SlateColorScheme.secondaryContainer
                                        selectedMonths.remove(index + 1)
                                    } else {
                                        textColor = SlateColorScheme.surface
                                        color = SlateColorScheme.secondary
                                        selectedMonths.add(index + 1)
                                        selectedMonths.sort() // small size list. so nvm.
                                    }
                                    onSelectedMonths(selectedMonths)
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = Klinder.getInstance().kMonths[index].substring(0, 3),
                            fontFamily = LexendFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = monthFontSize,
                            color = animatedTextColor.value
                        )
                    }
                }

            }
        }

    }
}

@Preview
@Composable
fun PreviewYearlyComponent() {
    YearlyComponent(
        onSelectedDates = {},
        onSelectedMonths = {}
    )
}