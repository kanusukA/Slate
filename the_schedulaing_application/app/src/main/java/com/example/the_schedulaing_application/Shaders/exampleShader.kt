package com.example.the_schedulaing_application.Shaders

import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


/*
val compositeSksl = RuntimeShader("""
    uniform shader inputShader;
    uniform float height;
    uniform float width;
            
    vec4 main(vec2 coords) {
        vec4 currValue = inputShader.eval(coords);
        float top = height - 100;
        if (coords.y < top) {
            return currValue;
        } else {
            // Avoid blurring edges
            if (coords.x > 1 && coords.y > 1 &&
                    coords.x < (width - 1) &&
                    coords.y < (height - 1)) {
                // simple box blur - average 5x5 grid around pixel
                vec4 boxSum =
                    inputShader.eval(coords + vec2(-2, -2)) + 
                    // ...
                    currValue +
                    // ...
                    inputShader.eval(coords + vec2(2, 2));
                currValue = boxSum / 25;
            }
            
            const vec4 white = vec4(1);
            // top-left corner of label area
            vec2 lefttop = vec2(0, top);
            float lightenFactor = min(1.0, .6 *
                    length(coords - lefttop) /
                    (0.85 * length(vec2(width, 100))));
            // White in upper-left, blended increasingly
            // toward lower-right
            return mix(currValue, white, 1 - lightenFactor);
        }
    }
""")

val compositeRuntimeShader = RenderEffect.createRuntimeShaderEffect(compositeSksl,"inputShader")

@Composable
fun GlassShaderBox(){
    Canvas(modifier = Modifier.fillMaxSize(1.0f)
        .graphicsLayer {
            compositeSksl.setFloatUniform("height",size.height)
            compositeSksl.setFloatUniform("width",size.width)
            renderEffect = compositeRuntimeShader.asComposeRenderEffect()
        }
    ) {


        drawCircle(
            brush = Brush.linearGradient(
                colors = listOf(Color(0xFF7A26D9), Color(0xFFE444E1)),
                start = Offset(450.dp.toPx(), 60.dp.toPx()),
                end = Offset(290.dp.toPx(), 190.dp.toPx()),
                tileMode = TileMode.Clamp
            ),
            center = Offset(375.dp.toPx(), 225.dp.toPx()),
            radius = 100.dp.toPx()
        )
        drawCircle(
            color = Color(0xFFEA357C),
            center = Offset(100.dp.toPx(), 265.dp.toPx()),
            radius = 55.dp.toPx()
        )
        drawCircle(
            brush = Brush.linearGradient(
                colors = listOf(Color(0xFFEA334C), Color(0xFFEC6051)),
                start = Offset(180.dp.toPx(), 125.dp.toPx()),
                end = Offset(230.dp.toPx(), 125.dp.toPx()),
                tileMode = TileMode.Clamp
            ),
            center = Offset(205.dp.toPx(), 125.dp.toPx()),
            radius = 25.dp.toPx()
        )
    }

}

@Preview
@Composable
fun previewGlassBlur(){
    GlassShaderBox()
}*/
