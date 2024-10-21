package com.example.the_schedulaing_application.Shapes.CalEventColumnShape.shapeAnimation.EventBoxAnimation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

interface EventBoxAnimation {
    @Composable
    fun animationShapeAsState(
        selected: Boolean,
        targetPos: Float,
        indentWidth: Dp,
        boxCornerRadius: Dp,
        indentCornerRadius:Dp,
        startPadding: Dp,
        itemNotVisible: () -> Unit
    ):State<Shape>
}