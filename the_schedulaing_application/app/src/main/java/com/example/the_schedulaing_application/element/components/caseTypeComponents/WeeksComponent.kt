package com.example.the_schedulaing_application.element.components.caseTypeComponents

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.the_schedulaing_application.domain.Cases.SlateWeeks
import com.example.the_schedulaing_application.domain.Klinder
import com.example.the_schedulaing_application.ui.theme.SlateColorScheme

@Composable
fun WeekComponent(
    selectedWeeks: List<SlateWeeks>,
    activeWeek: SlateWeeks,
    weekComponentSize: WeeksComponentSize = WeeksComponentSize.NORMAL
) {

    val slateWeeks = remember {
        SlateWeeks.entries
    }

    Row(
        modifier = Modifier
            .height(
                if (weekComponentSize == WeeksComponentSize.SHORT) {
                    32.dp
                } else {
                    40.dp
                }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        repeat(7) { index ->

            val week: String = remember {
                if (weekComponentSize == WeeksComponentSize.NORMAL) {
                    Klinder.getInstance().kWeeks[index].substring(0, 3)
                } else {
                    Klinder.getInstance().kWeeks[index][0].toString()
                }
            }


            val color = remember {
                if (selectedWeeks.contains(slateWeeks[index])) {
                    SlateColorScheme.secondary
                } else {
                    SlateColorScheme.secondaryContainer
                }
            }

            Spacer(modifier = Modifier.width(3.dp))

            Box(
                modifier = Modifier
                    .size(
                        when (weekComponentSize) {
                            WeeksComponentSize.NORMAL -> {
                                36.dp
                            }

                            WeeksComponentSize.SHORT -> {
                                28.dp
                            }

                            else -> {
                                12.dp
                            }
                        }
                    )
                    .background(color, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.animation.AnimatedVisibility(visible = weekComponentSize != WeeksComponentSize.TINY) {
                    Text(
                        text = week.uppercase(),
                        fontSize = if (weekComponentSize == WeeksComponentSize.NORMAL) {
                            14.sp
                        } else {
                            16.sp
                        },
                        fontWeight = FontWeight.Black,
                        color = SlateColorScheme.onSecondary
                    )
                }
            }
        }
        Spacer(modifier = Modifier.width(3.dp))
    }

}

enum class WeeksComponentSize {
    NORMAL,
    SHORT,
    TINY
}

@Preview
@Composable
fun PreviewWeekComponent() {
    WeekComponent(
        selectedWeeks = listOf(SlateWeeks.MONDAY, SlateWeeks.WEDNESDAY, SlateWeeks.SATURDAY),
        activeWeek = SlateWeeks.MONDAY,
        WeeksComponentSize.TINY
    )
}