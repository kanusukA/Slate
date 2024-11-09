package com.example.the_schedulaing_application.element.components.sentientTextBox

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp

interface SentientTextBoxImpl {
    @Composable
    fun sentientTextBoxAsState(
        indentWidth: Dp,
        indentHeight: Dp,
        indentPaddingFromStart: Dp,
        indentCornerRadius: Dp,
        cornerRadius: Dp,
        showIndent: Boolean
        ): State<Shape>
}