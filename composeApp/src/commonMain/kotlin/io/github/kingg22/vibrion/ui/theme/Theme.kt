package io.github.kingg22.vibrion.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.kingg22.vibrion.domain.model.ThemeMode
import io.github.kingg22.vibrion.ui.screens.settings.SettingsViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun VibrionAppTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = true,
    dynamicColorScheme: ColorScheme? = null,
    content: @Composable (() -> Unit),
) {
    val colorScheme = when {
        // Dynamic color is available on Android 12+
        dynamicColor && dynamicColorScheme != null -> dynamicColorScheme

        darkTheme -> highContrastDarkColorScheme

        else -> highContrastLightColorScheme
    }

    MaterialTheme(colorScheme, content = content)
}

/**
 * Determines whether to use the dark theme based on the current theme.
 * @return true if the dark theme should be used, false otherwise
 */
@Composable
fun isDarkTheme(viewModel: SettingsViewModel = koinViewModel()): Boolean {
    val theme by viewModel.themeMode.collectAsStateWithLifecycle(minActiveState = Lifecycle.State.CREATED)
    return when (theme) {
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
    }
}
