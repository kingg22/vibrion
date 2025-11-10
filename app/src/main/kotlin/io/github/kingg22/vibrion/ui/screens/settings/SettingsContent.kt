package io.github.kingg22.vibrion.ui.screens.settings

import androidx.compose.foundation.BasicTooltipBox
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberBasicTooltipState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Handshake
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.kingg22.vibrion.BuildConfig
import io.github.kingg22.vibrion.R
import io.github.kingg22.vibrion.domain.model.AppSettings
import io.github.kingg22.vibrion.domain.model.Quality
import io.github.kingg22.vibrion.domain.model.ThemeMode
import io.github.kingg22.vibrion.ui.getDisplayName
import io.github.kingg22.vibrion.ui.theme.VibrionAppTheme

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun SettingsContent(
    state: AppSettings,
    callbacks: SettingsCallbacks,
    modifier: Modifier = Modifier,
    bottomBar: @Composable (() -> Unit),
) {
    val uriHandler = LocalUriHandler.current
    val themes = remember { ThemeMode.entries.map { Pair(it, it.getDisplayName()) } }
    val qualityItems = remember { Quality.entries.map { Pair(it, it.getDisplayName()) } }
    var expanded by rememberSaveable { mutableStateOf(false) }
    var downloadPath by rememberSaveable { mutableStateOf(state.downloadPath) }

    Scaffold(
        modifier = modifier,
        bottomBar = bottomBar,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings)) },
                navigationIcon = {
                    IconButton(onClick = callbacks.onBackClick) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, stringResource(R.string.back))
                    }
                },
            )
        },
    ) { padding ->
        LazyColumn(Modifier.padding(padding).fillMaxSize()) {
            // Theme Settings
            item {
                SettingsSection(stringResource(R.string.theme)) {
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = {
                            // Remove in kotlin 2.3.0, false positive
                            @Suppress("ASSIGNED_VALUE_IS_NEVER_READ")
                            expanded = it
                        },
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    ) {
                        OutlinedTextField(
                            readOnly = true,
                            value = stringResource(themes.first { it.first == state.theme }.second),
                            onValueChange = { },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(type = ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                        ) {
                            themes.forEach { (theme, label) ->
                                DropdownMenuItem(
                                    text = { Text(stringResource(label)) },
                                    onClick = {
                                        expanded = false
                                        callbacks.updateThemeMode(theme)
                                    },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                )
                            }
                        }
                    }
                }
            }
            // Account Settings
            item {
                SettingsAccountSection(state.token, callbacks.updateToken)
            }

            // Storage Settings
            item {
                SettingsSection(title = stringResource(R.string.storage)) {
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        OutlinedTextField(
                            value = downloadPath,
                            onValueChange = {
                                downloadPath = it
                                callbacks.updateDownloadPath(it)
                            },
                            label = { Text(stringResource(R.string.download_folder)) },
                            modifier = Modifier.fillMaxWidth(),
                            trailingIcon = {
                                IconButton(onClick = { /* TODO Open folder picker */ }) {
                                    Icon(
                                        Icons.Default.Folder,
                                        stringResource(R.string.select) + " " + stringResource(R.string.folder),
                                    )
                                }
                            },
                        )
                    }
                }
            }
            // Services Settings
            item {
                SettingsSection(stringResource(R.string.services)) {
                    Column {
                        // Concurrent Downloads
                        ListItem(
                            headlineContent = { Text(stringResource(R.string.concurrent_downloads)) },
                            supportingContent = {
                                Column {
                                    Slider(
                                        value = state.maxConcurrentDownloads.toFloat(),
                                        onValueChange = callbacks.updateMaxConcurrentDownloads,
                                        valueRange = 1f..5f,
                                        steps = 3,
                                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                                    )
                                    Text(
                                        "${state.maxConcurrentDownloads} ${stringResource(R.string.download)}",
                                        modifier = Modifier.padding(start = 16.dp),
                                    )
                                }
                            },
                        )

                        // Quality Preference
                        ListItem(
                            headlineContent = {
                                Row {
                                    Text(
                                        stringResource(R.string.quality) + " " +
                                            stringResource(R.string.preferred),
                                    )
                                    Spacer(Modifier.width(4.dp))
                                    BasicTooltipBox(
                                        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
                                            TooltipAnchorPosition.Above,
                                        ),
                                        tooltip = {
                                            Surface(
                                                shape = MaterialTheme.shapes.medium,
                                                tonalElevation = 4.dp,
                                                modifier = Modifier.padding(8.dp),
                                            ) {
                                                Text(
                                                    stringResource(R.string.quality_description),
                                                    style = MaterialTheme.typography.labelLarge,
                                                    modifier = Modifier.padding(8.dp),
                                                )
                                            }
                                        },
                                        state = rememberBasicTooltipState(isPersistent = false),
                                    ) {
                                        Icon(Icons.Filled.Info, null, modifier = Modifier.size(20.dp))
                                    }
                                }
                            },
                            supportingContent = {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    qualityItems.forEach { (quality, label) ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                        ) {
                                            RadioButton(
                                                selected = quality.name == state.preferredQuality,
                                                onClick = { callbacks.updateQuality(quality) },
                                            )
                                            Text(stringResource(label))
                                        }
                                    }
                                }
                            },
                        )

                        // Stream Settings
                        ListItem(
                            headlineContent = { Text(stringResource(R.string.streaming)) },
                            supportingContent = { Text(stringResource(R.string.streaming_indication)) },
                            trailingContent = {
                                Switch(
                                    checked = state.useCompleteStream,
                                    onCheckedChange = callbacks.updateCompleteStream,
                                )
                            },
                        )
                    }
                }
            }

            // Third Party licenses
            item {
                CategoryItem(
                    stringResource(R.string.licenses),
                    Icons.Outlined.Handshake,
                    onClick = callbacks.onLibrariesClick,
                    Modifier.padding(16.dp),
                )
            }

            // Version Info
            item {
                AppVersion(
                    """Version ${BuildConfig.VERSION_NAME} ${BuildConfig.BUILD_TYPE}""",
                    "${stringResource(R.string.author)} Rey Acosta (Kingg22)",
                    onClick = { uriHandler.openUri("https://github.com/Kingg22/") },
                    Modifier.padding(16.dp),
                )
            }
        }
    }
}

@Composable
@Preview
private fun SettingsPreview() {
    VibrionAppTheme {
        SettingsContent(
            state = AppSettings.default,
            callbacks = SettingsCallbacks(
                onBackClick = {},
                onLibrariesClick = {},
                updateThemeMode = {},
                updateQuality = {},
                updateCompleteStream = {},
                updateMaxConcurrentDownloads = {},
                updateToken = {},
                updateDownloadPath = {},
            ),
        ) {}
    }
}
