package io.github.kingg22.vibrion.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.kingg22.vibrion.domain.model.AppSettings
import io.github.kingg22.vibrion.domain.model.ThemeMode
import io.github.kingg22.vibrion.domain.usecase.settings.GetSettingsUseCase
import io.github.kingg22.vibrion.domain.usecase.settings.UpdateSettingsUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel that manages application settings.
 * Provides access to user preferences and methods to update them.
 *
 * @property settings Flow that emits all application settings
 * @property themeMode Specific flow that only emits when the theme changes
 * @param getSettingsUseCase Use case to get the settings
 * @param updateSettingsUseCase Use case to update the settings
 * @param context Coroutine context for update operations
 */
class SettingsViewModel(
    getSettingsUseCase: GetSettingsUseCase,
    private val updateSettingsUseCase: UpdateSettingsUseCase,
) : ViewModel() {

    val settings = getSettingsUseCase()
        .distinctUntilChanged()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = AppSettings.default,
        )

    val themeMode = settings
        .map { it.theme }
        .distinctUntilChanged()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = ThemeMode.SYSTEM,
        )

    fun updateTheme(theme: ThemeMode) {
        viewModelScope.launch {
            updateSettingsUseCase.updateTheme(theme)
        }
    }

    fun updateToken(token: String) {
        viewModelScope.launch {
            updateSettingsUseCase.updateToken(token)
        }
    }

    fun updateDownloadPath(path: String) {
        viewModelScope.launch {
            updateSettingsUseCase.updateDownloadPath(path)
        }
    }

    fun updateMaxConcurrentDownloads(count: Int) {
        viewModelScope.launch {
            updateSettingsUseCase.updateMaxConcurrentDownloads(count)
        }
    }

    fun updatePreferredQuality(quality: String) {
        viewModelScope.launch {
            updateSettingsUseCase.updatePreferredQuality(quality)
        }
    }

    fun updateUseCompleteStream(use: Boolean) {
        viewModelScope.launch {
            updateSettingsUseCase.updateUseCompleteStream(use)
        }
    }
}
