package io.github.kingg22.vibrion.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.kingg22.vibrion.app.navigation.VibrionApp
import io.github.kingg22.vibrion.app.ui.theme.VibrionAppTheme

@Composable
fun App(modifier: Modifier = Modifier) {
    VibrionAppTheme {
        VibrionApp(modifier)
    }
}
