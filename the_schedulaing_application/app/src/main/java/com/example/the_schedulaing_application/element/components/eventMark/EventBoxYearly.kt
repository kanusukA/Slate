package com.example.the_schedulaing_application.element.components.eventMark

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.the_schedulaing_application.R
import com.example.the_schedulaing_application.custom.ScaleIndication
import com.example.the_schedulaing_application.domain.Klinder
import com.example.the_schedulaing_application.domain.kTime
import com.example.the_schedulaing_application.element.components.caseTypeComponents.MonthlyComponent
import com.example.the_schedulaing_application.element.components.caseTypeComponents.SingletonComponent
import com.example.the_schedulaing_application.element.components.caseTypeComponents.YearlyComponent
import com.example.the_schedulaing_application.ui.theme.LexendFamily
import com.example.the_schedulaing_application.ui.theme.SlateColorScheme

@Composable
fun EventBoxYearly(
    title: String,
    eventIconId: Int,
    dates: List<Int>,
    months: List<Int>,
    nextTime: kTime,
    timeLeft: String,
    onDelete: () -> Unit,
    onEdit: () -> Unit
){
    var expand by remember {
        mutableStateOf(true)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SlateColorScheme.surface, RoundedCornerShape(24.dp))
            .padding(horizontal = 12.dp)
            .clickable(
                interactionSource = null,
                indication = ScaleIndication
            ) { expand = !expand }
    ) {
        Row(
            modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {

                Image(
                    modifier = Modifier.size(32.dp),
                    painter = painterResource(id = eventIconId),
                    contentDescription = "Singleton icon",
                    colorFilter = ColorFilter.tint(SlateColorScheme.onSurface)
                )

                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = title,
                    color = SlateColorScheme.onSurface,
                    fontFamily = LexendFamily,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            androidx.compose.animation.AnimatedVisibility(visible = !expand){
                SingletonComponent(
                    indication = ScaleIndication,
                    time = nextTime
                )
            }
        }

        AnimatedVisibility(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            visible = expand
        ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ){

                Spacer(modifier = Modifier.height(12.dp))

                Column{
                    Text(
                        text = "on these dates",
                        fontFamily = LexendFamily,
                        fontWeight = FontWeight.Light,
                        color = SlateColorScheme.onSecondaryContainer,
                        fontSize = 16.sp
                    )

                    YearlyComponent(
                        modifier = Modifier
                            .width(300.dp)
                            .background(
                                SlateColorScheme.surfaceContainerHigh,
                                RoundedCornerShape(24.dp)
                            )
                            .padding(top = 6.dp,start = 12.dp,end = 12.dp,bottom = 12.dp)
                        ,
                        inputDates = dates,
                        inputMonths = months,
                        onSelectedDates = {},
                        onSelectedMonths = {}
                    )

                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "This is an example description which is only visible when the box is expanded.",
                    fontFamily = LexendFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                )


            }
        }

        Spacer(modifier = Modifier.height(18.dp))

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

        Spacer(modifier = Modifier.height(12.dp))

        AnimatedVisibility(visible = expand) {
            Column {

                HorizontalDivider()

                Spacer(modifier = Modifier.height(12.dp))

                Row (
                    horizontalArrangement = Arrangement.SpaceEvenly
                ){
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .height(34.dp)
                            .padding(end = 6.dp)
                            .background(SlateColorScheme.primaryContainer, CircleShape)
                            .clickable(
                                interactionSource = null,
                                indication = ScaleIndication
                            ) { onDelete() },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.delete_icon_24px),
                            colorFilter = ColorFilter.tint(SlateColorScheme.onSurface),
                            contentDescription =""
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(34.dp)
                            .background(SlateColorScheme.primaryContainer, CircleShape)
                            .clickable(
                                interactionSource = null,
                                indication = ScaleIndication
                            ) { onEdit() },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.edit_icon_24px),
                            colorFilter = ColorFilter.tint(SlateColorScheme.onSurface),
                            contentDescription =""
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))
            }
        }

    }
}

@Preview
@Composable
fun PreviewEventBoxYearly(){
    EventBoxYearly(
        title = "This Year",
        eventIconId = R.drawable.caserepeatable_icon,
        dates = listOf(1,12,9,23),
        months = listOf(1,5,6,9),
        nextTime = Klinder.getInstance().getKTime(),
        timeLeft = "69 Days left",
        onDelete = {},
        onEdit = {}
    )
}