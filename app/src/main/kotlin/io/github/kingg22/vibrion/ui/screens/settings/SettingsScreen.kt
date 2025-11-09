package io.github.kingg22.vibrion.ui.screens.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    onLibrariesClick: () -> Unit,
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel = koinViewModel(),
    bottomBar: @Composable (() -> Unit),
) {
    val settingsState by settingsViewModel.settings.collectAsStateWithLifecycle()

    SettingsContent(
        state = settingsState,
        callbacks = SettingsCallbacks(
            onBackClick = onBackClick,
            onLibrariesClick = onLibrariesClick,
            updateThemeMode = { newTheme ->
                settingsViewModel.updateTheme(newTheme)
            },
            updateQuality = { newQuality ->
                settingsViewModel.updatePreferredQuality(newQuality.name)
            },
            updateCompleteStream = {
                settingsViewModel.updateUseCompleteStream(it)
            },
            updateMaxConcurrentDownloads = { newValue ->
                settingsViewModel.updateMaxConcurrentDownloads(newValue.toInt())
            },
            updateToken = { newToken ->
                settingsViewModel.updateToken(newToken)
            },
            updateDownloadPath = { newPath ->
                settingsViewModel.updateDownloadPath(newPath)
            },
        ),
        modifier = modifier,
        bottomBar = bottomBar,
    )
}
