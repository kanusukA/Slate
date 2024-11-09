package com.example.the_schedulaing_application.element.components.caseTypeComponents

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Popup
import com.example.the_schedulaing_application.domain.Cases.CaseType
import com.example.the_schedulaing_application.domain.Cases.SlateWeeks
import com.example.the_schedulaing_application.domain.Klinder
import com.example.the_schedulaing_application.domain.kClock
import com.example.the_schedulaing_application.element.components.eventMark.DateMonthBox
import com.example.the_schedulaing_application.ui.theme.Beige20
import com.example.the_schedulaing_application.ui.theme.Pink20
import com.example.the_schedulaing_application.ui.theme.Yellow20
import com.example.the_schedulaing_application.ui.theme.eventBlue
import kotlinx.coroutines.delay


// Singleton
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaseSingletonComponent(
    onSetValue: (Long) -> Unit = {},
    onSetDateMonth: (date: Int,month: Int) -> Unit = {date, month ->}
) {

    var currentDate by remember {
        mutableStateOf(
            Klinder.getInstance().getKTime()
        )
    }

    val datePickerState = rememberDatePickerState()

    var showDatePicker by remember {
        mutableStateOf(false)
    }

    DateMonthBox(
        date = currentDate.date,
        month = currentDate.monthStr,
        day = currentDate.day,
        year = currentDate.year,
        clickable = { showDatePicker = true }
    )

    AnimatedVisibility(visible = showDatePicker) {

        Dialog(onDismissRequest = { showDatePicker = false }) {
            Column(
                modifier = Modifier
                    .width(300.dp)
                    .height(550.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(20.dp)
                    )
            ) {
                DatePicker(
                    state = datePickerState
                )
                Row(
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Button(onClick = { showDatePicker = false }) {
                        Text(text = "Cancel")
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    FilledTonalButton(onClick = {
                        showDatePicker = false
                        onSetValue(datePickerState.selectedDateMillis ?: System.currentTimeMillis())
                        currentDate = Klinder.getInstance().getKTime(
                            datePickerState.selectedDateMillis ?: System.currentTimeMillis()
                        )
                        onSetDateMonth(currentDate.date,currentDate.month)

                    }) {
                        Text(text = "Set")
                    }
                }
            }
        }

    }
}

@Composable
fun CaseDurationComponent(
    onSetFrom: (Long) -> Unit,
    onSetTo: (Long) -> Unit
) {
    Row {
        CaseSingletonComponent(
            onSetValue = { onSetFrom(it) }
        )
        Spacer(modifier = Modifier.width(12.dp))
        CaseSingletonComponent(
            onSetValue = { onSetTo(it) }
        )
    }
}

@Composable
fun CaseRepeatableWeek(
    modifier: Modifier = Modifier,
    colorSelect: Color = Pink20,
    colorBackground: Color = Beige20,
    activeWeeks: (List<SlateWeeks>) -> Unit
) {

    val weekString = remember {
        Klinder.getInstance().kWeeks
    }

    val slateWeeks by remember {
        mutableStateOf(SlateWeeks.entries)
    }

    val selectedWeeks = remember {
        mutableListOf<SlateWeeks>()
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        repeat(slateWeeks.size) { index ->
            var color by remember {
                mutableStateOf(Beige20)
            }
            val animatedColor = animateColorAsState(targetValue = color)
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(animatedColor.value, CircleShape)
                    .clickable {
                        if (selectedWeeks.contains(slateWeeks[index])) {
                            selectedWeeks.remove(slateWeeks[index])
                            color = colorBackground
                        } else {
                            selectedWeeks.add(slateWeeks[index])
                            color = colorSelect
                        }
                        activeWeeks(selectedWeeks)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = weekString[index].substring(0, 3),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = eventBlue
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CaseRepeatableDaily(
    modifier: Modifier = Modifier,
    timeChange: (kClock) -> Unit
) {
    var currentTime by remember {
        mutableStateOf(Klinder.getInstance().getKClock())
    }

    var showTimePicker by remember {
        mutableStateOf(false)
    }

    var longPressed by remember {
        mutableStateOf(false)
    }

    val hapticFeedback = LocalHapticFeedback.current

    val timePickerState = rememberTimePickerState()

    LaunchedEffect(key1 = longPressed) {
        delay(5000)
        longPressed = false
    }

    Row(modifier = modifier
        .background(Yellow20, shape = CircleShape)
        .padding(4.dp)
        .combinedClickable(
            onClick = { showTimePicker = true },
            onLongClick = {
                longPressed = true
                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
            }
        )
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(color = Beige20, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = currentTime.hour.toString(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = eventBlue
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .size(32.dp)
                .background(color = Beige20, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = currentTime.min.toString(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = eventBlue
            )
        }

        AnimatedVisibility(visible = longPressed) {
            Row {
                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(color = Beige20, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = currentTime.sec.toString(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = eventBlue
                    )
                }
            }
        }

    }

    AnimatedVisibility(visible = showTimePicker) {

        Dialog(onDismissRequest = { showTimePicker = false }) {
            Column(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        RoundedCornerShape(20.dp)
                    )
                    .padding(12.dp)
            ) {
                TimePicker(state = timePickerState)

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Button(onClick = { showTimePicker = false }) {
                        Text(text = "Cancel")
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    FilledTonalButton(onClick = {
                        showTimePicker = false
                        currentTime = kClock(
                            timePickerState.hour,
                            timePickerState.minute,
                            0
                        )
                        timeChange(currentTime)
                    }) {
                        Text(text = "Set")
                    }
                }
            }
        }
    }
}

@Composable
fun CaseRepeatingYearly(
    activeDays: (List<Int>) -> Unit,
    activeMonths: (List<Int>) -> Unit
) {

    val selectedDays = remember {
        mutableListOf<Int>()
    }

    val selectedMonths = remember {
        mutableListOf<Int>()
    }

    val caseMonths = remember {
        CaseMonths.toMutableStateList()
    }

    var blockFeb by remember {
        mutableStateOf(false)
    }

    var blockFullMonth by remember {
        mutableStateOf(false)
    }

    Column {
        LazyVerticalGrid(columns = GridCells.Fixed(8)) {

            items(31) { index ->

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    var color by remember {
                        mutableStateOf(Beige20)
                    }

                    val animatedColor = animateColorAsState(targetValue = color)

                    Spacer(modifier = Modifier.height(6.dp))

                    Box(
                        modifier = Modifier
                            .requiredSize(36.dp)
                            .background(color = animatedColor.value, CircleShape)
                            .clickable {
                                if (selectedDays.contains(index + 1)) {
                                    selectedDays.remove(index + 1)
                                    color = Beige20

                                } else {
                                    selectedDays.add(index + 1)
                                    color = Pink20
                                }


                                if (index + 1 == 31) {
                                    caseMonths[4] =
                                        caseMonths[4].copy(active = !selectedDays.contains(index + 1))
                                    caseMonths[6] =
                                        caseMonths[6].copy(active = !selectedDays.contains(index + 1))
                                    caseMonths[8] =
                                        caseMonths[8].copy(active = !selectedDays.contains(index + 1))
                                    caseMonths[10] =
                                        caseMonths[10].copy(active = !selectedDays.contains(index + 1))
                                }
                                if (Klinder
                                        .getInstance()
                                        .isLeapYear()
                                ) {
                                    if (index + 1 > 29) {
                                        caseMonths[1] =
                                            caseMonths[1].copy(active = !selectedDays.contains(index + 1))
                                    }
                                } else {
                                    if (index + 1 > 28) {
                                        caseMonths[1] =
                                            caseMonths[1].copy(active = !selectedDays.contains(index + 1))
                                    }
                                }
                                activeDays(selectedDays)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = (index + 1).toString(),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = eventBlue
                        )
                    }
                }
            }
        }


        LazyVerticalGrid(columns = GridCells.Fixed(6)) {
            items(
                caseMonths.size,
                key = { index: Int -> caseMonths[index].hashCode() }
            ) { index ->

                val month = remember {
                    caseMonths[index]
                }

                var color by remember(month.active) {
                    mutableStateOf(
                        if (month.active) {
                            Beige20
                        } else {
                            if (selectedMonths.contains(index)) {
                                selectedMonths.remove(index)
                            }
                            Beige20.copy(alpha = 0.3f)
                        }
                    )
                }

                val animatedColor = animateColorAsState(targetValue = color)

                Box(
                    modifier = Modifier
                        .height(32.dp)
                        .background(animatedColor.value, CircleShape)
                        .clickable {
                            if (month.active) {
                                if (selectedMonths.contains(index)) {
                                    selectedMonths.remove(index)
                                    color = Beige20
                                } else {
                                    selectedMonths.add(index)
                                    color = Pink20
                                }
                                activeMonths(selectedMonths)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = month.monthString.substring(0, 3),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = eventBlue
                    )
                }
            }
        }
    }

}

@Composable
fun CaseRepeatableMonthly(
    onSelectedDates: (selectedDates: List<Int>) -> Unit
) {

    val selectableDaysInMonth = remember {
        mutableStateListOf<Boolean>().apply {
            for (i in 0..31) {
                add(false)
            }
        }
    }

    val selectedDates = remember {
        mutableListOf<Int>()
    }

    LazyVerticalGrid(columns = GridCells.Fixed(8)) {
        items(31) { index ->

            val color by remember(selectableDaysInMonth[index]) {
                mutableStateOf(
                    if (selectableDaysInMonth[index]) {
                        Pink20
                    } else {
                        Beige20
                    }
                )
            }
            val animatedColor = animateColorAsState(targetValue = color, label = "")

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(6.dp))

                Box(
                    modifier = Modifier
                        .requiredSize(36.dp)
                        .background(color = animatedColor.value, CircleShape)
                        .clickable {
                            if (selectedDates.contains(index)) {
                                selectedDates.remove(index)
                                selectableDaysInMonth[index] = false
                            } else {
                                selectedDates.add(index)
                                selectableDaysInMonth[index] = true
                            }
                            onSelectedDates(selectedDates)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (index + 1).toString(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = eventBlue
                    )
                }
            }
        }
    }

}

@Composable
fun CaseRepeatableYearlyEvent(
    onSelectedDateMonth:(date: Int,month: Int) -> Unit
) {
    CaseSingletonComponent(
        onSetDateMonth = {date, month -> onSelectedDateMonth(date,month) }
    )
}

private data class CaseMonth(
    val monthString: String,
    val active: Boolean
)

private val CaseMonths = listOf(
    CaseMonth("January", true),
    CaseMonth("February", true),
    CaseMonth("March", true),
    CaseMonth("April", true),
    CaseMonth("May", true),
    CaseMonth("June", true),
    CaseMonth("July", true),
    CaseMonth("August", true),
    CaseMonth("September", true),
    CaseMonth("October", true),
    CaseMonth("November", true),
    CaseMonth("December", true)
)

@Preview
@Composable
fun PreviewCaseTypeComponents() {
    Surface {
        CaseRepeatingYearly(activeDays = {}) {
            
        }
    }

}