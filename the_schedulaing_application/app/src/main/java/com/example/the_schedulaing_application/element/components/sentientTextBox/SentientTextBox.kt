package com.example.the_schedulaing_application.element.components.sentientTextBox

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.the_schedulaing_application.Shapes.SentientTextBoxShape.SentientTextBoxShape
import com.example.the_schedulaing_application.ui.theme.Pink20
import com.example.the_schedulaing_application.ui.theme.Yellow20

class SentientTextBoxStateShape(

): SentientTextBoxImpl{
    @Composable
    override fun sentientTextBoxAsState(
        indentWidth: Dp,
        indentHeight: Dp,
        indentPaddingFromStart: Dp,
        indentCornerRadius: Dp,
        cornerRadius: Dp,
        showIndent: Boolean
    ): State<Shape> {



        val animatedIndentWidth = animateDpAsState(targetValue = indentWidth)
        println("showIndent $showIndent")
        val animateIndentHeight = animateDpAsState(
            targetValue = if(showIndent){ indentHeight }
            else{0.dp}
        )

        val initShape = remember {
            SentientTextBoxShape(
                indentWidth = indentWidth,
                indentHeight = indentHeight,
                indentSpaceFromStart = indentPaddingFromStart,
                indentCornerRadius = indentCornerRadius,
                cornerRadius = cornerRadius
            )
        }
        
        return produceState(initialValue = initShape,
            key1 = animatedIndentWidth.value,
            key2 = animateIndentHeight.value
        ) {
            this.value = this.value.copy(
                indentWidth = animatedIndentWidth.value,
                indentHeight = animateIndentHeight.value
            )
        }
    }

}



@Composable
fun SentientTextBox(
    modifier: Modifier = Modifier,
    textFieldValue: String,
    labelText: String,
    labelTextSize: Int = 10,
    indentWidth: Dp = 0.dp,
    indentHeight: Dp = 10.dp,
    indentPaddingFromStart: Dp = 40.dp,
    indentCornerRadius: Dp = 10.dp,
    boxCornerRadius: Dp = 60.dp,
    boxColor: Color = Yellow20,
    textColor: Color = Pink20,
    labelTextColor: Color = Pink20,
    singleLine: Boolean = false,
    onValueChanged: (String) -> Unit

){

    var showTopLabel by remember {
        mutableStateOf(
            textFieldValue.isNotBlank()
        )
    }

    val shape = SentientTextBoxStateShape().sentientTextBoxAsState(
            indentWidth = indentWidth,
            indentHeight = indentHeight,
            indentPaddingFromStart = indentPaddingFromStart,
            indentCornerRadius = indentCornerRadius,
            cornerRadius = boxCornerRadius,
            showIndent = showTopLabel
    )

    // Main Box
    Box(modifier = modifier
        .background(boxColor,shape.value)
        .animateContentSize()
    ){
        Column{
            AnimatedVisibility(visible = showTopLabel,
                modifier = Modifier
                    .height(indentHeight)
                    .width(indentWidth + indentPaddingFromStart)
                    .padding(start = indentPaddingFromStart + indentCornerRadius),

            ){
                Box(modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        modifier = Modifier,
                        text = labelText,
                        color = labelTextColor,
                        fontSize = labelTextSize.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

            }

            Box(modifier = Modifier
                .padding(vertical = 2.dp, horizontal = 4.dp)
            ){
                TextField(
                    value = textFieldValue,
                    onValueChange = {
                        onValueChanged(it)
                        showTopLabel = it.isNotBlank()
                                    },
                    modifier = Modifier,
                    colors = TextFieldDefaults.colors(
                        disabledIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent
                    ),
                    shape = CircleShape,
                    placeholder = {
                        Text(text = labelText,
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp
                        )
                    }
                )
            }
        }

    }


}

@Preview
@Composable
fun PreviewSentientTextBoxShape() {

    var textValue  by remember {
        mutableStateOf("")
    }

    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        SentientTextBox(
            modifier = Modifier,
            textFieldValue = textValue,
            onValueChanged = {
                textValue = it
            },
            labelText = "Enter Date",
            indentWidth = 70.dp
        )


    }
}