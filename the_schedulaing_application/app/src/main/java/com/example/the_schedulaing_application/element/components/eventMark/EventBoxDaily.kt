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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.the_schedulaing_application.R
import com.example.the_schedulaing_application.custom.ScaleIndication
import com.example.the_schedulaing_application.custom.ScaleWithCircleIndication
import com.example.the_schedulaing_application.domain.Klinder
import com.example.the_schedulaing_application.domain.kClock
import com.example.the_schedulaing_application.element.components.caseTypeComponents.ClockComponent
import com.example.the_schedulaing_application.element.components.caseTypeComponents.ClockViewModel
import com.example.the_schedulaing_application.element.components.caseTypeComponents.RunningClockType
import com.example.the_schedulaing_application.element.components.caseTypeComponents.SingletonComponent
import com.example.the_schedulaing_application.ui.theme.LexendFamily
import com.example.the_schedulaing_application.ui.theme.SlateColorScheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EventBoxDaily(
    title: String,
    description: String = "",
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
                        fontSize = 12.sp
                    )
                }
                ClockComponent(
                    time = initClock,
                    showSec = expand,
                    containerSize = 28.dp,
                    innerContainerSize = 24.dp,
                    fontSize = 14.sp
                )
            }

        }

        AnimatedVisibility(
            modifier = Modifier.padding(start = 12.dp),
            visible = expand
        ){
            Column {
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = description,
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
                    .height(28.dp)
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
                        color = SlateColorScheme.surface,
                        fontFamily = LexendFamily,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }else{
                val clockViewModel = remember {
                    ClockViewModel(
                        timeLeft,
                        RunningClockType.COUNTDOWN
                    )
                }
                val clock by clockViewModel.runningClock.collectAsStateWithLifecycle()
                ClockComponent(
                    time = clock,
                    showSec = true,
                    indication = ScaleIndication,
                    onLongPress = {showClockTimeLeft = false},
                    containerSize = 28.dp,
                    innerContainerSize = 24.dp,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        AnimatedVisibility(visible = expand) {
            Column {

                HorizontalDivider()

                Spacer(modifier = Modifier.height(6.dp))

                Row (
                    modifier = Modifier.padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ){
                    Box(
                        modifier = Modifier
                            .weight(0.5f)
                            .height(30.dp)
                            .background(SlateColorScheme.secondaryContainer, CircleShape)
                            .clip(CircleShape)
                            .clickable(
                                interactionSource = null,
                                indication = ScaleWithCircleIndication
                            ) { onDelete() },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.delete_icon_24px),
                            colorFilter = ColorFilter.tint(Color.Black),
                            contentDescription =""
                        )
                    }

                    Spacer(modifier = Modifier.width(28.dp))

                    Box(
                        modifier = Modifier
                            .weight(0.5f)
                            .height(30.dp)
                            .background(SlateColorScheme.secondaryContainer, CircleShape)
                            .clip(CircleShape)
                            .clickable(
                                interactionSource = null,
                                indication = ScaleWithCircleIndication
                            ) { onEdit() },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.edit_icon_24px),
                            colorFilter = ColorFilter.tint(Color.Black),
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