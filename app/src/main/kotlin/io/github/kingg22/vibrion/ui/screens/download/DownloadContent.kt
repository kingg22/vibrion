package io.github.kingg22.vibrion.ui.screens.download

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.kingg22.vibrion.R
import io.github.kingg22.vibrion.domain.model.DownloadItem
import io.github.kingg22.vibrion.domain.model.DownloadState
import io.github.kingg22.vibrion.ui.components.ExpandableItem
import io.github.kingg22.vibrion.ui.theme.VibrionAppTheme

@Composable
fun DownloadContent(
    state: DownloadState,
    downloads: List<DownloadItem>,
    onCancelAllClick: () -> Unit,
    onResumeAllClick: () -> Unit,
    onResumeClick: (String) -> Unit,
    onCancelClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    bottomBar: @Composable (() -> Unit),
) {
    Scaffold(
        modifier = modifier,
        bottomBar = bottomBar,
        topBar = {
            TopAppBar(
                title = {
                    Row {
                        Icon(
                            Icons.Rounded.Download,
                            stringResource(R.string.download),
                            Modifier.align(Alignment.Bottom),
                        )
                        Text(
                            stringResource(R.string.downloads),
                            modifier = Modifier.align(Alignment.CenterVertically),
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            )
        },
    ) { paddingValues ->
        Column(Modifier.padding(paddingValues).fillMaxSize()) {
            // Statistics card
            ExpandableItem(stringResource(R.string.resume), style = MaterialTheme.typography.titleMedium) {
                DownloadStatisticsCard(
                    totalItems = state.totalCount,
                    completedItems = state.successCount,
                    failedItems = state.failedCount,
                    cancelledItems = state.cancelledCount,
                    onCancelClick = onCancelAllClick,
                    onResumeClick = onResumeAllClick,
                )
            }

            // List of downloads
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(downloads.reversed(), key = { it.id }) { item ->
                    DownloadItemCard(
                        downloadItem = item,
                        onCancelClick = { onCancelClick(item.id) },
                        onRetryClick = { onResumeClick(item.id) },
                    )
                }
            }
        }
    }
}

@Composable
@Preview
private fun DownloadPreview() {
    VibrionAppTheme {
        DownloadContent(
            state = DownloadState(),
            downloads = listOf(DownloadItem.EMPTY, DownloadItem.PREVIEW),
            onCancelAllClick = {},
            onResumeAllClick = {},
            onResumeClick = {},
            onCancelClick = {},
            bottomBar = {},
        )
    }
}
