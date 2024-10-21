package com.example.the_schedulaing_application.element.CusModifier

import android.graphics.RenderEffect
import android.graphics.Shader
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer

fun Modifier.glassBlur() = graphicsLayer {
    renderEffect = RenderEffect.createBlurEffect(50f,50f,Shader.TileMode.REPEAT).asComposeRenderEffect()
}