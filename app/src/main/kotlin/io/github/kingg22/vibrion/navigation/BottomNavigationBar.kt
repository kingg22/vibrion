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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import io.github.kingg22.vibrion.R

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
    val labelRes: Int,
    val icon: ImageVector,
    val iconDescriptionRes: Int,
    val destinationRoute: VibrionRoutes,
) {
    HOME(R.string.home, Icons.Filled.MusicNote, R.string.music_note, VibrionRoutes.Home),
    SETTINGS(R.string.settings, Icons.Filled.Settings, R.string.settings, VibrionRoutes.Settings),
}
