package io.github.kingg22.vibrion.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import io.github.kingg22.vibrion.Res
import io.github.kingg22.vibrion.home
import io.github.kingg22.vibrion.music_note
import io.github.kingg22.vibrion.settings
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun BottomNavigationBar(
    isSelected: (VibrionRoutes) -> Boolean,
    onNavigate: (VibrionRoutes) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(modifier) {
        NavigationItem.entries.forEach { item ->
            NavigationBarItem(
                label = { Text(stringResource(item.labelRes)) },
                icon = { Icon(item.icon, stringResource(item.iconDescriptionRes)) },
                onClick = { onNavigate(item.destinationRoute) },
                selected = isSelected(item.destinationRoute),
            )
        }
    }
}

@Preview
@Composable
private fun BottomNavigationBarPreview() {
    BottomNavigationBar(
        isSelected = { it == VibrionRoutes.Home },
        onNavigate = {},
    )
}

@Immutable
private enum class NavigationItem(
    val labelRes: StringResource,
    val icon: ImageVector,
    val iconDescriptionRes: StringResource,
    val destinationRoute: VibrionRoutes,
) {
    HOME(Res.string.home, Icons.Filled.MusicNote, Res.string.music_note, VibrionRoutes.Home),
    SETTINGS(Res.string.settings, Icons.Filled.Settings, Res.string.settings, VibrionRoutes.Settings),
}
