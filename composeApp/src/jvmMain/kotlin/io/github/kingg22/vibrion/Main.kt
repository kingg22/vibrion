package io.github.kingg22.vibrion

import androidx.compose.ui.Alignment
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication
import io.github.kingg22.vibrion.di.vibrionAppModule
import io.github.kingg22.vibrion.navigation.Vibrion
import org.jetbrains.compose.resources.stringResource
import org.koin.core.context.startKoin

fun main() {
    System.setProperty("compose.interop.blending", "true")
    // TODO move to common module startKoin, Coil and Sentry
    startKoin { modules(vibrionAppModule) }
    singleWindowApplication(
        state = WindowState(position = WindowPosition.Aligned(Alignment.Center)),
    ) {
        window.title = stringResource(R.string.app_name)
        // window.setWindowsAdaptiveTitleBar(ThemeManager.isDarkTheme())
        Vibrion()
    }
}
