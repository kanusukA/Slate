package com.example.the_schedulaing_application.custom


import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.node.DrawModifierNode
import kotlinx.coroutines.launch


private class ScaleNode(
    val interactionSource: InteractionSource,
): Modifier.Node(), DrawModifierNode{

    var currentPressPosition: Offset = Offset.Zero
    val animatedScalePercentage = Animatable(1f)

    private suspend fun animateToPressed(pressPosition: Offset){
        currentPressPosition = pressPosition

        animatedScalePercentage.animateTo(0.9f, spring())
    }

    private suspend fun animateToRest(){
        animatedScalePercentage.animateTo(1f, spring())

    }

    override fun onAttach() {
        coroutineScope.launch {
            interactionSource.interactions.collect{interaction ->
                when(interaction){
                    is PressInteraction.Press -> animateToPressed(interaction.pressPosition)
                    is PressInteraction.Release -> animateToRest()
                    is PressInteraction.Cancel -> animateToRest()
                }
            }
        }
    }

    override fun ContentDrawScope.draw() {

        scale(
            scale = animatedScalePercentage.value,
            pivot = currentPressPosition
        ) {
            this@draw.drawContent()
        }
    }

}

private class ScaleWithCircleNode(
    val interactionSource: InteractionSource,
): Modifier.Node(), DrawModifierNode{

    var currentPressPosition: Offset = Offset.Zero
    val animatedScalePercentage = Animatable(1f)

    private suspend fun animateToPressed(pressPosition: Offset){
        currentPressPosition = pressPosition

        animatedScalePercentage.animateTo(0.9f, spring())
    }

    private suspend fun animateToRest(){
        animatedScalePercentage.animateTo(1f, spring())

    }

    override fun onAttach() {
        coroutineScope.launch {
            interactionSource.interactions.collect{interaction ->
                when(interaction){
                    is PressInteraction.Press -> animateToPressed(interaction.pressPosition)
                    is PressInteraction.Release -> animateToRest()
                    is PressInteraction.Cancel -> animateToRest()
                }
            }
        }
    }

    override fun ContentDrawScope.draw() {

        drawCircle(
            color = Color.Black.copy(alpha = 1f - animatedScalePercentage.value),
            radius = (size.minDimension / 2) + ((1f - animatedScalePercentage.value) * 200),
            center = currentPressPosition
        )



        scale(
            scale = animatedScalePercentage.value,
            pivot = currentPressPosition
        ) {
            this@draw.drawContent()
        }
    }

}

object ScaleWithCircleIndication : IndicationNodeFactory {
    override fun create(interactionSource: InteractionSource): DelegatableNode {
        return ScaleWithCircleNode(interactionSource)
    }

    override fun equals(other: Any?): Boolean = other === ScaleIndication
    override fun hashCode() = 100
}


object ScaleIndication : IndicationNodeFactory {
    override fun create(interactionSource: InteractionSource): DelegatableNode {
        return ScaleNode(interactionSource)
    }

    override fun equals(other: Any?): Boolean = other === ScaleIndication
    override fun hashCode() = 100
}