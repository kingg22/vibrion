package io.github.kingg22.vibrion.app.ui.screens.settings

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsViewModel : ViewModel() {
    val token: StateFlow<String> = MutableStateFlow("")

    fun updateToken(newToken: String) {
        (token as MutableStateFlow).value = newToken
    }
}
