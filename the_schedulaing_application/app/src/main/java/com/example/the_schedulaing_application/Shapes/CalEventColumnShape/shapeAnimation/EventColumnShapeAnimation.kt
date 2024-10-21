package com.example.the_schedulaing_application.Shapes.CalEventColumnShape.shapeAnimation

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.the_schedulaing_application.Shapes.CalEventColumnShape.EventColumnShape
import com.example.the_schedulaing_application.Shapes.CalEventColumnShape.shapeAnimation.EventBoxAnimation.EventBoxAnimation

class EventColumnShapeAnimation(
    private val animationSpec: AnimationSpec<Float> = tween(200),

    ) : EventBoxAnimation {
    @Composable
    override fun animationShapeAsState(
        selected: Boolean,
        targetPos: Float,
        indentWidth: Dp,
        boxCornerRadius: Dp,
        indentCornerRadius: Dp,
        startPadding: Dp,
        itemNotVisible: () -> Unit
    ): State<Shape> {

        val config = LocalConfiguration.current
        val density = LocalDensity.current

        var topLeftCorner by remember {
            mutableStateOf(true)
        }
        var topRightCorner by remember {
            mutableStateOf(true)
        }

        val indentWidthPx = remember {
            with(density){indentCornerRadius.toPx() + indentWidth.toPx()}
        }

        val indentCornerWidthPx = remember {
            with(density){indentCornerRadius.toPx()}
        }

        val screenWidthPx = remember {
            config.screenWidthDp * density.density//conversion not working
        }

        val adjustment = remember {
            with(density) { (indentCornerRadius.toPx() / 2) - startPadding.toPx() }
        }



        topLeftCorner = if (targetPos <= indentCornerWidthPx) {
            false

        } else  {
            //showIndent = false
            true
        }
        topRightCorner = if (targetPos >= screenWidthPx - indentWidthPx){
            false
        }else {
            true
        }

        return produceState(
            initialValue = EventColumnShape(),
            key1 = targetPos,
            key2 = topLeftCorner,
            key3 = topRightCorner
        ) {

            this.value = this.value.copy(
                curveTopStartCorner = topLeftCorner,
                curveTopEndCorner = topRightCorner,
                position = targetPos - adjustment,
                indentCornerRadius = indentCornerRadius,
                indentWidth = indentWidth
            )

        }


    }

}
