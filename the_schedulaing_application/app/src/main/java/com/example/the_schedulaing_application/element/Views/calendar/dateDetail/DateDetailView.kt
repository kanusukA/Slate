package com.example.the_schedulaing_application.element.Views.calendar.dateDetail

import android.graphics.Paint.Align
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.the_schedulaing_application.Shapes.CalEventColumnShape.shapeAnimation.EventColumnShapeAnimation
import com.example.the_schedulaing_application.domain.Cases.CaseRepeatableType
import com.example.the_schedulaing_application.domain.Cases.CaseType
import com.example.the_schedulaing_application.domain.Cases.SlateEvent
import com.example.the_schedulaing_application.element.Views.calendar.DateBox
import com.example.the_schedulaing_application.element.Views.calendar.DateBoxData
import com.example.the_schedulaing_application.element.components.eventMark.EventBox
import com.example.the_schedulaing_application.ui.theme.Pink20
import com.example.the_schedulaing_application.ui.theme.Yellow20
import java.util.Calendar


@Composable
fun DateDetails(
    dateBox: List<DateBoxData>,
    onInitDateBox: DateBoxData,

    ){

    var selectedDateBox by remember {
        mutableStateOf(onInitDateBox)
    }

    var selectedDateBoxPos by remember(
        key1 = selectedDateBox
    ) {
        mutableStateOf(0f)
    }


    Column(
        modifier = Modifier.fillMaxSize()
    ){

        val listState = rememberLazyListState()

        LazyRow(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            verticalAlignment = Alignment.Bottom,
            state = listState
        ) {
            items(dateBox) { dateBoxData ->

                DateBox(
                    modifier = Modifier
                        .onGloballyPositioned {
                            if (dateBoxData == selectedDateBox) {
                                selectedDateBoxPos = it.positionInWindow().x
                            }
                        }
                        .clickable {
                            if (selectedDateBox != dateBoxData) {
                                selectedDateBox = dateBoxData
                            }

                        }
                    ,
                    dateBoxData = dateBoxData,
                    emptyBox = false,
                    tinyBox = selectedDateBox == dateBoxData
                )

            }
        }

        val eventColumnShapeAnimation = EventColumnShapeAnimation().animationShapeAsState(
            targetPos = selectedDateBoxPos,
            indentCornerRadius = 40.dp,
            indentWidth = 92.dp,
            boxCornerRadius = 40.dp,
            selected = true,
            startPadding = 3.dp,
            itemNotVisible = { TODO()}
        )

        Box(
          modifier = Modifier
              .fillMaxWidth()
              .height(400.dp)
              .background(Yellow20, shape = eventColumnShapeAnimation.value)
        ){
            AnimatedContent(
                targetState = selectedDateBox,
                transitionSpec = {
                    fadeIn(tween(350, delayMillis = 400)) togetherWith
                           fadeOut(tween(350))

                }
            ){ animatedDataBox ->
                LazyColumn(
                    modifier = Modifier,
                    contentPadding = PaddingValues(12.dp)
                ) {
                    items(animatedDataBox.events) { event ->
                        EventBox(event = event)
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                }
            }
        }


    }

}

@Preview
@Composable
fun PreviewDateDetails(){
    val event1 = SlateEvent(
        "Repeat",
        "This is an example description",
        caseType = CaseType.CaseRepeatable(
            CaseRepeatableType.YearlyEvent(
                27,9
            )
        )
    )
    val event2 = SlateEvent(
        "This is an Example",
        "This is an  example description",
        caseType = CaseType.CaseSingleton(
            1732645800000
        )
    )

    Surface(color = Color.Cyan.copy(alpha = 0.1f)){
        DateDetails(
            listOf(
                DateBoxData(1, listOf(event1)),
                DateBoxData(2, listOf(event2)),
                DateBoxData(3, listOf(event1, event2)),
                DateBoxData(4, listOf(event1)),
                DateBoxData(5, listOf(event2)),
                DateBoxData(6, listOf(event1, event2)),
                DateBoxData(7, listOf(event1)),
                DateBoxData(8, listOf(event2)),
                DateBoxData(9, listOf(event1, event2)),
                DateBoxData(10, listOf(event1)),
                DateBoxData(11, listOf(event2)),
                DateBoxData(12, listOf(event1, event2))
            ),
            onInitDateBox = DateBoxData(2, listOf(event2)),
        )
    }
}