package io.github.kingg22.vibrion.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import io.github.kingg22.vibrion.Icons
import io.github.kingg22.vibrion.Res
import io.github.kingg22.vibrion.filled.PlayArrow
import io.github.kingg22.vibrion.placeholder
import io.github.kingg22.vibrion.ui.theme.VibrionAppTheme
import org.jetbrains.compose.resources.painterResource

@Composable
fun PlaylistCard(playlist: PlaylistItem, onClick: () -> Unit, onPlayClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Playlist image
            Card(
                modifier = Modifier.size(64.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            ) {
                if (playlist.image is Painter) {
                    Image(
                        painter = playlist.image,
                        contentDescription = playlist.contentDescription,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    )
                } else {
                    AsyncImage(
                        model = playlist.image,
                        contentDescription = playlist.contentDescription,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(Res.drawable.placeholder),
                    )
                }
            }

            // Content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceEvenly,
            ) {
                Text(
                    text = playlist.headline,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.basicMarquee(),
                )
                Text(
                    text = playlist.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.basicMarquee(),
                )
            }

            // Play button
            IconButton(
                onClick = onPlayClick,
                modifier = Modifier.size(40.dp),
            ) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = "Play ${playlist.headline}",
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Composable
fun PlaylistCardPlaceholder(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Imagen de la playlist
            ShimmerBox(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp)),
            )

            // Contenido textual simulado
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceEvenly,
            ) {
                // Título
                ShimmerBox(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(18.dp)
                        .clip(RoundedCornerShape(4.dp)),
                )
                // Descripción
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    ShimmerBox(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(14.dp)
                            .clip(RoundedCornerShape(4.dp)),
                    )
                    ShimmerBox(
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .height(14.dp)
                            .clip(RoundedCornerShape(4.dp)),
                    )
                }
            }

            // Botón de reproducción simulado
            ShimmerBox(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
            )
        }
    }
}

@Composable
@Preview
private fun PlaylistScreen() {
    val playlists = listOf(
        PlaylistItem(
            image = painterResource(Res.drawable.placeholder),
            headline = "Headline 1",
            description = "Description duis aute irure dolor in reprehenderit in voluptate velit.",
            contentDescription = "Playlist 1 cover",
        ),
        PlaylistItem(
            image = painterResource(Res.drawable.placeholder),
            headline = "Headline 2",
            description = "Description duis aute irure dolor in reprehenderit in voluptate velit.",
            contentDescription = "Playlist 2 cover",
        ),
        PlaylistItem(
            image = painterResource(Res.drawable.placeholder),
            headline = "Headline 3",
            description = "Description duis aute irure dolor in reprehenderit in voluptate velit.",
            contentDescription = "Playlist 2 cover",
        ),
    )

    VibrionAppTheme {
        LazyColumn {
            items(
                items = playlists,
                key = { it.headline },
            ) { item ->
                PlaylistCard(
                    playlist = item,
                    onClick = {},
                    onPlayClick = {},
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                )
            }
            items(playlists.size) { _ ->
                PlaylistCardPlaceholder(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp))
            }
        }
    }
}
