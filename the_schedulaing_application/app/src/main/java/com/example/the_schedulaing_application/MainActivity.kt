package com.example.the_schedulaing_application

import android.app.Activity
import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import com.example.the_schedulaing_application.data.fb.GoogleSignInClient
import com.example.the_schedulaing_application.element.Navigation.NavConductor
import com.example.the_schedulaing_application.ui.theme.SlateColorScheme
import com.example.the_schedulaing_application.ui.theme.The_schedulaing_applicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            The_schedulaing_applicationTheme {
                val googleAuthUiClient = GoogleSignInClient(LocalContext.current)

                // status bar color
                DisposableEffect(isSystemInDarkTheme()) {
                    window.statusBarColor = SlateColorScheme.surface.toArgb()
                    onDispose {  }
                }


                // Top Padding for cutout
                Surface(modifier =
                    Modifier.windowInsetsPadding(WindowInsets.displayCutout)
                )
                {
                    NavConductor(googleAuthUiClient)
                }



            }
        }
    }
}






