package io.github.kingg22.vibrion.ui.screens.search.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import io.github.kingg22.vibrion.R
import io.github.kingg22.vibrion.domain.model.DownloadableItem
import io.github.kingg22.vibrion.domain.model.DownloadableSingle
import io.github.kingg22.vibrion.ui.items
import io.github.kingg22.vibrion.ui.screens.search.ListItemCard
import io.github.kingg22.vibrion.ui.screens.search.ListItemCardPlaceholder
import io.github.kingg22.vibrion.ui.theme.VibrionAppTheme
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun SearchDetailContent(
    title: String,
    listResult: LazyPagingItems<out DownloadableItem>,
    canDownload: Boolean,
    onBackClick: () -> Unit,
    onPlayClick: (DownloadableItem) -> Unit,
    onDownloadClick: (DownloadableItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier,
        topBar = {
            TopAppBar(
                title = { Text(title) },
                modifier = Modifier.fillMaxWidth(),
                navigationIcon = {
                    IconButton(onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.back))
                    }
                },
            )
        },
    ) { paddingValues ->
        LazyColumn(
            Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            if (listResult.loadState.refresh == LoadState.Loading) {
                items(count = 10) { ListItemCardPlaceholder() }
            }

            items(
                listResult,
                key = { it.id },
            ) { item ->
                val artist = item.artists.joinToString(prefix = "Artists: ") { it.name }
                val duration = item.duration?.toString()?.let { "Duration: $it" }
                val releaseDate = item.releaseDate?.let { "Release Date: $it" }
                val album = item.album?.let { "Album: $it" }

                ListItemCard(
                    title = item.title,
                    image = item.thumbnailUrl,
                    subtitles = listOfNotNull(artist, album, duration, releaseDate),
                    canDownload = canDownload,
                    onPlayClick = { onPlayClick(item) },
                    onDownloadClick = { onDownloadClick(item) },
                    onSeeMoreClick = {},
                )
            }

            if (listResult.loadState.append == LoadState.Loading) {
                items(count = 5) { ListItemCardPlaceholder() }
            }
        }
    }
}

@Preview(group = "Search Detail")
@Composable
private fun SearchDetailLoadedPreview() {
    val data = List(10) { DownloadableSingle("id$it", "Title $it", "Description $it") }
    val pagingData = PagingData.from(data)
    val fakeFlow = MutableStateFlow(pagingData)
    val listItems = fakeFlow.collectAsLazyPagingItems()

    VibrionAppTheme {
        SearchDetailContent(
            title = "Section Title",
            listResult = listItems,
            canDownload = true,
            onBackClick = {},
            onPlayClick = {},
            onDownloadClick = {},
        )
    }
}

@Preview(group = "Search Detail")
@Composable
private fun SearchDetailErrorPreview() {
    val pagingData = PagingData.empty<DownloadableSingle>(
        LoadStates(
            LoadState.Loading,
            LoadState.NotLoading(true),
            LoadState.NotLoading(true),
        ),
    )
    val fakeFlow = MutableStateFlow(pagingData)
    val listItems = fakeFlow.collectAsLazyPagingItems()

    VibrionAppTheme {
        SearchDetailContent(
            title = "Section Title",
            listResult = listItems,
            canDownload = true,
            onBackClick = {},
            onPlayClick = {},
            onDownloadClick = {},
        )
    }
}
