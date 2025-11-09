package io.github.kingg22.vibrion.ui.screens.settings

import androidx.annotation.RawRes
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
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import co.touchlab.kermit.Logger
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import io.github.kingg22.vibrion.R
import io.github.kingg22.vibrion.ui.theme.VibrionAppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun LibrariesScreen(onBackClick: () -> Unit, modifier: Modifier = Modifier) {
    val libraries by produceLibraries(R.raw.aboutlibraries)

    Scaffold(modifier, {
        TopAppBar(
            title = { Text(stringResource(R.string.licenses)) },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Default.ArrowBack, stringResource(R.string.back))
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
private fun produceLibraries(@RawRes resId: Int): State<Libs?> {
    val resources = LocalResources.current
    return produceState(initialValue = null) {
        value = withContext(Dispatchers.IO) {
            val json = try {
                resources.openRawResource(resId).bufferedReader().use { it.readText() }
            } catch (e: Exception) {
                librariesLogger.e(e) { "Failed to load libraries with resId: $resId" }
                null
            }
            Libs.Builder().apply {
                if (json != null) withJson(json)
            }.build()
        }
    }
}

@Composable
@Preview
private fun LibrariesViewPreview() {
    VibrionAppTheme { LibrariesScreen(onBackClick = {}) }
}
