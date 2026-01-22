package io.github.kingg22.vibrion.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import io.github.kingg22.vibrion.Icons
import io.github.kingg22.vibrion.Res
import io.github.kingg22.vibrion.download
import io.github.kingg22.vibrion.filled.Download
import io.github.kingg22.vibrion.ui.theme.VibrionAppTheme
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SurpriseFeatureButton(
    featureAvailable: Boolean,
    modifier: Modifier = Modifier,
    button: @Composable BoxScope.() -> Unit,
) {
    var showConfetti by remember { mutableStateOf(false) }

    LaunchedEffect(featureAvailable) {
        if (featureAvailable) {
            showConfetti = true
            delay(1200)
            showConfetti = false
        }
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        AnimatedVisibility(
            visible = featureAvailable,
            label = "Surprise Feature",
            enter = fadeIn(tween(600)) +
                scaleIn(
                    initialScale = 0.3f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow,
                    ),
                ),
        ) {
            button()
        }

        if (showConfetti) {
            ConfettiBurst(modifier = Modifier.matchParentSize())
        }
    }
}

@Composable
fun ConfettiBurst(modifier: Modifier = Modifier) {
    val particles = 14
    val anim = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        anim.snapTo(0f)
        anim.animateTo(
            targetValue = 1f,
            animationSpec = tween(900, easing = LinearOutSlowInEasing),
        )
    }

    Canvas(modifier = modifier) {
        val minDimension = size.minDimension
        val maxRadius = minDimension * 0.6f

        repeat(particles) { i ->
            val angle = (2 * PI / particles) * i
            val radius = anim.value * maxRadius

            val offset = Offset(
                x = center.x + cos(angle).toFloat() * radius,
                y = center.y + sin(angle).toFloat() * radius,
            )

            drawCircle(
                color = confettiColors[i % confettiColors.size],
                radius = (minDimension * 0.08f * (1 - anim.value)).coerceAtLeast(minDimension * 0.03f),
                center = offset,
            )
        }
    }
}

private val confettiColors = listOf(
    Color.Yellow,
    Color.Magenta,
    Color.Cyan,
    Color.Red,
)

@Preview
@Composable
private fun SurpriseFeatureButtonPreview() {
    var showConfetti by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(2000)
        showConfetti = true
    }

    VibrionAppTheme {
        Column {
            SurpriseFeatureButton(featureAvailable = showConfetti) {
                Button(onClick = {}) { Text("Nueva Función ✨") }
            }
            SurpriseFeatureButton(featureAvailable = showConfetti) {
                IconButton(onClick = {}) {
                    Icon(Icons.Filled.Download, stringResource(Res.string.download))
                }
            }
        }
    }
}
