package io.github.kingg22.vibrion.ui.screens.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import io.github.kingg22.vibrion.R
import io.github.kingg22.vibrion.domain.model.ArtistInfo
import io.github.kingg22.vibrion.domain.model.DownloadableItem
import io.github.kingg22.vibrion.domain.model.DownloadableSingle
import io.github.kingg22.vibrion.domain.model.ModelType
import io.github.kingg22.vibrion.ui.components.ImageLabel
import io.github.kingg22.vibrion.ui.components.SectionHeader
import io.github.kingg22.vibrion.ui.getModelType
import io.github.kingg22.vibrion.ui.items
import io.github.kingg22.vibrion.ui.theme.VibrionAppTheme
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun SearchContent(
    singles: List<DownloadableItem>,
    playlists: List<DownloadableItem>,
    albums: List<DownloadableItem>,
    artists: LazyPagingItems<ArtistInfo>,
    canDownload: Boolean,
    onPlayClick: (item: DownloadableItem) -> Unit,
    onDownloadClick: (item: DownloadableItem) -> Unit,
    onDetailClick: (type: ModelType, id: String) -> Unit,
    onSectionClick: (type: ModelType) -> Unit,
    modifier: Modifier = Modifier,
) {
    val playlistsTitle = stringResource(R.string.playlists)
    val songsTitle = stringResource(R.string.songs)
    val albumsTitle = stringResource(R.string.albums)
    val artistsTitle = stringResource(R.string.artists)

    LazyColumn(modifier.fillMaxSize()) {
        item(songsTitle) {
            SectionHeader(songsTitle, onClick = { onSectionClick(ModelType.SINGLE) })
            Spacer(Modifier.height(8.dp))
        }

        // List of elevated items
        items(singles, key = { it.id }) { item ->
            FeaturedItem(
                item,
                canDownload = canDownload,
                onDownloadClick = { onDownloadClick(item) },
                onCardClick = { onDetailClick(item.getModelType(), item.id) },
                onPlayClick = { onPlayClick(item) },
            )
            Spacer(Modifier.height(12.dp))
        }

        item(playlistsTitle) {
            SectionHeader(playlistsTitle, onClick = { onSectionClick(ModelType.PLAYLIST) })
            Spacer(Modifier.height(8.dp))
        }

        items(playlists, key = { it.id }) { item ->
            ListItem(
                title = item.title,
                supportingText =
                item.artists.joinToString { a -> a.name }.takeIf { l -> l.isNotEmpty() } ?: item.album ?: "",
                image = item.thumbnailUrl ?: DownloadableSingle.DEFAULT_THUMBNAIL_URL,
                canDownload = canDownload,
                onClick = { onDetailClick(item.getModelType(), item.id) },
                onDownloadClick = { onDownloadClick(item) },
            )
            Spacer(Modifier.height(4.dp))
        }

        item(albumsTitle) {
            SectionHeader(albumsTitle, onClick = { onSectionClick(ModelType.ALBUM) })
            Spacer(Modifier.height(8.dp))
        }

        items(albums, key = { it.id }) { item ->
            FeaturedItem(
                item,
                canDownload = canDownload,
                onDownloadClick = { onDownloadClick(item) },
                onCardClick = { onDetailClick(item.getModelType(), item.id) },
                onPlayClick = { onPlayClick(item) },
            )
            Spacer(Modifier.height(12.dp))
        }

        item(artistsTitle) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp).clip(CircleShape),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(artistsTitle, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(8.dp))
        }

        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(124.dp)
                    .padding(start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
                verticalAlignment = Alignment.Top,
            ) {
                if (artists.loadState.refresh == LoadState.Loading) {
                    item { CircularProgressIndicator(modifier = Modifier.fillMaxSize().wrapContentSize()) }
                }

                items(artists, key = { it.id }) { artist ->
                    ImageLabel(
                        image = artist.pictureUrl ?: DownloadableSingle.DEFAULT_THUMBNAIL_URL,
                        label = artist.name,
                        modifier = Modifier.clickable(onClick = { onDetailClick(ModelType.ARTIST, artist.id) }),
                        imageModifier = Modifier.clip(CircleShape),
                    )
                }

                if (artists.loadState.append == LoadState.Loading) {
                    item {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentWidth(Alignment.CenterHorizontally),
                        )
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
@Preview
private fun SearchContentPreview() {
    val pagingData = PagingData.empty<ArtistInfo>(
        LoadStates(
            LoadState.Loading,
            LoadState.Loading,
            LoadState.Loading,
        ),
    )
    val fakeFlow = MutableStateFlow(pagingData)
    val listItems = fakeFlow.collectAsLazyPagingItems()

    VibrionAppTheme {
        Surface {
            SearchContent(
                singles = listOf(DownloadableSingle.previewDefault),
                playlists = listOf(DownloadableSingle.previewDefault),
                albums = listOf(DownloadableSingle.previewDefault),
                artists = listItems,
                canDownload = true,
                onDownloadClick = {},
                onDetailClick = { _, _ -> },
                onSectionClick = {},
                onPlayClick = {},
            )
        }
    }
}
