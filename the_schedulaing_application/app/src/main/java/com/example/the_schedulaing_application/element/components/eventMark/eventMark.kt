package com.example.the_schedulaing_application.element.components.eventMark

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateIntSizeAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.the_schedulaing_application.custom.ScaleIndication
import com.example.the_schedulaing_application.domain.Cases.CaseRepeatableType
import com.example.the_schedulaing_application.domain.Cases.CaseType
import com.example.the_schedulaing_application.domain.Cases.SlateEvent
import com.example.the_schedulaing_application.domain.Cases.SlateWeeks
import com.example.the_schedulaing_application.domain.Klinder
import com.example.the_schedulaing_application.ui.theme.Beige20
import com.example.the_schedulaing_application.ui.theme.LexendFamily
import com.example.the_schedulaing_application.ui.theme.Orange20
import com.example.the_schedulaing_application.ui.theme.Pink20
import com.example.the_schedulaing_application.ui.theme.Purple80
import com.example.the_schedulaing_application.ui.theme.SlateColorScheme
import com.example.the_schedulaing_application.ui.theme.Yellow20
import com.example.the_schedulaing_application.ui.theme.eventBlue
import com.example.the_schedulaing_application.ui.theme.eventRed
import kotlinx.coroutines.delay
import java.util.Locale
import kotlin.enums.EnumEntries
import kotlin.enums.enumEntries

@Composable
fun EventMark(
    modifier: Modifier = Modifier,
    event: SlateEvent,
    collapse: Boolean
) {


    Row(
        modifier = modifier
            .requiredHeight(16.dp)
            .clip(RoundedCornerShape(topStartPercent = 50, bottomStartPercent = 50))
            .background(Purple80),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = event.getEventIcon()),
            contentDescription = null,
        )

        Text(
            text = event.eventName,
            fontFamily = LexendFamily,
            fontSize = 8.sp,
            softWrap = false,
            lineHeight = 2.sp

        )
    }

}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DateMonthBox(
    modifier: Modifier = Modifier,
    date: Int,
    day: String,
    month: String,
    monthLongPress: String = month.substring(0, 3),
    year: Int,
    boxHeight: Int = 32,
    fontSize: Int = 16,
    indication: Indication? = null,
    clickable: () -> Unit = {},
    expanded: (Boolean) -> Unit = {}
) {

    var longClicked by remember {
        mutableStateOf(false)
    }

    val monthStr by remember(longClicked, month) {
        mutableStateOf(
            if (longClicked) {
                monthLongPress
            } else {
                month
            }
        )
    }

    val interactionSource = remember {
        MutableInteractionSource()
    }

    LaunchedEffect(key1 = longClicked) {
        delay(5000)
        longClicked = false
        expanded(false)
    }

    val hapticFeedback = LocalHapticFeedback.current


    Row(
        modifier = modifier
            .height(boxHeight.dp)
            .background(color = SlateColorScheme.secondaryContainer, shape = CircleShape)
            .padding(horizontal = 2.dp)
            .combinedClickable(
                interactionSource = interactionSource,
                indication = indication,
                onClick = { clickable() },
                onLongClick = {
                    expanded(true)
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    longClicked = true
                }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size((boxHeight - 2).dp)
                .background(SlateColorScheme.secondary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = date.toString(),
                fontFamily = LexendFamily,
                fontSize = fontSize.sp,
                fontWeight = FontWeight.Black,
                color = SlateColorScheme.onSecondary
            )
        }

        Spacer(modifier = Modifier.width(4.dp))

        AnimatedVisibility(visible = longClicked) {
            Row {
                Text(
                    text = day.substring(0, 3).uppercase(),
                    fontSize = fontSize.sp,
                    fontFamily = LexendFamily,
                    fontWeight = FontWeight.Black,
                    color = SlateColorScheme.surface
                )

                Spacer(modifier = Modifier.width(4.dp))
            }
        }

        // Month Expanded - October
        AnimatedContent(targetState = monthStr) { it ->
            Row {
                Text(
                    text = it.uppercase(),
                    fontSize = fontSize.sp,
                    fontFamily = LexendFamily,
                    fontWeight = FontWeight.Black,
                    color = SlateColorScheme.surface
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
        }

        AnimatedVisibility(visible = longClicked) {
            Row {

                Text(
                    text = year.toString(),
                    fontSize = fontSize.sp,
                    fontFamily = LexendFamily,
                    fontWeight = FontWeight.Black,
                    color = SlateColorScheme.surface
                )
                Spacer(modifier = Modifier.width(4.dp))
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
        inputCaseType = CaseType.CaseRepeatable(
            CaseRepeatableType.YearlyEvent(
                27, 9
            )
        )
    )
    //EventMark(event,false)
    DateMonthBox(date = 28, day = "Monday", month = "October", year = 2024)
}