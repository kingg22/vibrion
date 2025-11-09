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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import io.github.kingg22.vibrion.R
import io.github.kingg22.vibrion.ui.theme.VibrionAppTheme

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
                contentDescription = stringResource(R.string.cover_of, label),
                modifier = imageModifier.width(96.dp).height(96.dp),
                contentScale = ContentScale.Crop,
            )
        } else {
            AsyncImage(
                model = image,
                contentDescription = stringResource(R.string.cover_of, label),
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

@Preview
@Composable
private fun ImageLabelPreview() {
    VibrionAppTheme {
        Column {
            ImageLabel(painterResource(R.drawable.placeholder), "Test")
            Text("Others:")
            ImageLabel(painterResource(R.drawable.placeholder), "Test", Modifier.clip(CircleShape))
            ImageLabel(
                painterResource(R.drawable.placeholder),
                "Test",
                Modifier.clip(RoundedCornerShape(percent = 15)),
            )
        }
    }
}
