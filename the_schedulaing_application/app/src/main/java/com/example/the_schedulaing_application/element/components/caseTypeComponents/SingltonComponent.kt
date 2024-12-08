package com.example.the_schedulaing_application.element.components.caseTypeComponents

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.the_schedulaing_application.domain.Klinder
import com.example.the_schedulaing_application.domain.kTime
import com.example.the_schedulaing_application.element.components.eventMark.DateMonthBox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingletonComponent(
    time: kTime = Klinder.getInstance().getKTime(),
    clickable: Boolean = false,
    indication: Indication? = null,
    tiny: Boolean = false,
    boxHeight: Int = 32,
    fontSize: Int = 16,
    expanded: (Boolean) -> Unit = {},
    onSetValue: (Long) -> Unit = {},
    onSetDateMonth: (date: Int,month: Int) -> Unit = {date, month ->}
) {

    var currentDate by remember {
        mutableStateOf(
            time
        )
    }

    val datePickerState = rememberDatePickerState()

    var showDatePicker by remember {
        mutableStateOf(false)
    }

    if(tiny){
        DateMonthBox(
            date = currentDate.date,
            month = currentDate.monthStr.substring(0,3),
            day = Klinder.getInstance().kWeeks[currentDate.day - 1],
            year = currentDate.year,
            indication = indication,
            boxHeight = boxHeight,
            fontSize = fontSize,
            expanded = {expanded(it)},
            clickable = {
                if (clickable) {
                    showDatePicker = true
                }
            }
        )
    }else{
        // Fix the month string
        DateMonthBox(
            date = currentDate.date,
            month = currentDate.monthStr,
            day = Klinder.getInstance().kWeeks[currentDate.day - 1],
            year = currentDate.year,
            indication = indication,
            boxHeight = boxHeight,
            fontSize = fontSize,
            expanded = {expanded(it)},
            clickable = {
                if (clickable) {
                    showDatePicker = true
                }
            }
        )
    }

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

@Preview
@Composable
fun PreviewSingletonComponent(){
    SingletonComponent()
}