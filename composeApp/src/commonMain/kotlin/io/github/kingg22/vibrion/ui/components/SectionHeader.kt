package io.github.kingg22.vibrion.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.kingg22.vibrion.Res
import io.github.kingg22.vibrion.see_more
import io.github.kingg22.vibrion.ui.theme.VibrionAppTheme
import org.jetbrains.compose.resources.stringResource

@Composable
fun SectionHeader(
    text: String,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    onClick: () -> Unit = {},
) {
    Row(
        modifier.fillMaxWidth().padding(16.dp).clip(CircleShape).clickable(onClick = onClick),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Icon(Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription ?: stringResource(Res.string.see_more, text))
    }
}

@Composable
@Preview
private fun TitlePreview() {
    VibrionAppTheme { SectionHeader("Hola") }
}
