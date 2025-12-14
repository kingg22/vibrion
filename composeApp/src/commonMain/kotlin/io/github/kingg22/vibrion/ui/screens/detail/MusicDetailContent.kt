package io.github.kingg22.vibrion.ui.screens.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import io.github.kingg22.vibrion.R
import io.github.kingg22.vibrion.albums
import io.github.kingg22.vibrion.back
import io.github.kingg22.vibrion.cover_of
import io.github.kingg22.vibrion.domain.model.DownloadableAlbum
import io.github.kingg22.vibrion.domain.model.DownloadableItem
import io.github.kingg22.vibrion.domain.model.DownloadablePlaylist
import io.github.kingg22.vibrion.domain.model.DownloadableSingle
import io.github.kingg22.vibrion.ui.components.SurpriseFeatureButton
import io.github.kingg22.vibrion.download
import io.github.kingg22.vibrion.play_track
import io.github.kingg22.vibrion.playlists
import io.github.kingg22.vibrion.songs
import io.github.kingg22.vibrion.ui.theme.VibrionAppTheme
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Duration.Companion.seconds

@Composable
fun MusicDetailContent(
    detail: DownloadableItem,
    canDownload: Boolean,
    onBackClick: () -> Unit,
    onDownloadClick: (item: DownloadableItem) -> Unit,
    onTrackClick: (item: DownloadableItem) -> Unit,
    onTrackPlayClick: (item: DownloadableItem) -> Unit,
    onPlayClick: (item: DownloadableItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val type = remember(detail) {
        when (detail) {
            is DownloadableAlbum -> R.string.albums
            is DownloadablePlaylist -> R.string.playlists
            is DownloadableSingle -> R.string.songs
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(type), color = MaterialTheme.colorScheme.onSurface) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.back))
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
        ) {
            // Cabecera con imagen y datos del álbum
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    AsyncImage(
                        model = detail.thumbnailUrl,
                        contentDescription = stringResource(R.string.cover_of, detail.title),
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop,
                    )

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = detail.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                    )

                    Text(
                        text = detail.artists.joinToString { it.name },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )

                    val descriptionText = detail.description
                        ?: detail.album?.let { "Album: $it" }
                        ?: ""

                    val releaseText = detail.releaseDate
                        ?.replaceFirstChar { it.uppercase() }
                        ?.let { "Released: $it" }
                        ?: ""

                    val durationText = detail.duration
                        ?.let { "Duration: $it" }
                        ?: ""

                    listOf(descriptionText, releaseText, durationText)
                        .filter { it.isNotBlank() }
                        .forEach { text ->
                            Text(
                                text = text,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }

                    Spacer(Modifier.height(16.dp))

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        IconButton(onClick = { onPlayClick(detail) }) {
                            Icon(
                                Icons.Default.PlayCircle,
                                contentDescription = stringResource(R.string.play_track, detail.title),
                                modifier = Modifier.size(48.dp),
                            )
                        }
                        Spacer(Modifier.width(8.dp))
                        SurpriseFeatureButton(canDownload) {
                            Button(onClick = { onDownloadClick(detail) }, enabled = canDownload) {
                                Icon(Icons.Default.Download, contentDescription = stringResource(R.string.download))
                                Spacer(Modifier.width(8.dp))
                                Text(stringResource(R.string.download))
                            }
                        }
                    }
                }
            }

            if (detail is DownloadableItem.ItemWithTracks) {
                // Lista de canciones
                items(detail.tracks, key = { it.id }) { track ->
                    TrackListItem(
                        track = track,
                        onPlayClick = { onTrackPlayClick(track) },
                        onClick = { onTrackClick(track) },
                        onDownload = { onDownloadClick(track) },
                        canDownload = canDownload,
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
fun TrackListItem(
    track: DownloadableItem,
    canDownload: Boolean,
    onPlayClick: () -> Unit,
    onClick: () -> Unit,
    onDownload: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ListItem(
        headlineContent = {
            Text(track.title)
        },
        supportingContent = {
            Text("${track.artists.joinToString { it.name }} · ${track.duration ?: ""}")
        },
        leadingContent = {
            AsyncImage(
                track.thumbnailUrl,
                contentDescription = stringResource(R.string.cover_of, track.title),
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp)),
            )
        },
        trailingContent = {
            Row {
                SurpriseFeatureButton(canDownload) {
                    IconButton(onClick = onDownload, enabled = canDownload) {
                        Icon(Icons.Default.Download, stringResource(R.string.download))
                    }
                }
                IconButton(onClick = onPlayClick) {
                    Icon(Icons.Default.PlayCircle, stringResource(R.string.play_track, track.title))
                }
            }
        },
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp),
    )
}

@Preview
@Composable
private fun MusicDetailContentPreview() {
    val albumItem = DownloadableAlbum(
        id = "1",
        title = "Sample Album",
        description = "This is a sample album description.",
        thumbnailUrl = "https://via.placeholder.com/412x340",
        releaseDate = "2023-01-01",
        duration = 1800.seconds,
        artists = emptyList(),
        tracks = listOf(DownloadableSingle.previewDefault),
        upc = null,
    )
    VibrionAppTheme {
        MusicDetailContent(
            albumItem,
            canDownload = true,
            onBackClick = {},
            onDownloadClick = {},
            onTrackClick = {},
            onPlayClick = {},
            onTrackPlayClick = {},
        )
    }
}
