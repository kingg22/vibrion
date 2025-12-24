package io.github.kingg22.vibrion.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import io.github.kingg22.vibrion.Res
import io.github.kingg22.vibrion.cover_of
import io.github.kingg22.vibrion.placeholder
import io.github.kingg22.vibrion.ui.theme.VibrionAppTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ImageLabel(image: Any, label: String, modifier: Modifier = Modifier, imageModifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.width(96.dp).height(124.dp),
    ) {
        if (image is Painter) {
            Image(
                image,
                contentDescription = stringResource(Res.string.cover_of, label),
                modifier = imageModifier.width(96.dp).height(96.dp),
                contentScale = ContentScale.Crop,
            )
        } else {
            AsyncImage(
                model = image,
                contentDescription = stringResource(Res.string.cover_of, label),
                modifier = imageModifier.width(96.dp).height(96.dp),
                contentScale = ContentScale.Crop,
            )
        }

        Text(
            label,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.width(96.dp).height(20.dp),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun ImageLabelPlaceholder(modifier: Modifier = Modifier, imageModifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .width(96.dp)
            .height(124.dp),
    ) {
        // Imagen simulada
        ShimmerBox(
            modifier = imageModifier
                .width(96.dp)
                .height(96.dp)
                .clip(RoundedCornerShape(8.dp)),
        )

        // Etiqueta simulada (texto)
        ShimmerBox(
            modifier = Modifier
                .width(96.dp)
                .height(20.dp)
                .clip(RoundedCornerShape(4.dp)),
        )
    }
}

@Preview(
    name = "ImageLabel - Default",
    showBackground = true,
)
@Composable
private fun ImageLabelDefaultPreview() {
    VibrionAppTheme {
        ImageLabel(painterResource(Res.drawable.placeholder), "Test")
    }
}

@Preview(
    name = "ImageLabel - Circle Shape",
    showBackground = true,
)
@Composable
private fun ImageLabelCirclePreview() {
    VibrionAppTheme {
        ImageLabel(
            image = painterResource(Res.drawable.placeholder),
            label = "Test",
            modifier = Modifier.clip(CircleShape),
        )
    }
}

@Preview(
    name = "ImageLabel - Rounded Shape",
    showBackground = true,
)
@Composable
private fun ImageLabelRoundedPreview() {
    VibrionAppTheme {
        ImageLabel(
            image = painterResource(Res.drawable.placeholder),
            label = "Test",
            modifier = Modifier.clip(RoundedCornerShape(percent = 15)),
        )
    }
}

@Preview(
    name = "ImageLabel - Placeholder",
    showBackground = true,
)
@Composable
private fun ImageLabelPlaceholder1Preview() {
    VibrionAppTheme {
        ImageLabelPlaceholder()
    }
}
