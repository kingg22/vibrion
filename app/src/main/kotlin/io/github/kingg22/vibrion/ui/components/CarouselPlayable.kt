package io.github.kingg22.vibrion.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import io.github.kingg22.vibrion.R
import io.github.kingg22.vibrion.ui.theme.VibrionAppTheme

@Composable
fun SongCard(carouselItem: CarouselItem, onClick: () -> Unit, onPlayClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier.aspectRatio(1f).clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8E8E8)),
    ) {
        Box(Modifier.fillMaxSize()) {
            // Background Image
            if (carouselItem.painter is Painter) {
                Image(
                    painter = carouselItem.painter,
                    contentDescription = stringResource(
                        R.string.cover_of,
                        carouselItem.contentDescription ?: carouselItem.titleSong,
                    ),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
            } else {
                AsyncImage(
                    model = carouselItem.painter,
                    contentDescription = stringResource(
                        R.string.cover_of,
                        carouselItem.contentDescription ?: carouselItem.titleSong,
                    ),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.placeholder),
                )
            }

            // Semi-transparent overlay
            Box(
                Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.3f)).clickable(onClick = onClick),
            )

            // Position indicator
            Surface(
                Modifier.padding(8.dp).size(40.dp),
                shape = CircleShape,
                color = Color.Gray.copy(alpha = 0.7f),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = when (carouselItem.position) {
                            1 -> "1st"
                            2 -> "2nd"
                            3 -> "3rd"
                            else -> "${carouselItem.position}th"
                        },
                        color = Color.White,
                    )
                }
            }

            // Play button and text at the bottom
            Column(Modifier.align(Alignment.BottomStart).padding(16.dp)) {
                // artists
                Text(
                    text = carouselItem.artistName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )

                // title
                Text(
                    text = carouselItem.titleSong,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f),
                )

                Spacer(Modifier.height(8.dp))

                IconButton(
                    onClick = onPlayClick,
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = Color.White.copy(alpha = 0.9f),
                            shape = CircleShape,
                        ),
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = stringResource(R.string.play_track, carouselItem.titleSong),
                        tint = Color.Black,
                    )
                }
            }
        }
    }
}

@Composable
@Preview
private fun CarouselPreview() {
    val items = List(10) {
        CarouselItem(
            id = it.toString(),
            position = it,
            painter = painterResource(R.drawable.placeholder),
            contentDescription = null,
            artistName = "Artist $it",
            titleSong = "Title $it",
        )
    }
    VibrionAppTheme {
        Column(Modifier.padding(16.dp)) {
            // Song cards
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(items) {
                    SongCard(
                        carouselItem = it,
                        modifier = Modifier.width(280.dp),
                        onClick = {},
                        onPlayClick = {},
                    )
                }
            }
        }
    }
}
