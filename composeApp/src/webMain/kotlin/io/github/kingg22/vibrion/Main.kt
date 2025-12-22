package io.github.kingg22.vibrion

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import io.github.kingg22.vibrion.di.vibrionAppModule
import io.github.kingg22.vibrion.navigation.Vibrion
import io.github.kingg22.vibrion.ui.theme.VibrionAppTheme
import org.koin.core.context.startKoin

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    // TODO move to common module startKoin, Coil and Sentry
    startKoin { modules(vibrionAppModule) }
    ComposeViewport {
        VibrionAppTheme {
            Vibrion()
        }
    }
}
