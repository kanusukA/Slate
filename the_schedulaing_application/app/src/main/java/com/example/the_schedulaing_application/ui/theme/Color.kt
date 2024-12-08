package com.example.the_schedulaing_application.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Yellow20 = Color(0xFFFFF7D1)
val Beige20 = Color(0xFFFFECC8)
val Orange20 = Color(0xFFFFD09B)
val Pink20 = Color(0xFFFFB0B0)

// Event Colors
val eventRed = Color(0xFFFF204E)
val eventMaroon = Color(0xFFA0153E)
val eventMarron80 = Color(0xFF5D0E41)
val eventBlue = Color(0xFF00224D)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

@Composable
fun AccentColor() = if(isSystemInDarkTheme()){
    Color.Black.copy(alpha = 0.5f)
}else{
    Color.White.copy(alpha = 0.5f)
}

// SlateColorTheme
val SlateColorScheme = lightColorScheme(
    primary = Color(0xff130f95),
    onPrimary = Color(0xffe9d8ff),
    primaryContainer = Color(0xff918ef6),
    onPrimaryContainer = Color(0xff130f95),
    secondary = Color(0xff3c1c87),
    onSecondary = Color(0xffe9d8ff),
    secondaryContainer = Color(0xffab90ea),
    onSecondaryContainer = Color(0xff3c1c87),
    surfaceContainerLowest = Color(0xfff7f3ff),
    surfaceContainerLow = Color(0xfff1e7ff),
    surface = Color(0xffe9d8ff),
    surfaceContainerHigh = Color(0xffe6d0fd),
    surfaceContainerHighest = Color(0xffe3c8fa),
    onSurface = Color(0xff141b41),
    onErrorContainer = Color(0xffFF7878)
)