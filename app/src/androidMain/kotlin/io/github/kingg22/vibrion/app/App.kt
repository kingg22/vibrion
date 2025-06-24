package io.github.kingg22.vibrion.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import io.github.kingg22.vibrion.app.ui.theme.VibrionAppTheme

@Composable
fun App(modifier: Modifier = Modifier) {
    VibrionAppTheme {
        Column(
            modifier = modifier
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text("Hello, world!")
        }
    }
}

@PreviewScreenSizes
@Composable
private fun AppPreview() {
    App()
}
