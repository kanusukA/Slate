package com.example.the_schedulaing_application.element.components.slateDropDownBox

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SlateDropDownBox(
    modifier: Modifier = Modifier,
    boxModifier: Modifier = Modifier,
    boxShape: Shape = RoundedCornerShape(20.dp),
    selectedColor: Color,
    color: Color,
    initSelected: Int,
    elements: List<@Composable () -> Unit>,
    onSelectionChanged: (Int) -> Unit
){

    var selectedElement by remember {
        mutableIntStateOf(initSelected)
    }

    var showAll by remember {
        mutableStateOf(false)
    }

    // TODO ADD SWIPE UP DOWN FUNCTIONALITY

    LazyColumn(
        modifier = modifier
    ) {
        items(elements.size){ index ->
            val selectColor = animateColorAsState(targetValue = if(selectedElement == index){selectedColor}else{color})
            AnimatedVisibility(visible = showAll || selectedElement == index,
                enter = fadeIn(tween(300)),
                exit = fadeOut(tween(300))
            ){
                Box(
                    modifier = boxModifier
                        .fillParentMaxWidth()
                        .background(selectColor.value, shape = boxShape)
                        .clickable {
                            if (showAll) {
                                selectedElement = index
                                onSelectionChanged(index)
                                showAll = false
                            } else {
                                showAll = true
                            }
                        }

                ) {
                    elements[index]()
                }
            }

        }
    }

}

@Preview
@Composable
fun PreviewSlateDropDownBox(){

    /*SlateDropDownBox(elements = listOf(
        {Text("This",
            fontSize = 18.sp
        )
        },
        { Text(text = "That")},
        { Text(text = "NotThis")}
    ),
        initSelected = 0
    )*/

}