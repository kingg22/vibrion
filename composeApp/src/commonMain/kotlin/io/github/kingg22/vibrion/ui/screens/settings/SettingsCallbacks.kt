package io.github.kingg22.vibrion.ui.screens.settings

import io.github.kingg22.vibrion.domain.model.Quality
import io.github.kingg22.vibrion.domain.model.ThemeMode

class SettingsCallbacks(
    val onBackClick: () -> Unit,
    val onLibrariesClick: () -> Unit,
    val updateThemeMode: (ThemeMode) -> Unit,
    val updateQuality: (Quality) -> Unit,
    val updateCompleteStream: (Boolean) -> Unit,
    val updateMaxConcurrentDownloads: (Float) -> Unit,
    val updateToken: (String) -> Unit,
    val updateDownloadPath: (String) -> Unit,
)
