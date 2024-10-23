package com.example.the_schedulaing_application.element.components.eventMark

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateIntSizeAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.the_schedulaing_application.domain.Cases.CaseRepeatableType
import com.example.the_schedulaing_application.domain.Cases.CaseType
import com.example.the_schedulaing_application.domain.Cases.SlateEvent
import com.example.the_schedulaing_application.domain.Cases.SlateWeeks
import com.example.the_schedulaing_application.domain.Klinder
import com.example.the_schedulaing_application.ui.theme.Beige20
import com.example.the_schedulaing_application.ui.theme.Orange20
import com.example.the_schedulaing_application.ui.theme.Pink20
import com.example.the_schedulaing_application.ui.theme.Yellow20
import com.example.the_schedulaing_application.ui.theme.eventBlue
import com.example.the_schedulaing_application.ui.theme.eventRed
import kotlin.enums.EnumEntries
import kotlin.enums.enumEntries

@Composable
fun EventMark(event: SlateEvent, collapse: Boolean) {


    val size by remember(collapse) {
        mutableStateOf(
            if (collapse) {
                15.dp
            } else {
                70.dp
            }
        )
    }

    val animatedSizeDp by animateDpAsState(targetValue = size, label = "SizeAnimation")

    val shape by remember(collapse) {
        mutableStateOf(
            if (collapse) {
                CircleShape
            } else {
                RoundedCornerShape(50, 0, 0, 50)
            }

        )
    }

    // Icon Area
    Box(
        modifier = Modifier
            .size(animatedSizeDp, 15.dp)
            .background(
                event
                    .getCaseSpecificColor()
                    .copy(alpha = 0.7f),
                shape = shape
            )


    ) {

        Image(
            painter = painterResource(id = event.getEventIcon()),
            contentDescription = "",
            colorFilter = ColorFilter.tint(eventBlue)
        )

        AnimatedVisibility(
            modifier = Modifier.align(Alignment.CenterEnd),
            visible = !collapse
        ) {
            Text(
                modifier = Modifier.width(55.dp),
                text = event.eventName.value,
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                color = Pink20

            )
        }
    }

}

@Composable
fun EventBox(
    event: SlateEvent
) {
    Surface(
        color = event.getCaseSpecificColor(),
        shape = RoundedCornerShape(24.dp)

    ) {
        Column(modifier = Modifier.padding(8.dp)) {

            Row(
                modifier = Modifier.padding(start = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier.size(28.dp),
                    painter = painterResource(id = event.getEventIcon()),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(eventBlue)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    modifier = Modifier,
                    text = event.eventName.value,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Pink20
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                Box(
                    modifier = Modifier
                        .height(32.dp)
                        .background(Beige20, shape = CircleShape)
                        .padding(horizontal = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = event.getDateStringFromKTime(event.getTimeLeft()),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = eventBlue
                    )
                }

                CaseTypeEventInfoBar(caseType = event.caseType.value)

            }

        }
    }
}

@Composable
private fun CaseTypeEventInfoBar(
    caseType: CaseType
) {

    when (caseType) {
        is CaseType.CaseDuration -> {
            val timeFrom = remember {
                Klinder.getInstance().timeMillisTokTime(caseType.fromEpoch)
            }
            val timeTo = remember {
                Klinder.getInstance().timeMillisTokTime(caseType.toEpoch)
            }
            Row(
                modifier = Modifier
                    .background(Color.Black.copy(alpha = 0.3f), shape = CircleShape)
                    .padding(2.dp)
            ) {
                DateMonthBox(
                    modifier = Modifier.background(Color.Transparent),
                    date = timeFrom.date,
                    month = Klinder.getInstance().conMonthIntToMonthStr(timeFrom.month)
                        .substring(0, 3)
                )
                Spacer(modifier = Modifier.width(6.dp))
                DateMonthBox(
                    date = timeTo.date,
                    month = Klinder.getInstance().conMonthIntToMonthStr(timeTo.month)
                        .substring(0, 3)
                )
            }
        }

        is CaseType.CaseRepeatable -> {
            when (caseType.caseRepeatableType) {
                is CaseRepeatableType.Daily -> TODO()
                is CaseRepeatableType.Monthly -> TODO()
                is CaseRepeatableType.Weekly -> {
                    WeekChartBox(
                        selectWeeks = caseType.caseRepeatableType.selectWeeks,
                        activeWeek = Klinder.getInstance()
                            .getNextActiveWeekDay(caseType.caseRepeatableType.selectWeeks)
                    )
                }

                is CaseRepeatableType.Yearly -> TODO()
                is CaseRepeatableType.YearlyEvent -> {
                    DateMonthBox(
                        date = caseType.caseRepeatableType.date
                        , month = Klinder.getInstance().conMonthIntToMonthStr(
                            caseType.caseRepeatableType.month
                        )
                    )
                }
            }
        }

        is CaseType.CaseSingleton -> {
            val time = remember {
                Klinder.getInstance().timeMillisTokTime(caseType.epochTimeMilli)
            }
            DateMonthBox(
                date = time.date,
                month = Klinder.getInstance().conMonthIntToMonthStr(time.month)
            )

        }
    }
}

@Composable
fun DateMonthBox(
    modifier: Modifier = Modifier,
    date: Int,
    month: String
) {
    Row(
        modifier = modifier
            .height(32.dp)
            .background(color = Beige20, shape = CircleShape)
            .padding(horizontal = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .background(Orange20, CircleShape)
                .padding(4.dp)
        ) {
            Text(
                text = date.toString(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = eventBlue
            )
        }

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = month,
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            color = eventBlue
        )

    }
}

@Composable
fun WeekChartBox(
    modifier: Modifier = Modifier,
    selectWeeks: List<SlateWeeks>,
    activeWeek: SlateWeeks
) {
    Row (
        modifier = modifier
    ){
        repeat(7){ index ->
            var selectionColor by remember {
                mutableStateOf(Yellow20.copy(0.4f))
            }

            LaunchedEffect(key1 = selectWeeks) {
                selectWeeks.forEach {
                    if(index == activeWeek.ordinal){
                        selectionColor = eventBlue
                    }
                    else if (it.ordinal == index){
                        selectionColor = Yellow20
                    }
                }
            }

            Box(modifier = Modifier
                .size(34.dp)
                .padding(2.dp)
                .background(selectionColor, CircleShape),
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = Klinder.getInstance().conWeekIntToWeekStr(index).substring(0,3),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewEventMark() {
    println(System.currentTimeMillis())
    val event = SlateEvent(
        "This is an Example",
        "This is an  example description",
        caseType = CaseType.CaseRepeatable(
            CaseRepeatableType.YearlyEvent(
                27,9
            )
        )
    )
    //EventMark(event,false)
    EventBox(event = event)
}