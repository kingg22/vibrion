package io.github.kingg22.vibrion.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.kingg22.vibrion.ui.theme.VibrionAppTheme

@Composable
fun ShimmerBox(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
        ),
        label = "shimmerOffset",
    )

    val brush = Brush.linearGradient(
        colors = listOf(
            Color.Gray.copy(alpha = 0.2f),
            Color.Gray.copy(alpha = 0.4f),
            Color.Gray.copy(alpha = 0.2f),
        ),
        start = Offset(translateAnim, translateAnim),
        end = Offset(translateAnim + 200f, translateAnim + 200f),
    )

    Box(modifier = modifier.background(brush, RoundedCornerShape(8.dp)))
}

@Composable
fun ShimmeringSongCard(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        ShimmerBox(Modifier.size(64.dp))
        Spacer(Modifier.width(12.dp))
        Column {
            ShimmerBox(
                Modifier
                    .width(150.dp)
                    .height(16.dp),
            )
            Spacer(Modifier.height(8.dp))
            ShimmerBox(
                Modifier
                    .width(100.dp)
                    .height(14.dp),
            )
        }
    }
}

@Preview
@Composable
private fun SongCardPlaceholderPreview() {
    VibrionAppTheme {
        ShimmeringSongCard()
    }
}
