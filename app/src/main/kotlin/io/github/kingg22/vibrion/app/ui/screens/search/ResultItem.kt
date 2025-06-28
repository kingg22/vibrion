package io.github.kingg22.vibrion.app.ui.screens.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Explicit
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
import coil3.compose.AsyncImage
import io.github.kingg22.vibrion.app.R
import io.github.kingg22.vibrion.app.domain.model.Single
import io.github.kingg22.vibrion.app.ui.theme.VibrionAppTheme
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

@Composable
fun ResultItem(item: Single, onDownloadClick: () -> Unit, modifier: Modifier = Modifier) {
    ElevatedCard(
        modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp, bottom = 4.dp)
            .clip(CardDefaults.elevatedShape),
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = item.thumbnailUrl,
                    contentDescription = "Cover of " + item.title,
                    modifier = Modifier.size(60.dp),
                )
                Spacer(Modifier.width(16.dp))
                Column {
                    Text(item.title, style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = item.description ?: item.title,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(Modifier.height(4.dp))
                    Row {
                        Text(
                            text = item.duration?.toString() ?: Duration.ZERO.toString(),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Spacer(Modifier.width(4.dp))
                        if (item.explicit == true) {
                            Image(Icons.Filled.Explicit, stringResource(R.string.explicit), Modifier.size(16.dp))
                        }
                    }
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
    val single = Single(
        "",
        "Title",
        "Description duis aute irure dolor in reprehenderit in voluptate velit.",
        "",
        "",
        "2022-01-01",
        3.minutes,
    )
    VibrionAppTheme {
        Column {
            ResultItem(single, {})
            ResultItem(single.copy(explicit = true), {})
        }
    }
}
