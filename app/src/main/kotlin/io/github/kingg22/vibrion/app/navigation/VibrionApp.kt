package io.github.kingg22.vibrion.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import io.github.kingg22.vibrion.app.ui.screens.search.SearchResultScreen
import io.github.kingg22.vibrion.app.ui.screens.search.SearchScreen
import io.github.kingg22.vibrion.app.ui.screens.settings.SettingsScreen

@Composable
fun VibrionApp(modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(NavigationKeys.Search)
    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        entryProvider = entryProvider {
            entry<NavigationKeys.Search> {
                SearchScreen(onSearch = {
                    backStack.add(NavigationKeys.SearchResult(it))
                }, onSettingsClick = {
                    backStack.add(NavigationKeys.Settings)
                })
            }

            entry<NavigationKeys.SearchResult> {
                SearchResultScreen(query = it.query, onBackClick = {
                    backStack.removeLastOrNull()
                }, onSettingsClick = {
                    backStack.add(NavigationKeys.Settings)
                })
            }

            entry<NavigationKeys.Settings> {
                SettingsScreen(onBackClick = {
                    backStack.removeLastOrNull()
                })
            }
        },
    )
}
