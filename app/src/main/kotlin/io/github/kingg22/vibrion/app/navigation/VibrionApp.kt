package io.github.kingg22.vibrion.app.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.runtime.serialization.NavBackStackSerializer
import androidx.navigation3.ui.NavDisplay
import io.github.kingg22.vibrion.app.ui.screens.search.SearchResultScreen
import io.github.kingg22.vibrion.app.ui.screens.search.SearchScreen
import io.github.kingg22.vibrion.app.ui.screens.settings.SettingsScreen

@Composable
fun VibrionApp(modifier: Modifier = Modifier) {
    // Don't relays on rememberNavStack because that use reflection under the hood
    val backStack = rememberSerializable(
        serializer = NavBackStackSerializer(elementSerializer = NavigationKeys.serializer()),
    ) {
        NavBackStack(NavigationKeys.Search)
    }

    NavDisplay(
        backStack = backStack,
        modifier = modifier.fillMaxSize(),
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            entry<NavigationKeys.Search> { _ ->
                SearchScreen(
                    onSearch = { backStack.add(NavigationKeys.SearchResult(it)) },
                    onSettingsClick = { backStack.add(NavigationKeys.Settings) },
                )
            }

            entry<NavigationKeys.SearchResult> { (query) ->
                SearchResultScreen(
                    query = query,
                    onBackClick = { backStack.removeLastOrNull() },
                    onSettingsClick = { backStack.add(NavigationKeys.Settings) },
                )
            }

            entry<NavigationKeys.Settings> { _ ->
                SettingsScreen(onBackClick = { backStack.removeLastOrNull() })
            }
        },
    )
}
