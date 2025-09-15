package io.github.kingg22.vibrion.app.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface NavigationKeys {
    @Serializable
    object Search : NavigationKeys, NavKey

    @Serializable
    class SearchResult(val query: String) :
        NavigationKeys,
        NavKey

    @Serializable
    object Settings : NavigationKeys, NavKey
}
