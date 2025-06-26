package io.github.kingg22.vibrion.app.ui.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.kingg22.vibrion.app.R
import io.github.kingg22.vibrion.app.ui.theme.VibrionAppTheme

@Composable
fun ResultItem(onDownloadClick: () -> Unit, onPlayClick: () -> Unit, modifier: Modifier = Modifier) {
    ElevatedCard(
        modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
            .clip(CardDefaults.elevatedShape)
            .clickable(onClick = onPlayClick),
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                // Placeholder for geometric shapes TODO change to image
                Box(
                    Modifier
                        .size(60.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                )
                Spacer(Modifier.width(16.dp))
                Column {
                    Text("Title", style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = "Description duis aute irure dolor in reprehenderit in voluptate velit.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Today • 23 min",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            IconButton(onDownloadClick) {
                Icon(Icons.Default.Download, stringResource(R.string.download))
            }
        }
    }
}

@Preview
@Composable
private fun ResultItemPreview() {
    VibrionAppTheme {
        ResultItem({}, {})
    }
}
