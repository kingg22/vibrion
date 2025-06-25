package io.github.kingg22.vibrion.app.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

interface NavigationKeys {
    @Serializable
    data object Search : NavigationKeys, NavKey

    @Serializable
    data class SearchResult(val query: String) :
        NavigationKeys,
        NavKey

    @Serializable
    data object Settings : NavigationKeys, NavKey
}
