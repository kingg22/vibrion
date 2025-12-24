package io.github.kingg22.vibrion.ui.screens.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import io.github.kingg22.vibrion.Res
import io.github.kingg22.vibrion.cover_of
import io.github.kingg22.vibrion.domain.model.DownloadableItem
import io.github.kingg22.vibrion.domain.model.DownloadableSingle
import io.github.kingg22.vibrion.download
import io.github.kingg22.vibrion.play_track
import io.github.kingg22.vibrion.ui.components.ShimmerBox
import io.github.kingg22.vibrion.ui.components.SurpriseFeatureButton
import org.jetbrains.compose.resources.stringResource

@Composable
fun FeaturedItem(
    item: DownloadableItem,
    canDownload: Boolean,
    onDownloadClick: () -> Unit,
    onCardClick: () -> Unit,
    onPlayClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
            .clip(CardDefaults.elevatedShape)
            .clickable(onClick = onCardClick),
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AsyncImage(
                    item.thumbnailUrl,
                    stringResource(Res.string.cover_of, item.title),
                    Modifier.size(60.dp),
                )

                Spacer(Modifier.width(16.dp))

                Column {
                    Text(
                        item.title,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = item.artists.joinToString { it.name },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = item.releaseDate ?: item.duration?.toString() ?: item.description ?: "",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            SurpriseFeatureButton(canDownload) {
                IconButton(onDownloadClick, enabled = canDownload) {
                    Icon(Icons.Default.Download, stringResource(Res.string.download))
                }
            }

            if (item is DownloadableItem.StreamableItem) {
                IconButton(onPlayClick) {
                    Icon(Icons.Default.PlayCircle, stringResource(Res.string.play_track, item.title))
                }
            }
        }
    }
}

@Composable
fun ListItem(
    title: String,
    supportingText: String,
    image: Any,
    canDownload: Boolean,
    onClick: () -> Unit,
    onDownloadClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ListItem(
        headlineContent = {
            Text(
                title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        supportingContent = {
            Text(
                supportingText,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )
        },
        leadingContent = {
            AsyncImage(image, stringResource(Res.string.cover_of, title), Modifier.size(60.dp))
        },
        trailingContent = {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End) {
                SurpriseFeatureButton(canDownload) {
                    IconButton(onClick = onDownloadClick, enabled = canDownload) {
                        Icon(Icons.Default.Download, stringResource(Res.string.download))
                    }
                }
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
            .clip(CardDefaults.shape)
            .clickable(onClick = onClick),
    )
}

@Composable
fun ListItemCard(
    title: String,
    image: Any?,
    subtitles: List<String>,
    canDownload: Boolean,
    onClick: () -> Unit,
    onDownloadClick: () -> Unit,
    onPlayClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                image,
                stringResource(Res.string.cover_of, title),
                Modifier.size(80.dp),
            )

            Spacer(Modifier.width(10.dp))

            // Content
            Column(
                Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                subtitles.forEach { subtitle ->
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            SurpriseFeatureButton(canDownload) {
                IconButton(onClick = onDownloadClick, enabled = canDownload) {
                    Icon(Icons.Default.Download, stringResource(Res.string.download))
                }
            }

            IconButton(onPlayClick) {
                Icon(Icons.Default.PlayCircle, stringResource(Res.string.play_track, title))
            }
        }
    }
}

@Composable
fun ListItemCardPlaceholder(modifier: Modifier = Modifier) {
    Card(
        modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Imagen de portada simulada
            ShimmerBox(
                modifier = Modifier
                    .size(80.dp)
                    .clip(MaterialTheme.shapes.medium),
            )

            Spacer(Modifier.width(10.dp))

            // Columnas simulando título y subtítulos
            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                // Placeholder para el título
                ShimmerBox(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(18.dp)
                        .clip(MaterialTheme.shapes.small),
                )

                // Placeholder para subtítulos (ejemplo: 2 líneas)
                repeat(2) {
                    ShimmerBox(
                        modifier = Modifier
                            .fillMaxWidth(0.5f + it * 0.2f)
                            .height(14.dp)
                            .clip(MaterialTheme.shapes.small),
                    )
                }
            }

            // Botones de acción simulados
            repeat(2) {
                ShimmerBox(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape),
                )
            }
        }
    }
}

@Preview
@Composable
private fun FeaturedItemPreview() {
    FeaturedItem(
        item = DownloadableSingle.previewDefault,
        canDownload = true,
        onDownloadClick = {},
        onCardClick = {},
        onPlayClick = {},
    )
}

@Preview
@Composable
private fun ListItemPreview() {
    ListItem(
        title = "List item",
        supportingText = "Supporting line text lorem ipsum dolor sit amet, consectetur.",
        image = DownloadableSingle.DEFAULT_THUMBNAIL_URL,
        canDownload = true,
        onClick = {},
        onDownloadClick = {},
    )
}

@Preview
@Composable
private fun ListItemCardPreview() {
    ListItemCard(
        title = "List item card",
        image = DownloadableSingle.DEFAULT_THUMBNAIL_URL,
        subtitles = listOf(
            "Supporting line text lorem ipsum dolor sit amet, consectetur.",
            "Another supporting line",
        ),
        canDownload = true,
        onClick = {},
        onDownloadClick = {},
        onPlayClick = {},
    )
}

@Preview
@Composable
private fun ListItemMultiCardsPreview() {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(10) {
            ListItemCard(
                title = "List item card",
                image = DownloadableSingle.DEFAULT_THUMBNAIL_URL,
                subtitles = listOf(
                    "Supporting line text lorem ipsum dolor sit amet, consectetur.",
                    "Another supporting line",
                ),
                canDownload = true,
                onClick = {},
                onDownloadClick = {},
                onPlayClick = {},
            )
        }
    }
}

@Preview
@Composable
private fun ListItemCardPlaceholderPreview() {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(10) {
            ListItemCardPlaceholder()
        }
    }
}
