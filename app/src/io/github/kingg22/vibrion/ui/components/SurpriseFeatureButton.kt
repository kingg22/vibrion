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
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
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
import androidx.compose.ui.unit.dp
import io.github.kingg22.vibrion.ui.theme.VibrionAppTheme
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SurpriseFeatureButton(featureAvailable: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    var showConfetti by remember { mutableStateOf(false) }

    // Se dispara cuando la característica se habilita
    LaunchedEffect(featureAvailable) {
        if (featureAvailable) {
            showConfetti = true
            delay(1200) // Confetti se oculta luego del efecto
            showConfetti = false
        }
    }

    Box(modifier, contentAlignment = Alignment.Center) {
        AnimatedVisibility(
            visible = featureAvailable,
            enter = fadeIn(animationSpec = tween(600)) +
                scaleIn(
                    initialScale = 0.3f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow,
                    ),
                ),
        ) {
            Button(onClick = onClick) { Text("Nueva Función ✨") }
        }

        // Confetti / Estrellas estilo pop
        if (showConfetti) {
            ConfettiBurst()
        }
    }
}

@Composable
fun ConfettiBurst(modifier: Modifier = Modifier) {
    val particles = 15
    val anim = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        anim.animateTo(
            targetValue = 1f,
            animationSpec = tween(900, easing = LinearOutSlowInEasing),
        )
    }

    Canvas(modifier = modifier.size(150.dp)) {
        repeat(particles) { i ->
            val angle = (360f / particles) * i
            val radius = anim.value * 70.dp.toPx()
            val x = center.x + cos(Math.toRadians(angle.toDouble())).toFloat() * radius
            val y = center.y + sin(Math.toRadians(angle.toDouble())).toFloat() * radius

            drawCircle(
                color = listOf(Color.Yellow, Color.Magenta, Color.Cyan, Color.Red).random(),
                radius = (6.dp.toPx() * (1 - anim.value)).coerceAtLeast(2f),
                center = Offset(x, y),
            )
        }
    }
}

@Preview
@Composable
private fun SurpriseFeatureButtonPreview() {
    var showConfetti by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(2000)
        showConfetti = true
    }

    VibrionAppTheme {
        SurpriseFeatureButton(featureAvailable = showConfetti, onClick = {})
    }
}
