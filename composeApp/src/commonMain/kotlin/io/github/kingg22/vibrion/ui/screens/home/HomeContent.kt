package io.github.kingg22.vibrion.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import io.github.kingg22.vibrion.Res
import io.github.kingg22.vibrion.artists
import io.github.kingg22.vibrion.domain.model.ArtistInfo
import io.github.kingg22.vibrion.domain.model.DownloadableItem
import io.github.kingg22.vibrion.domain.model.GenreInfo
import io.github.kingg22.vibrion.domain.model.ModelType
import io.github.kingg22.vibrion.genres
import io.github.kingg22.vibrion.placeholder
import io.github.kingg22.vibrion.playlists
import io.github.kingg22.vibrion.top_music_country
import io.github.kingg22.vibrion.ui.components.CarouselItem
import io.github.kingg22.vibrion.ui.components.ImageLabel
import io.github.kingg22.vibrion.ui.components.ImageLabelPlaceholder
import io.github.kingg22.vibrion.ui.components.PlaylistCard
import io.github.kingg22.vibrion.ui.components.PlaylistCardPlaceholder
import io.github.kingg22.vibrion.ui.components.PlaylistItem
import io.github.kingg22.vibrion.ui.components.SectionHeader
import io.github.kingg22.vibrion.ui.components.SongCard
import io.github.kingg22.vibrion.ui.components.SongCardPlaceholder
import io.github.kingg22.vibrion.ui.items
import io.github.kingg22.vibrion.ui.itemsIndexed
import io.github.kingg22.vibrion.ui.theme.VibrionAppTheme
import kotlinx.coroutines.flow.MutableStateFlow
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeContent(
    genresList: LazyPagingItems<GenreInfo>,
    carouselItems: LazyPagingItems<out DownloadableItem>,
    artistList: LazyPagingItems<ArtistInfo>,
    playlistsList: LazyPagingItems<out DownloadableItem>,
    bottomBar: @Composable (() -> Unit),
    bottomBarPadding: (padding: Dp) -> Unit,
    onDetailClick: (type: ModelType, id: String) -> Unit,
    onPlayTrackClick: (item: DownloadableItem) -> Unit,
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit,
) {
    /*
    val options = rememberSaveable { listOf(R.string.deezer_name) }
    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }
     */

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = topBar,
        bottomBar = bottomBar,
    ) { paddingValues ->
        bottomBarPadding(paddingValues.calculateBottomPadding())

        LazyColumn(
            modifier = Modifier.padding(paddingValues).fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.Top),
        ) {
            /* Button Bar of supported platforms
            item {
                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .padding(start = 4.dp, top = 8.dp, end = 4.dp),
                ) {
                    options.forEachIndexed { index, labelKey ->
                        SegmentedButton(
                            shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                            modifier = Modifier.weight(1f),
                            onClick = { selectedIndex = index },
                            selected = index == selectedIndex,
                            label = { Text(stringResource(labelKey)) },
                            icon = {},
                        )
                    }
                }
            }
             */

            // Genres icons
            item {
                SectionHeader(stringResource(Res.string.genres))
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(124.dp)
                        .padding(start = 16.dp, end = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
                    verticalAlignment = Alignment.Top,
                ) {
                    if (genresList.loadState.refresh == LoadState.Loading) {
                        items(5) { ImageLabelPlaceholder(imageModifier = Modifier.clip(CircleShape)) }
                    }

                    items(genresList, key = { it.id }) { genre ->
                        ImageLabel(
                            image = genre.picture ?: painterResource(Res.drawable.placeholder),
                            label = genre.name,
                            modifier = Modifier.clickable(onClick = {
                                onDetailClick(ModelType.GENRE, genre.id.toString())
                            }),
                            imageModifier = Modifier.clip(CircleShape),
                        )
                    }

                    if (genresList.loadState.append == LoadState.Loading) {
                        item {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentWidth(Alignment.CenterHorizontally),
                            )
                        }
                    }
                }
            }

            // Top of country
            // TODO add country name based on location or something
            item {
                SectionHeader(stringResource(Res.string.top_music_country, "Panamá"))
                LazyRow(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    if (carouselItems.loadState.refresh == LoadState.Loading) {
                        items(count = 5) { _ ->
                            SongCardPlaceholder(modifier = Modifier.width(280.dp))
                        }
                    }

                    itemsIndexed(carouselItems, key = { _, entry -> entry.id }) { index, item ->
                        SongCard(
                            carouselItem = CarouselItem(
                                id = item.id,
                                position = index + 1,
                                painter = item.thumbnailUrl
                                    ?: painterResource(Res.drawable.placeholder),
                                contentDescription = item.title,
                                artistName = item.artists.joinToString { it.name },
                                titleSong = item.title,
                            ),
                            onClick = { onDetailClick(ModelType.SINGLE, item.id) },
                            onPlayClick = { onPlayTrackClick(item) },
                            modifier = Modifier.width(280.dp),
                        )
                    }

                    if (carouselItems.loadState.append == LoadState.Loading) {
                        items(count = 5) { _ ->
                            SongCardPlaceholder(modifier = Modifier.width(280.dp))
                        }
                    }
                }
            }

            // Favorites Artist
            item {
                SectionHeader(stringResource(Res.string.artists))
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(136.dp)
                        .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
                    verticalAlignment = Alignment.Top,
                ) {
                    if (artistList.loadState.refresh == LoadState.Loading) {
                        items(5) {
                            ImageLabelPlaceholder(imageModifier = Modifier.clip(RoundedCornerShape(percent = 15)))
                        }
                    }

                    items(artistList, key = { it.id }) { artist ->
                        ImageLabel(
                            image = artist.pictureUrl ?: painterResource(Res.drawable.placeholder),
                            label = artist.name,
                            modifier = Modifier.clickable(onClick = { onDetailClick(ModelType.ARTIST, artist.id) }),
                            imageModifier = Modifier.clip(RoundedCornerShape(percent = 15)),
                        )
                    }

                    if (artistList.loadState.append == LoadState.Loading) {
                        items(5) {
                            ImageLabelPlaceholder(imageModifier = Modifier.clip(RoundedCornerShape(percent = 15)))
                        }
                    }
                }
            }

            // Playlist
            item { SectionHeader(stringResource(Res.string.playlists)) }
            if (playlistsList.loadState.refresh == LoadState.Loading) {
                items(10) { _ ->
                    PlaylistCardPlaceholder(modifier = Modifier.padding(start = 10.dp, end = 10.dp))
                }
            }

            items(playlistsList, key = { it.id }) { item ->
                PlaylistCard(
                    playlist = PlaylistItem(
                        image = item.thumbnailUrl ?: painterResource(Res.drawable.placeholder),
                        headline = item.title,
                        description = item.description ?: item.artists.joinToString { it.name },
                        contentDescription = item.title,
                    ),
                    onClick = { onDetailClick(ModelType.PLAYLIST, item.id) },
                    onPlayClick = { onPlayTrackClick(item) },
                    modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                )
            }

            if (playlistsList.loadState.append == LoadState.Loading) {
                items(10) { _ ->
                    PlaylistCardPlaceholder(modifier = Modifier.padding(start = 10.dp, end = 10.dp))
                }
            }

            // TODO add albums and podcast sections
        }
    }
}

@Preview
@Composable
private fun HomePreview() {
    val artistPages = PagingData.empty<ArtistInfo>(
        LoadStates(
            LoadState.Loading,
            LoadState.NotLoading(true),
            LoadState.NotLoading(true),
        ),
    )
    val imageFakeFlow = MutableStateFlow(artistPages)
    val artistItems = imageFakeFlow.collectAsLazyPagingItems()

    val genrePages = PagingData.empty<GenreInfo>(
        LoadStates(
            LoadState.Loading,
            LoadState.NotLoading(true),
            LoadState.NotLoading(true),
        ),
    )
    val genreFakeFlow = MutableStateFlow(genrePages)
    val genreItems = genreFakeFlow.collectAsLazyPagingItems()

    val carouselPages = PagingData.empty<DownloadableItem>(
        LoadStates(
            LoadState.Loading,
            LoadState.NotLoading(true),
            LoadState.NotLoading(true),
        ),
    )
    val carouselFakeFlow = MutableStateFlow(carouselPages)
    val carouselItems = carouselFakeFlow.collectAsLazyPagingItems()

    val playlistPages = PagingData.empty<DownloadableItem>(
        LoadStates(
            LoadState.Loading,
            LoadState.NotLoading(true),
            LoadState.NotLoading(true),
        ),
    )
    val playlistFakeFlow = MutableStateFlow(playlistPages)
    val playlistsList = playlistFakeFlow.collectAsLazyPagingItems()

    VibrionAppTheme {
        HomeContent(
            genresList = genreItems,
            carouselItems = carouselItems,
            artistList = artistItems,
            playlistsList = playlistsList,
            bottomBar = {},
            bottomBarPadding = {},
            topBar = {},
            onDetailClick = { _, _ -> },
            onPlayTrackClick = {},
        )
    }
}
