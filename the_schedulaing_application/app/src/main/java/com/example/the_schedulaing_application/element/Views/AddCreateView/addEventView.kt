package com.example.the_schedulaing_application.element.Views.AddCreateView

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.the_schedulaing_application.R
import com.example.the_schedulaing_application.domain.Cases.CaseRepeatableType
import com.example.the_schedulaing_application.domain.Cases.CaseType
import com.example.the_schedulaing_application.domain.Cases.SlateEvent
import com.example.the_schedulaing_application.domain.Klinder
import com.example.the_schedulaing_application.element.components.caseTypeComponents.CaseDurationComponent
import com.example.the_schedulaing_application.element.components.caseTypeComponents.CaseRepeatableDaily
import com.example.the_schedulaing_application.element.components.caseTypeComponents.CaseRepeatableMonthly
import com.example.the_schedulaing_application.element.components.caseTypeComponents.CaseRepeatableWeek
import com.example.the_schedulaing_application.element.components.caseTypeComponents.CaseRepeatableYearlyEvent
import com.example.the_schedulaing_application.element.components.caseTypeComponents.CaseRepeatingYearly
import com.example.the_schedulaing_application.element.components.caseTypeComponents.CaseSingletonComponent
import com.example.the_schedulaing_application.element.components.sentientTextBox.SentientTextBox
import com.example.the_schedulaing_application.element.components.slateDropDownBox.SlateDropDownBox
import com.example.the_schedulaing_application.ui.theme.Pink20
import com.example.the_schedulaing_application.ui.theme.Yellow20

@Composable
fun AddEventView(
    viewModel: AddEditViewModel
) {

    val eventName by viewModel.eventName.collectAsStateWithLifecycle()
    val eventDescription by viewModel.eventDescription.collectAsStateWithLifecycle()
    val eventCaseType by viewModel.eventCaseType.collectAsStateWithLifecycle()

    var showRepeating by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = eventCaseType) {
        showRepeating = when (eventCaseType) {
            is CaseType.CaseRepeatable -> {
                true
            }

            else -> {
                false
            }
        }
    }

    Column(
        modifier = Modifier
            .height(500.dp)
            .fillMaxWidth()
            .background(Yellow20, shape = RoundedCornerShape(20.dp))
            .padding(8.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ){
        Column {
            SentientTextBox(textFieldValue = eventName,
                labelText = "Enter Title",
                indentWidth = 70.dp,
                onValueChanged = { viewModel.setEventName(it) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            SentientTextBox(textFieldValue = eventDescription,
                labelText = "Enter Description",
                indentWidth = 90.dp,
                onValueChanged = { viewModel.setEventDescription(it) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // SELECTION BOX

            Row {
                SlateDropDownBox(
                    modifier = Modifier
                        .width(200.dp)
                        .background(Pink20.copy(alpha = 0.3f), shape = RoundedCornerShape(30.dp))
                        .padding(vertical = 6.dp, horizontal = 6.dp),
                    selectedColor = Color.White.copy(alpha = 0.3f),
                    color = Color.Black.copy(alpha = 0.3f),
                    initSelected = 0,
                    onSelectionChanged = { viewModel.setEventCaseType(it) },
                    elements = listOf(
                        {
                            Row(
                                modifier = Modifier.height(40.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.casesingleton_icon),
                                    contentDescription = ""
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Instance",
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                            }
                        },
                        {
                            Row(
                                modifier = Modifier.height(40.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.caseduration_100_icon),
                                    contentDescription = ""
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Duration",
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                            }
                        },
                        {
                            Row(
                                modifier = Modifier.height(40.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.caserepeatable_icon),
                                    contentDescription = ""
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Repeating",
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                            }
                        }
                    )
                )

                AnimatedVisibility(visible = showRepeating) {
                    SlateDropDownBox(
                        modifier = Modifier
                            .background(
                                Pink20.copy(alpha = 0.3f),
                                shape = RoundedCornerShape(30.dp)
                            )
                            .padding(vertical = 6.dp, horizontal = 6.dp),
                        selectedColor = Color.White.copy(alpha = 0.3f),
                        color = Color.Black.copy(alpha = 0.3f),
                        initSelected = 0,
                        onSelectionChanged = {
                            viewModel.setEventCaseRepeatableType(it)
                        },
                        elements = listOf(
                            {
                                Row(
                                    modifier = Modifier.height(40.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Daily",
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                }
                            },
                            {
                                Row(
                                    modifier = Modifier.height(40.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Text(
                                        text = "Weekly",
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                }
                            },
                            {
                                Row(
                                    modifier = Modifier.height(40.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Text(
                                        text = "Monthly",
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                }
                            },
                            {
                                Row(
                                    modifier = Modifier.height(40.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Text(
                                        text = "Yearly",
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                }
                            },
                            {
                                Row(
                                    modifier = Modifier.height(40.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Text(
                                        text = "Event",
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                }
                            }
                        )
                    )
                }

            }

            // TIME CONTROL
            when (eventCaseType) {
                is CaseType.CaseDuration -> {

                    var caseDuration by remember {
                        mutableStateOf(
                            CaseType.CaseDuration(
                                System.currentTimeMillis(),
                                System.currentTimeMillis()
                            )
                        )
                    }
                    CaseDurationComponent(
                        onSetFrom = {
                            caseDuration = caseDuration.copy(it)
                            viewModel.setEventCaseType(caseDuration)
                        },
                        onSetTo = {
                            caseDuration = caseDuration.copy(toEpoch = it)
                            viewModel.setEventCaseType(caseDuration)
                        }
                    )

                }

                is CaseType.CaseRepeatable -> {

                    when ((eventCaseType as CaseType.CaseRepeatable).caseRepeatableType) {
                        is CaseRepeatableType.Daily -> {
                            CaseRepeatableDaily(
                                timeChange = {
                                    viewModel.setEventCaseType(
                                        CaseType.CaseRepeatable(
                                            CaseRepeatableType.Daily(it.timeOfDay)
                                        )
                                    )

                                }
                            )
                        }

                        is CaseRepeatableType.Monthly -> {
                            CaseRepeatableMonthly { selectedDates ->
                                viewModel.setEventCaseType(
                                    CaseType.CaseRepeatable(
                                        CaseRepeatableType.Monthly(selectedDates)
                                    )
                                )
                            }

                        }

                        is CaseRepeatableType.Weekly -> {
                            CaseRepeatableWeek {
                                viewModel.setEventCaseType(
                                    CaseType.CaseRepeatable(
                                        CaseRepeatableType.Weekly(it)
                                    )
                                )

                            }
                        }

                        is CaseRepeatableType.Yearly -> {
                            // TODO GET CASE TYPE YEARLY FROM THE WHEN CONDITION.
                            var caseTypeYearly by remember {
                                mutableStateOf(CaseRepeatableType.Yearly(emptyList(), emptyList()))
                            }
                            CaseRepeatingYearly(
                                activeDays = {

                                    caseTypeYearly = caseTypeYearly.copy(date = it)
                                    viewModel.setEventCaseType(
                                        CaseType.CaseRepeatable(caseTypeYearly)
                                    )
                                },
                                activeMonths = {

                                    caseTypeYearly = caseTypeYearly.copy(selectMonths = it)
                                    viewModel.setEventCaseType(
                                        CaseType.CaseRepeatable(caseTypeYearly)
                                    )
                                }
                            )
                        }

                        is CaseRepeatableType.YearlyEvent -> {
                            CaseRepeatableYearlyEvent { date, month ->
                                viewModel.setEventCaseType(
                                    CaseType.CaseRepeatable(
                                        CaseRepeatableType.YearlyEvent(date, month)
                                    )
                                )
                            }
                        }
                    }
                }

                is CaseType.CaseSingleton -> {
                    CaseSingletonComponent(
                        onSetValue = {
                            viewModel.setEventCaseType(CaseType.CaseSingleton(it))
                        }
                    )
                }
            }

        }

        Row(modifier = Modifier.align(Alignment.End)) {
            Button(onClick = { viewModel.resetValues() }) {
                Text(text = "Cancel")
            }
            Spacer(modifier = Modifier.width(12.dp))
            FilledTonalButton(onClick = { viewModel.setValuesToDataBase() }) {
                Text(text = "Set")
            }
        }

    }
}

@Preview
@Composable
fun PreviewAddEventView() {
    /*val viewModel = remember {
        //AddEditViewModel()
    }
    AddEventView(
        viewModel
    )*/
}