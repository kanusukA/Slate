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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.example.the_schedulaing_application.domain.Klinder
import com.example.the_schedulaing_application.domain.kClock
import com.example.the_schedulaing_application.ui.theme.LexendFamily
import com.example.the_schedulaing_application.ui.theme.SlateColorScheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ClockComponent(
    time: kClock,
    showSec: Boolean = false,
    indication: Indication? = null,
    containerSize: Dp = 32.dp,
    innerContainerSize: Dp = 28.dp,
    fontSize: TextUnit = 16.sp,
    clickable: Boolean = false,
    onClick: () -> Unit = {},
    onLongPress: () -> Unit = {},
    onSetTime: (kClock) -> Unit = {}
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

    var showTimePicker by remember {
        mutableStateOf(false)
    }

    val timePickerState = rememberTimePickerState()

    AnimatedVisibility(visible = showTimePicker) {

        Dialog(onDismissRequest = { showTimePicker = false }) {
            Column(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        RoundedCornerShape(20.dp)
                    )
                    .padding(12.dp)
            ) {
                TimePicker(state = timePickerState)

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Button(onClick = { showTimePicker = false }) {
                        Text(text = "Cancel")
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    FilledTonalButton(onClick = {
                        showTimePicker = false
                        onSetTime(
                            kClock(
                                hour = timePickerState.hour,
                                min = timePickerState.minute,
                                sec = 0
                            )
                        )
                    }) {
                        Text(text = "Set")
                    }
                }
            }
        }
    }

    Row(modifier = Modifier
        .height(containerSize)
        .background(SlateColorScheme.secondaryContainer, CircleShape)
        .combinedClickable(interactionSource = interactionSource,
            indication = indication,
            onClick = {
                onClick()
                showTimePicker = clickable
                      },
            onLongClick = { onLongPress() }), verticalAlignment = Alignment.CenterVertically) {
        Spacer(modifier = Modifier.width(3.dp))

        Box(
            modifier = Modifier
                .size(innerContainerSize)
                .background(SlateColorScheme.secondary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = time.hour.toString(),
                fontFamily = LexendFamily,
                fontSize = fontSize,
                fontWeight = FontWeight.Black,
                color = SlateColorScheme.surface
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
                    .background(SlateColorScheme.surface, CircleShape)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Box(
                modifier = Modifier
                    .size(5.dp)
                    .background(SlateColorScheme.surface, CircleShape)
            )
        }

        Spacer(modifier = Modifier.width(2.dp))

        Box(
            modifier = Modifier
                .size(innerContainerSize)
                .background(SlateColorScheme.secondary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = time.min.toString(),
                fontFamily = LexendFamily,
                fontSize = fontSize,
                fontWeight = FontWeight.Black,
                color = SlateColorScheme.surface
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
                            .background(SlateColorScheme.surface, CircleShape)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .size(5.dp)
                            .background(SlateColorScheme.surface, CircleShape)
                    )
                }

                Spacer(modifier = Modifier.width(2.dp))

                Box(
                    modifier = Modifier
                        .size(innerContainerSize)
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
                            fontSize = fontSize,
                            fontWeight = FontWeight.Black,
                            color = SlateColorScheme.surface
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.width(3.dp))

    }
}

class ClockViewModel(
    inputKClock: kClock,
    val clockType: RunningClockType
) : ViewModel() {

    private var _runningClock = MutableStateFlow(inputKClock)
    val runningClock: StateFlow<kClock> = _runningClock

    init {
        viewModelScope.launch{
            Klinder.getInstance().clockFlow().collect {
                when(clockType){
                    RunningClockType.COUNTDOWN -> timeDown()
                    RunningClockType.NORMAL -> timeUp()
                }
            }
        }
    }

    private var stop = false

    private fun timeUp(){
        _runningClock.update { it.copy(sec = it.sec + 1) }
        if(_runningClock.value.sec >= 60){
            _runningClock.update { it.copy(min = it.min + 1, sec = 0) }
            if(_runningClock.value.min >= 60){
                _runningClock.update { it.copy(hour = it.hour + 1, min = 0, sec = 0) }
                if(_runningClock.value.hour >= 24){
                    _runningClock.update { it.copy(hour = 0, min = 0, sec = 0) }
                }
            }
        }
    }

    private fun timeDown(){
        if (stop){
            return
        }
        _runningClock.update { it.copy(sec = it.sec - 1) }
        if(_runningClock.value.sec < 0){
            _runningClock.update { it.copy(min = it.min - 1, sec = 59) }
            if(_runningClock.value.min < 0){
                _runningClock.update { it.copy(hour = it.hour - 1, min = 59, sec = 59) }
                if(_runningClock.value.hour <= 0){
                    _runningClock.update { it.copy(hour = 0, min = 0, sec = 0) }
                    stop = true
                }
            }
        }
    }

}

enum class RunningClockType{
    COUNTDOWN,
    NORMAL
}

@Preview
@Composable
fun PreviewClockComponent() {
    val clockViewModel = remember {
        ClockViewModel(
            kClock(0,0,10),
            RunningClockType.COUNTDOWN
        )
    }

    val clock by clockViewModel.runningClock.collectAsStateWithLifecycle()

    ClockComponent(
        showSec = true,
        time = clock,
        clickable = true,
        onSetTime = {}
    )
}

@Preview
@Composable
fun PreviewClockComponentBigger() {
    val clockViewModel = remember {
        ClockViewModel(
            kClock(0,0,10),
            RunningClockType.COUNTDOWN
        )
    }

    val clock by clockViewModel.runningClock.collectAsStateWithLifecycle()

    ClockComponent(
        showSec = false,
        time = clock,
        clickable = true,
        containerSize = 38.dp,
        innerContainerSize = 34.dp,
        fontSize = 18.sp,
        onSetTime = {}
    )
}