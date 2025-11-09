package io.github.kingg22.vibrion.ui.screens.download

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.kingg22.vibrion.R
import io.github.kingg22.vibrion.domain.model.DownloadItem
import io.github.kingg22.vibrion.domain.model.DownloadStatus

@Composable
fun DownloadStatisticsCard(
    totalItems: Int,
    completedItems: Int,
    failedItems: Int,
    cancelledItems: Int,
    onCancelClick: () -> Unit,
    onResumeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, bottom = 5.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            StatItem(
                label = stringResource(R.string.total),
                value = totalItems.toString(),
                color = MaterialTheme.colorScheme.primary,
            )
            StatItem(
                label = pluralStringResource(R.plurals.completed, 10),
                value = completedItems.toString(),
                color = MaterialTheme.colorScheme.tertiary,
            )
            StatItem(
                label = pluralStringResource(R.plurals.failed, 10),
                value = failedItems.toString(),
                color = MaterialTheme.colorScheme.error,
            )
            StatItem(
                label = pluralStringResource(R.plurals.cancelled, 10),
                value = cancelledItems.toString(),
                color = MaterialTheme.colorScheme.outline,
            )
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            OutlinedButton(onClick = onCancelClick, modifier = Modifier.weight(1f)) {
                Text(stringResource(R.string.cancel) + " " + stringResource(R.string.all))
            }
            OutlinedButton(onClick = onResumeClick, modifier = Modifier.weight(1f)) {
                Text(stringResource(R.string.retry) + " " + stringResource(R.string.all))
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, style = MaterialTheme.typography.headlineSmall, color = color)
        Text(text = label, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
fun DownloadItemCard(
    downloadItem: DownloadItem,
    onCancelClick: () -> Unit,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)) {
        Column(Modifier.fillMaxWidth().padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(Modifier.weight(1f)) {
                    Text(
                        text = downloadItem.fileName,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

                // Status icon or cancel button
                when (downloadItem.status) {
                    DownloadStatus.IN_PROGRESS -> {
                        IconButton(onClick = onCancelClick) {
                            Icon(
                                imageVector = Icons.Default.Cancel,
                                contentDescription =
                                stringResource(R.string.cancel) + " " + stringResource(R.string.download),
                                tint = MaterialTheme.colorScheme.error,
                            )
                        }
                    }

                    DownloadStatus.COMPLETED -> {
                        // maybe change to open folder or play
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription =
                            stringResource(R.string.download) + " " + pluralStringResource(
                                R.plurals.completed,
                                1,
                            ),
                            tint = MaterialTheme.colorScheme.tertiary,
                        )
                    }

                    DownloadStatus.CANCELLED, DownloadStatus.FAILED, DownloadStatus.PAUSED -> {
                        IconButton(onClick = onRetryClick) {
                            Icon(
                                imageVector = Icons.Default.Replay,
                                contentDescription =
                                "${stringResource(R.string.download)} ${
                                    pluralStringResource(R.plurals.failed, 1)
                                } ${stringResource(R.string.retry)}",
                                tint = MaterialTheme.colorScheme.error,
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            ProgressIndicator(downloadItem)
        }
    }
}

@Composable
fun ProgressIndicator(downloadItem: DownloadItem, modifier: Modifier = Modifier) {
    val progress by animateFloatAsState(downloadItem.progress, ProgressIndicatorDefaults.ProgressAnimationSpec)
    // Progress indicator
    when (downloadItem.status) {
        DownloadStatus.IN_PROGRESS -> {
            Row(modifier) {
                LinearProgressIndicator(
                    progress = { progress / 100 },
                    modifier = Modifier.align(Alignment.CenterVertically).weight(99f),
                )
                Spacer(Modifier.width(5.dp))
                Text(
                    text = "${progress.toInt()}%",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.align(Alignment.Top),
                )
            }
        }

        else -> {
            // Status text
            val statusText = when (downloadItem.status) {
                DownloadStatus.COMPLETED -> pluralStringResource(R.plurals.completed, 1)
                DownloadStatus.FAILED -> pluralStringResource(R.plurals.failed, 1)
                DownloadStatus.CANCELLED -> pluralStringResource(R.plurals.cancelled, 1)
                else -> ""
            }

            val statusColor = when (downloadItem.status) {
                DownloadStatus.COMPLETED -> MaterialTheme.colorScheme.tertiary
                DownloadStatus.FAILED -> MaterialTheme.colorScheme.error
                DownloadStatus.CANCELLED -> MaterialTheme.colorScheme.outline
                else -> MaterialTheme.colorScheme.onSurface
            }

            if (statusText.isNotEmpty()) {
                Text(
                    text = statusText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = statusColor,
                    modifier = modifier,
                )
            }
        }
    }
}

@Composable
@Preview
private fun DownloadCardPreview() {
    DownloadItemCard(
        DownloadItem("1", "prueba.mp3", "200mbps", 0f, DownloadStatus.IN_PROGRESS),
        {},
        {},
    )
}
