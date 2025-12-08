package io.github.kingg22.vibrion.navigation

import androidx.navigation3.runtime.NavKey
import io.github.kingg22.vibrion.domain.model.ModelType
import kotlinx.serialization.Serializable

/** All routes of the app. Can only be called in [Vibrion] */
@Serializable
sealed class VibrionRoutes : NavKey {
    /** Refers to [io.github.kingg22.vibrion.ui.screens.home.HomeScreen] */
    @Serializable
    data object Home : VibrionRoutes()

    /** Refers to [io.github.kingg22.vibrion.ui.screens.settings.SettingsScreen] */
    @Serializable
    data object Settings : VibrionRoutes()

    /** Refers to [io.github.kingg22.vibrion.ui.screens.settings.LibrariesScreen] */
    @Serializable
    data object Libraries : VibrionRoutes()

    /** Refers to [io.github.kingg22.vibrion.ui.screens.search.SearchScreen] */
    @Serializable
    data class Search(val query: String) : VibrionRoutes()

    /** Refers to [io.github.kingg22.vibrion.ui.screens.search.detail.SearchDetailScreen] */
    @Serializable
    data class SearchDetail(val query: String, val title: String) : VibrionRoutes() {
        constructor(query: String, title: ModelType) : this(query, title.name.uppercase())
        val modelType = ModelType.valueOf(title.uppercase())
    }

    @Serializable
    data class Detail(val type: String, val id: String) : VibrionRoutes() {
        constructor(type: ModelType, id: String) : this(type.name.uppercase(), id)
        val modelType = ModelType.valueOf(type.uppercase())
    }
}
