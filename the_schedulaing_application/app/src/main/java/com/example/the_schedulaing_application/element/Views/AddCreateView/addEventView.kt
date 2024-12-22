package com.example.the_schedulaing_application.element.Views.AddCreateView

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.the_schedulaing_application.R
import com.example.the_schedulaing_application.custom.ScaleIndication
import com.example.the_schedulaing_application.domain.Cases.CaseRepeatableType
import com.example.the_schedulaing_application.domain.Cases.CaseType
import com.example.the_schedulaing_application.domain.Cases.SlateWeeks
import com.example.the_schedulaing_application.domain.Klinder
import com.example.the_schedulaing_application.element.components.caseTypeComponents.CaseRepeatableYearlyEvent
import com.example.the_schedulaing_application.element.components.caseTypeComponents.CaseRepeatingYearly
import com.example.the_schedulaing_application.element.components.caseTypeComponents.ClockComponent
import com.example.the_schedulaing_application.element.components.caseTypeComponents.MonthlyComponent
import com.example.the_schedulaing_application.element.components.caseTypeComponents.SingletonComponent
import com.example.the_schedulaing_application.element.components.caseTypeComponents.WeekComponent
import com.example.the_schedulaing_application.element.components.caseTypeComponents.YearlyComponent
import com.example.the_schedulaing_application.element.components.sentientTextBox.SentientTextBox
import com.example.the_schedulaing_application.ui.theme.LexendFamily
import com.example.the_schedulaing_application.ui.theme.SlateColorScheme

@Composable
fun AddEventView(
    modifier: Modifier = Modifier
) {

    val viewModel = hiltViewModel<AddEditViewModel>()

    val eventName by viewModel.addEditSharedEvent.slateEventName.collectAsStateWithLifecycle()
    val eventDescription by viewModel.addEditSharedEvent.slateEventDescription.collectAsStateWithLifecycle()
    val eventCaseType by viewModel.addEditSharedEvent.slateEventCaseType.collectAsStateWithLifecycle()
    val timeLeft by viewModel.timeLeft.collectAsStateWithLifecycle()

    val density = LocalDensity.current

    var showRepeating by remember {
        mutableStateOf(false)
    }

    var dropDownExpanded by remember {
        mutableStateOf(false)
    }

    var repeatingDropDownExpanded by remember {
        mutableStateOf(false)
    }

    val caseTypes = remember {
        listOf("Instance", "Duration", "Repeating")
    }
    var caseTypeIndexPosition by remember(eventCaseType) {
        mutableIntStateOf(
            when(eventCaseType){
                is CaseType.CaseDuration -> 1
                is CaseType.CaseRepeatable -> 2
                is CaseType.CaseSingleton -> 0
            }
        )
    }

    val repeatingCaseType = remember {
        listOf("Daily", "Weekly", "Monthly", "Yearly", "Events")
    }

    var repeatingCaseTypeItemPosition by remember(eventCaseType) {
        mutableIntStateOf(
            when(eventCaseType){
                is CaseType.CaseRepeatable -> {
                    val repeating = (eventCaseType as CaseType.CaseRepeatable).caseRepeatableType
                    when(repeating){
                        is CaseRepeatableType.Daily -> 0
                        is CaseRepeatableType.Monthly -> 2
                        is CaseRepeatableType.Weekly -> 1
                        is CaseRepeatableType.Yearly -> 3
                        is CaseRepeatableType.YearlyEvent -> 4
                    }
                }
                else -> 0
            }
        )
    }

    Box(modifier = modifier){
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .background(SlateColorScheme.surfaceContainerLowest),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {

                SentientTextBox(textFieldValue = eventName,
                    labelText = "Title",
                    indentWidth = 60.dp,
                    indentHeight = 18.dp,
                    labelTextSize = 14,
                    singleLine = true,
                    onValueChanged = { viewModel.setEventName(it) }
                )

                Spacer(modifier = Modifier.height(8.dp))

                SentientTextBox(textFieldValue = eventDescription,
                    labelText = "Description",
                    indentWidth = 100.dp,
                    indentHeight = 18.dp,
                    labelTextSize = 14,
                    onValueChanged = { viewModel.setEventDescription(it) }
                )

                Spacer(modifier = Modifier.height(18.dp))

                // SELECTION BOX

                Row {

                    Box(
                        modifier = Modifier
                            .background(SlateColorScheme.secondaryContainer, CircleShape)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                            .clickable(
                                interactionSource = null,
                                indication = ScaleIndication
                            ) { dropDownExpanded = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = caseTypes[caseTypeIndexPosition],
                            fontFamily = LexendFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = SlateColorScheme.onSecondaryContainer
                        )
                    }

                    DropdownMenu(expanded = dropDownExpanded,
                        onDismissRequest = { dropDownExpanded = false }) {
                        caseTypes.forEachIndexed { index, case ->
                            DropdownMenuItem(
                                text = {

                                    Box(
                                        modifier = Modifier
                                            .background(
                                                if (index == caseTypeIndexPosition) {
                                                    SlateColorScheme.secondaryContainer
                                                } else {
                                                    SlateColorScheme.surface
                                                },
                                                CircleShape
                                            )
                                            .padding(horizontal = 12.dp, vertical = 6.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = case,
                                            fontFamily = LexendFamily,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 24.sp,
                                            color = if (index == caseTypeIndexPosition) {
                                                SlateColorScheme.onSecondaryContainer
                                            } else {
                                                SlateColorScheme.onSurface
                                            }
                                        )
                                    }

                                },
                                onClick = {
                                    dropDownExpanded = false
                                    caseTypeIndexPosition = index
                                    showRepeating = index == 2
                                    viewModel.setEventCaseType(index)
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(24.dp))

                    var dropDownOffset by remember {
                        mutableStateOf(DpOffset(0.dp, 0.dp))
                    }

                    androidx.compose.animation.AnimatedVisibility(
                        modifier = Modifier.onGloballyPositioned {
                            with(density) {
                                dropDownOffset = DpOffset(
                                    it.positionInWindow().x.toDp(),
                                    0.dp
                                )
                            }
                        },
                        enter = fadeIn(),
                        exit = fadeOut(),
                        visible = showRepeating
                    ) {
                        Box(
                            modifier = Modifier
                                .background(SlateColorScheme.secondaryContainer, CircleShape)
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                                .clickable(
                                    interactionSource = null,
                                    indication = ScaleIndication
                                ) { repeatingDropDownExpanded = true },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = repeatingCaseType[repeatingCaseTypeItemPosition],
                                fontFamily = LexendFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                color = SlateColorScheme.onSecondaryContainer
                            )
                        }
                    }

                    DropdownMenu(expanded = repeatingDropDownExpanded,
                        offset = dropDownOffset,
                        onDismissRequest = { repeatingDropDownExpanded = false }) {
                        repeatingCaseType.forEachIndexed { index, case ->
                            DropdownMenuItem(
                                text = {

                                    Box(
                                        modifier = Modifier
                                            .background(
                                                if (index == repeatingCaseTypeItemPosition) {
                                                    SlateColorScheme.secondaryContainer
                                                } else {
                                                    SlateColorScheme.surface
                                                },
                                                CircleShape
                                            )
                                            .padding(horizontal = 12.dp, vertical = 6.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = case,
                                            fontFamily = LexendFamily,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 24.sp,
                                            color = if (index == repeatingCaseTypeItemPosition) {
                                                SlateColorScheme.onSecondaryContainer
                                            } else {
                                                SlateColorScheme.onSurface
                                            }
                                        )
                                    }

                                },
                                onClick = {
                                    repeatingDropDownExpanded = false
                                    repeatingCaseTypeItemPosition = index
                                    viewModel.setEventCaseRepeatableType(index)
                                }
                            )
                        }
                    }


                }

                Spacer(modifier = Modifier.height(24.dp))

                // TIME CONTROL
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 90.dp)
                        .background(SlateColorScheme.surface, RoundedCornerShape(24.dp))
                        .padding(12.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        when (val caseType = eventCaseType) {
                            is CaseType.CaseDuration -> {

                                Row {

                                    Column {
                                        Text(
                                            text = stringResource(id = R.string.add_edit_case_duration_from),
                                            fontSize = 16.sp,
                                            fontFamily = LexendFamily,
                                            fontWeight = FontWeight.Light,
                                            color = SlateColorScheme.onSurface
                                        )

                                        Spacer(modifier = Modifier.height(4.dp))

                                        SingletonComponent(
                                            indication = ScaleIndication,
                                            boxHeight = 38,
                                            fontSize = 18,
                                            clickable = true,
                                            onSetValue = {
                                                viewModel.setEventCaseType(caseType.copy(fromEpoch = it))
                                            }
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column {
                                        Text(
                                            text = stringResource(id = R.string.add_edit_case_duration_to),
                                            fontSize = 16.sp,
                                            fontFamily = LexendFamily,
                                            fontWeight = FontWeight.Light,
                                            color = SlateColorScheme.onSurface
                                        )

                                        Spacer(modifier = Modifier.height(4.dp))

                                        SingletonComponent(
                                            indication = ScaleIndication,
                                            boxHeight = 38,
                                            fontSize = 18,
                                            clickable = true,
                                            onSetValue = {
                                                viewModel.setEventCaseType(caseType.copy(toEpoch = it))
                                            }
                                        )
                                    }

                                }
                            }

                            is CaseType.CaseRepeatable -> {

                                when ((eventCaseType as CaseType.CaseRepeatable).caseRepeatableType) {
                                    is CaseRepeatableType.Daily -> {

                                        var selectedTime by remember {
                                            mutableStateOf(Klinder.getInstance().getKClock())
                                        }

                                        Text(
                                            text = stringResource(id = R.string.add_edit_case_daily),
                                            fontSize = 16.sp,
                                            fontFamily = LexendFamily,
                                            fontWeight = FontWeight.Light,
                                            color = SlateColorScheme.onSurface
                                        )

                                        Spacer(modifier = Modifier.height(4.dp))

                                        ClockComponent(
                                            time = selectedTime,
                                            clickable = true,
                                            containerSize = 38.dp,
                                            innerContainerSize = 34.dp,
                                            fontSize = 18.sp,
                                            onSetTime = {
                                                selectedTime = it
                                                viewModel.setEventCaseType(
                                                    CaseType.CaseRepeatable(
                                                        CaseRepeatableType.Daily(it.timeOfDay)
                                                    )
                                                )
                                            }
                                        )
                                    }

                                    is CaseRepeatableType.Monthly -> {
                                        Text(
                                            text = stringResource(id = R.string.add_edit_case_monthly),
                                            fontSize = 16.sp,
                                            fontFamily = LexendFamily,
                                            fontWeight = FontWeight.Light,
                                            color = SlateColorScheme.onSurface
                                        )

                                        Spacer(modifier = Modifier.height(4.dp))
                                        MonthlyComponent(
                                            modifier = Modifier.height(280.dp),
                                            boxSize = 38.dp,
                                            fontSize = 20.sp,
                                            takeInput = true,
                                            onSelectedDates = { selectedDates ->
                                                viewModel.setEventCaseType(
                                                    CaseType.CaseRepeatable(
                                                        CaseRepeatableType.Monthly(selectedDates)
                                                    )
                                                )
                                            }
                                        )

                                    }

                                    is CaseRepeatableType.Weekly -> {
                                        Text(
                                            text = stringResource(id = R.string.add_edit_case_weekly),
                                            fontSize = 16.sp,
                                            fontFamily = LexendFamily,
                                            fontWeight = FontWeight.Light,
                                            color = SlateColorScheme.onSurface
                                        )

                                        Spacer(modifier = Modifier.height(4.dp))
                                        WeekComponent(
                                            modifier = Modifier.fillMaxWidth(),
                                            inputWeeks = emptyList(),
                                            clickable = true,
                                            activeWeek = SlateWeeks.MONDAY,
                                            onSelectedWeeks = {
                                                viewModel.setEventCaseType(
                                                    CaseType.CaseRepeatable(
                                                        CaseRepeatableType.Weekly(it)
                                                    )
                                                )
                                            }
                                        )

                                    }

                                    is CaseRepeatableType.Yearly -> {

                                        var caseTypeYearly by remember {
                                            mutableStateOf(
                                                CaseRepeatableType.Yearly(
                                                    emptyList(),
                                                    emptyList()
                                                )
                                            )
                                        }
                                        Text(
                                            text = stringResource(id = R.string.add_edit_case_yearly),
                                            fontSize = 16.sp,
                                            fontFamily = LexendFamily,
                                            fontWeight = FontWeight.Light,
                                            color = SlateColorScheme.onSurface
                                        )

                                        Spacer(modifier = Modifier.height(4.dp))
                                        YearlyComponent(
                                            takeInput = true,
                                            dateBoxSize = 38.dp,
                                            dateFontSize = 20.sp,
                                            monthBoxSize = 38.dp,
                                            monthFontSize = 20.sp,
                                            onSelectedDates = {
                                                caseTypeYearly = caseTypeYearly.copy(date = it)
                                                viewModel.setEventCaseType(
                                                    CaseType.CaseRepeatable(caseTypeYearly)
                                                )
                                            },
                                            onSelectedMonths = {
                                                caseTypeYearly =
                                                    caseTypeYearly.copy(selectMonths = it)
                                                viewModel.setEventCaseType(
                                                    CaseType.CaseRepeatable(caseTypeYearly)
                                                )
                                            }
                                        )
                                    }

                                    is CaseRepeatableType.YearlyEvent -> {
                                        Text(
                                            text = stringResource(id = R.string.add_edit_case_event),
                                            fontSize = 16.sp,
                                            fontFamily = LexendFamily,
                                            fontWeight = FontWeight.Light,
                                            color = SlateColorScheme.onSurface
                                        )

                                        Spacer(modifier = Modifier.height(4.dp))
                                        SingletonComponent(
                                            indication = ScaleIndication,
                                            boxHeight = 38,
                                            fontSize = 18,
                                            clickable = true,
                                            onSetDateMonth = { date, month ->
                                                viewModel.setEventCaseType(
                                                    CaseType.CaseRepeatable(
                                                        CaseRepeatableType.YearlyEvent(date, month)
                                                    )
                                                )
                                            }
                                        )
                                    }
                                }
                            }

                            is CaseType.CaseSingleton -> {
                                Text(
                                    text = stringResource(id = R.string.add_edit_case_singleton),
                                    fontSize = 16.sp,
                                    fontFamily = LexendFamily,
                                    fontWeight = FontWeight.Light,
                                    color = SlateColorScheme.onSurface
                                )

                                Spacer(modifier = Modifier.height(4.dp))
                                SingletonComponent(
                                    indication = ScaleIndication,
                                    boxHeight = 38,
                                    fontSize = 18,
                                    clickable = true,
                                    onSetValue = {
                                        viewModel.setEventCaseType(CaseType.CaseSingleton(it))
                                    }
                                )

                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .height(32.dp)
                                .background(SlateColorScheme.secondaryContainer, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                modifier = Modifier.padding(horizontal = 8.dp),
                                text = timeLeft,
                                color = SlateColorScheme.onSecondaryContainer,
                                fontFamily = LexendFamily,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black
                            )
                        }


                    }
                }
            }

        }
    }
}

@Preview
@Composable
fun PreviewAddEventView() {

}