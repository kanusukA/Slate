package com.example.the_schedulaing_application.element.components.caseTypeComponents

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.example.the_schedulaing_application.domain.Klinder
import com.example.the_schedulaing_application.domain.kClock
import com.example.the_schedulaing_application.ui.theme.LexendFamily
import com.example.the_schedulaing_application.ui.theme.SlateColorScheme
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ClockComponent(
    time: kClock,
    showSec: Boolean = false,
    indication: Indication? = null,
    onClick: () -> Unit = {},
    onLongPress: () -> Unit = {}
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }
    var min by remember {
        mutableIntStateOf(time.min)
    }

    var colonTickSec by remember {
        mutableFloatStateOf(1f)
    }

    val colonTickAnimation = animateFloatAsState(
        targetValue = colonTickSec,
        animationSpec = tween(200),
        finishedListener = {colonTickSec = 1f}
    )

    var minColonTick by remember {
        mutableFloatStateOf(1f)
    }

    val minColonTickAnimation = animateFloatAsState(
        targetValue = minColonTick,
        animationSpec = tween(200),
        finishedListener = {minColonTick = 1f}
    )

    LaunchedEffect(key1 = time) {
        colonTickSec = 0.3f
        if(time.min != min){
            min = time.min
            minColonTick = 0.2f
        }

    }

    Row(modifier = Modifier
        .height(32.dp)
        .background(SlateColorScheme.secondaryContainer, CircleShape)
        .combinedClickable(interactionSource = interactionSource,
            indication = indication,
            onClick = { onClick() },
            onLongClick = { onLongPress() }), verticalAlignment = Alignment.CenterVertically) {
        Spacer(modifier = Modifier.width(3.dp))

        Box(
            modifier = Modifier
                .size(28.dp)
                .background(SlateColorScheme.secondary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = time.hour.toString(),
                fontFamily = LexendFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                color = SlateColorScheme.onSecondary
            )
        }
        Spacer(modifier = Modifier.width(2.dp))
        Column (
            modifier = Modifier.alpha(
                if(showSec){
                    minColonTickAnimation.value
                }else{
                    colonTickAnimation.value
                }
            )
        ){
            Box(
                modifier = Modifier
                    .size(5.dp)
                    .background(SlateColorScheme.onSecondary, CircleShape)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Box(
                modifier = Modifier
                    .size(5.dp)
                    .background(SlateColorScheme.onSecondary, CircleShape)
            )
        }

        Spacer(modifier = Modifier.width(2.dp))

        Box(
            modifier = Modifier
                .size(28.dp)
                .background(SlateColorScheme.secondary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = time.min.toString(),
                fontFamily = LexendFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                color = SlateColorScheme.onSecondary
            )
        }

        AnimatedVisibility(visible = showSec) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(modifier = Modifier.width(2.dp))
                Column (
                    modifier = Modifier.alpha(colonTickAnimation.value)
                ){
                    Box(
                        modifier = Modifier
                            .size(5.dp)
                            .background(SlateColorScheme.onSecondary, CircleShape)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .size(5.dp)
                            .background(SlateColorScheme.onSecondary, CircleShape)
                    )
                }

                Spacer(modifier = Modifier.width(2.dp))

                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .background(SlateColorScheme.secondary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    AnimatedContent(
                        targetState = time.sec,
                        transitionSpec = {
                            fadeIn() togetherWith
                                    fadeOut()
                        }
                    ){ sec ->
                        Text(
                            text = sec.toString(),
                            fontFamily = LexendFamily,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = SlateColorScheme.onSecondary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.width(3.dp))

    }
}

class ClockViewModel() : ViewModel() {

    val clock = Klinder.getInstance().clockFlow().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = Klinder.getInstance().getKClock()
    )

}

@Preview
@Composable
fun PreviewClockComponent() {
    val clockViewModel = remember {
        ClockViewModel()
    }
    val clock by clockViewModel.clock.collectAsStateWithLifecycle()

    ClockComponent(
        showSec = true, time = clock
    )
}