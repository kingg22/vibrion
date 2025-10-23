package io.github.kingg22.vibrion.app.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed class NavigationKeys : NavKey {
    @Serializable
    data object Search : NavigationKeys()

    @Serializable
    data class SearchResult(val query: String) : NavigationKeys()

    @Serializable
    data object Settings : NavigationKeys()
}
