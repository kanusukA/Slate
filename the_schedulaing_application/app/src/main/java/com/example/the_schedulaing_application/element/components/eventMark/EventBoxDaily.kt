package com.example.the_schedulaing_application.element.components.eventMark

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.lifecycle.ViewModel
import com.example.the_schedulaing_application.R
import com.example.the_schedulaing_application.custom.ScaleIndication
import com.example.the_schedulaing_application.domain.Klinder
import com.example.the_schedulaing_application.domain.kClock
import com.example.the_schedulaing_application.element.components.caseTypeComponents.ClockComponent
import com.example.the_schedulaing_application.element.components.caseTypeComponents.SingletonComponent
import com.example.the_schedulaing_application.ui.theme.LexendFamily
import com.example.the_schedulaing_application.ui.theme.SlateColorScheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EventBoxDaily(
    title: String,
    eventIconId: Int,
    initClock: kClock,
    timeLeft: kClock,
    timeLeftStr: String,
    onDelete: () -> Unit,
    onEdit : () -> Unit
){

    var showClockTimeLeft by remember {
        mutableStateOf(false)
    }

    var expand by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SlateColorScheme.surface, RoundedCornerShape(24.dp))
            .padding(horizontal = 12.dp)
            .clickable (
                interactionSource = null,
                indication = ScaleIndication
            ){
                expand = !expand
            }
    ){
        Row (
            modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Row(verticalAlignment = Alignment.CenterVertically){

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

            Column{
                androidx.compose.animation.AnimatedVisibility(visible = expand) {
                    Text(
                        text = "daily at",
                        fontFamily = LexendFamily,
                        fontWeight = FontWeight.Light,
                        color = SlateColorScheme.onSecondaryContainer,
                        fontSize = 16.sp
                    )
                }
                ClockComponent(
                    time = initClock,
                    showSec = expand
                )
            }

        }

        AnimatedVisibility(visible = expand){
            Column {
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

        AnimatedContent(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            targetState = showClockTimeLeft
        ){show ->
            if(!show){
                Box(modifier = Modifier
                    .height(32.dp)
                    .background(SlateColorScheme.secondaryContainer, CircleShape)
                    .combinedClickable(
                        interactionSource = null,
                        indication = ScaleIndication,
                        onClick = {},
                        onLongClick = { showClockTimeLeft = true }
                    ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        text = timeLeftStr,
                        color = SlateColorScheme.onSecondaryContainer,
                        fontFamily = LexendFamily,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }else{
                ClockComponent(
                    time = timeLeft,
                    showSec = true,
                    indication = ScaleIndication,
                    onLongPress = {showClockTimeLeft = false}
                )
            }
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
                            ) {onDelete()},
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
                            ) {onEdit()},
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
fun PreviewEventBoxDaily(){
    EventBoxDaily(
        title = "Next Holidays",
        eventIconId = R.drawable.casesingleton_icon,
        initClock = Klinder.getInstance().getKClock(),
        timeLeft = Klinder.getInstance().getKClock(),
        timeLeftStr = "69 Days left",
        onDelete = {},
        onEdit = {}
    )
}