package com.example.the_schedulaing_application.element.calendar


import android.animation.ValueAnimator
import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.the_schedulaing_application.domain.Cases.CaseRepeatableType
import com.example.the_schedulaing_application.domain.Cases.CaseType
import com.example.the_schedulaing_application.domain.Cases.SlateEvent
import com.example.the_schedulaing_application.element.eventMark.EventMark
import com.example.the_schedulaing_application.ui.theme.LexendFamily
import com.example.the_schedulaing_application.ui.theme.Pink20
import com.example.the_schedulaing_application.ui.theme.Yellow20
import com.example.the_schedulaing_application.ui.theme.eventBlue


@Composable
fun CalenderView(
    viewModel: CalViewModel
){

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)
    ){

        /*Column {

            repeat(7){
                Box(modifier = Modifier.height(90.dp),
                    contentAlignment = Alignment.BottomStart
                ){
                    Text(
                        modifier = Modifier,
                        text = viewModel.getWeekStr(it + 1),
                        //fontFamily = LexendFamily,
                        fontWeight = FontWeight.Black,
                        fontSize = 58.sp,
                        color = eventBlue.copy()
                    )
                }
            }
        }*/

        // Date Boxes
        Column(modifier = Modifier.fillMaxWidth()){

            Text(
                text = viewModel.year.value.toString(),
                fontSize = 36.sp,
                fontWeight = FontWeight.Black,
                color = Pink20
            )

            Text(text = viewModel.month.value.monthStr,
                fontSize = 48.sp,
                fontWeight = FontWeight.Black,
                color = Pink20
            )

            LazyHorizontalGrid(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .height(630.dp),
                rows = GridCells.Fixed(7),
            ) {
                items(viewModel.daysInThisMonth() + viewModel.getFirstDayOfMonth()) {
                    DateBox(
                       dateBoxData =  viewModel.getDateBoxData(it - viewModel.getFirstDayOfMonth()),
                       emptyBox =  it <= viewModel.getFirstDayOfMonth()
                    )
                }
            }
        }





    }

}


@Composable
fun DateBox(
    modifier: Modifier = Modifier,
    dateBoxData: DateBoxData,
    emptyBox: Boolean ,
    tinyBox: Boolean = false ,
    ){

    val density = LocalDensity.current

    val boxHeightSize: Float = remember {
        with(density){90.dp.toPx()}
    }

    val boxSize by remember(tinyBox) {
        mutableStateOf(if(tinyBox){70.dp}else{90.dp})
    }

    val animatedBoxSize = animateDpAsState(targetValue = boxSize)

    Box(
        modifier = modifier
            .requiredSize(75.dp, animatedBoxSize.value)
            .padding(
                top = 2.dp,
                start = 2.dp,
                bottom = 2.dp
            )
            .clip(shape = MaterialTheme.shapes.large)
            .background(Yellow20)

    ){

        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 2.dp)
        ) {
            if (!emptyBox) {
                Text(
                    dateBoxData.date.toString(),
                    fontSize = 28.sp,
                    fontFamily = LexendFamily,
                    fontWeight = FontWeight.Bold,
                    color = Pink20
                )

                if (dateBoxData.events.isNotEmpty()) {

                    AnimatedVisibility(
                        visible = !tinyBox,
                        enter = slideInVertically(
                            animationSpec = tween(700)
                        ){
                            boxHeightSize.toInt()
                        },
                        exit = slideOutVertically(
                            animationSpec = tween(700)
                        ){
                            boxHeightSize.toInt()
                        },

                    ) {
                        Column(
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            repeat(dateBoxData.events.size) {
                                EventMark(event = dateBoxData.events[it], false)
                            }
                        }
                    }

                }
            }
        }

    }
}

@Preview
@Composable
fun previewCalender(){
    val calViewModel = CalViewModel()
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
    DateBox(dateBoxData = DateBoxData(1, listOf(event1)), emptyBox = false)
}