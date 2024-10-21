package com.example.the_schedulaing_application.Shapes.CalEventColumnShape

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.VectorProperty
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.the_schedulaing_application.ui.theme.Yellow20

class EventColumnShape (

    val position: Float = 0f,
    val indentWidth: Dp = 10.dp,
    val boxCornerRadius: Dp = 25.dp,
    val indentCornerRadius: Dp = 40.dp,
    val curveTopStartCorner: Boolean = true,
    val curveTopEndCorner: Boolean = true
): Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {

        val boxCornerRadiusPx = with(density) {boxCornerRadius.toPx()}
        val indentWidthPx = with(density) {indentWidth.toPx()}
        val indentCornerRadiusPx = with(density) {indentCornerRadius.toPx()}

        val path = eventBoxShapePath(
            size = size,
            topStart = if(curveTopStartCorner){boxCornerRadiusPx}else{0f},
            topEnd = if(curveTopEndCorner){boxCornerRadiusPx}else{0f},
            bottomStart = boxCornerRadiusPx,
            bottomEnd = boxCornerRadiusPx,
            startPosition = position,
            indentWidth = indentWidthPx,
            indentCornerRadius = indentCornerRadiusPx
        )

        return Outline.Generic(path)
    }

    fun copy(
        position: Float = 0f,
        indentWidth: Dp = 10.dp,
        curveTopStartCorner: Boolean = true,
        curveTopEndCorner: Boolean = true,
        indentCornerRadius: Dp = 0.dp
    ) = EventColumnShape(
        curveTopStartCorner = curveTopStartCorner,
        curveTopEndCorner = curveTopEndCorner,
        position = position,
        indentWidth = indentWidth,
        indentCornerRadius = indentCornerRadius
    )
}

private fun indentPath(
    startPosition: Float,
    indentWidth: Float,
    indentCornerRadius : Float
): Path = Path().apply {
    arcTo(
        rect =  Rect(
            Offset(startPosition - (indentCornerRadius/2),-indentCornerRadius), Size(indentCornerRadius,indentCornerRadius)
        ),
        startAngleDegrees = 90f,
        sweepAngleDegrees = -90f,
        forceMoveTo = true
    )

    lineTo(startPosition + indentWidth, -(indentCornerRadius/2))

    arcTo(
        rect = Rect(
            Offset(startPosition + indentWidth,-indentCornerRadius),Size(indentCornerRadius,indentCornerRadius)
        ),
        startAngleDegrees = 180f,
        sweepAngleDegrees = -90f,
        forceMoveTo = false
    )



}

private fun boxPath(
    size: Size,
    topStart:  Float,
    topEnd: Float,
    bottomStart: Float,
    bottomEnd: Float
): Path = Path().apply {

    addRoundRect(
        RoundRect(
            Rect(Offset(0f,0f),Size(size.width,size.height)),
            topLeft = CornerRadius(topStart),
            topRight = CornerRadius(topEnd),
            bottomLeft = CornerRadius(bottomStart),
            bottomRight = CornerRadius(bottomEnd)
        )
    )

    close()
}

private fun eventBoxShapePath(
    size: Size,
    topStart:  Float,
    topEnd: Float,
    bottomStart: Float,
    bottomEnd: Float,
    startPosition: Float,
    indentWidth: Float,
    indentCornerRadius : Float
):Path {
    val path1 = indentPath(
        startPosition, indentWidth, indentCornerRadius
    )
    val path2 = boxPath(
        size,
        topStart,
        topEnd,
        bottomStart,
        bottomEnd
    )
    return Path().apply { op(path2,path1, PathOperation.Union) }
}



@Preview
@Composable
fun PreviewECS(){
    /*Box(modifier = Modifier.fillMaxSize()){
        Canvas(
            modifier = Modifier.
                align(Alignment.Center)
                .fillMaxWidth()
                .height(400.dp)
        ) {
            drawPath(
                eventBoxShapePath(
                    size,
                    80f,
                    90f,
                    300f,
                    200f
                ),
                color = Color.Red,
                style = Stroke(10f)
            )
        }
    }*/
}