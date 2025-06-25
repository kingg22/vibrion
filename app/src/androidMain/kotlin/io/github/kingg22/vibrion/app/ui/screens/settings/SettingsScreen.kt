package io.github.kingg22.vibrion.app.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import io.github.kingg22.vibrion.app.ui.theme.VibrionAppTheme

@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier.safeContentPadding()) {
        Text("TODO settings")
    }
}

@PreviewScreenSizes
@Composable
private fun SettingsPreview() {
    VibrionAppTheme {
        SettingsScreen()
    }
}
