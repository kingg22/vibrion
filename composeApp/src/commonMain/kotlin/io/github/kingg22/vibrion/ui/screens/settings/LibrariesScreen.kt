package io.github.kingg22.vibrion.ui.screens.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import co.touchlab.kermit.Logger
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import io.github.kingg22.vibrion.Res
import io.github.kingg22.vibrion.back
import io.github.kingg22.vibrion.licenses
import io.github.kingg22.vibrion.ui.theme.VibrionAppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.stringResource

@Composable
fun LibrariesScreen(onBackClick: () -> Unit, modifier: Modifier = Modifier) {
    val libraries by produceLibraries()

    Scaffold(modifier, {
        TopAppBar(
            title = { Text(stringResource(Res.string.licenses)) },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Default.ArrowBack, stringResource(Res.string.back))
                }
            },
        )
    }) {
        Box(Modifier.padding(it).fillMaxSize())
        LibrariesContainer(
            libraries,
            Modifier.padding(it).fillMaxSize(),
            showLicenseBadges = false,
        )
    }
}

private val librariesLogger = Logger.withTag("Libraries Screen")

@Composable
private fun produceLibraries(): State<Libs?> = produceState(initialValue = null) {
    // TODO Use IO dispatch
    value = withContext(Dispatchers.Default) {
        val json = try {
            Res.readBytes("files/aboutlibraries.json").decodeToString()
        } catch (e: Exception) {
            librariesLogger.e(e) { "Failed to load libraries with resId" }
            null
        }
        Libs.Builder().apply {
            if (json != null) withJson(json)
        }.build()
    }
}

@Composable
@Preview
private fun LibrariesViewPreview() {
    VibrionAppTheme { LibrariesScreen(onBackClick = {}) }
}
