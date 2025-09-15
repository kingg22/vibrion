package io.github.kingg22.vibrion.app.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.kingg22.vibrion.app.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(private val settingsRepository: SettingsRepository) : ViewModel() {
    val token = settingsRepository.loadToken().stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun updateToken(newToken: String) {
        viewModelScope.launch {
            settingsRepository.updateToken(newToken)
        }
    }
}
