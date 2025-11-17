package io.github.kingg22.vibrion.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.runtime.serialization.NavBackStackSerializer
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.NavDisplay.predictivePopTransitionSpec
import co.touchlab.kermit.Logger
import io.github.kingg22.vibrion.navigation.VibrionRoutes.Detail
import io.github.kingg22.vibrion.navigation.VibrionRoutes.Download
import io.github.kingg22.vibrion.navigation.VibrionRoutes.Home
import io.github.kingg22.vibrion.navigation.VibrionRoutes.Libraries
import io.github.kingg22.vibrion.navigation.VibrionRoutes.Search
import io.github.kingg22.vibrion.navigation.VibrionRoutes.SearchDetail
import io.github.kingg22.vibrion.navigation.VibrionRoutes.Settings
import io.github.kingg22.vibrion.ui.screens.detail.MusicDetailScreen
import io.github.kingg22.vibrion.ui.screens.download.DownloadScreen
import io.github.kingg22.vibrion.ui.screens.home.HomeScreen
import io.github.kingg22.vibrion.ui.screens.search.SearchScreen
import io.github.kingg22.vibrion.ui.screens.search.detail.SearchDetailScreen
import io.github.kingg22.vibrion.ui.screens.settings.LibrariesScreen
import io.github.kingg22.vibrion.ui.screens.settings.SettingsScreen
import kotlinx.serialization.Serializable

/** Navigation of Vibrion app. */
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Vibrion(modifier: Modifier = Modifier) {
    val backStack = rememberSerializable(
        serializer = NavBackStackSerializer(VibrionRoutes.serializer()),
    ) {
        NavBackStack(Home)
    }

    // prevent exception and trigger recomposition
    if (backStack.isEmpty()) {
        navigationLogger.w { "The backstack is empty, adding home screen. This is an error." }
        backStack += Home
    }

    SharedTransitionLayout {
        NavDisplay(
            backStack,
            modifier = modifier,
            onBack = { backStack.removeLastOrNull() },
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
            ),
        ) { key ->
            when (key) {
                is Home -> entry(key) {
                    HomeScreen(
                        onSearch = { query -> backStack.add(Search(query)) },
                        onDetailClick = { type, id -> backStack += Detail(type, id) },
                        bottomBar = {
                            BottomNavigationBar(
                                isSelected = { backStack.lastOrNull() == it },
                                onNavigate = { backStack.singletonToLast(it) },
                            )
                        },
                    )
                }

                is Download -> entry(key) {
                    DownloadScreen(bottomBar = {
                        BottomNavigationBar(
                            isSelected = { backStack.lastOrNull() == it },
                            onNavigate = { backStack.singletonToLast(it) },
                        )
                    })
                }

                is Settings -> entry(key) {
                    SettingsScreen(
                        onBackClick = { backStack.removeLastOrNull() },
                        onLibrariesClick = { backStack += Libraries },
                        bottomBar = {
                            BottomNavigationBar(
                                isSelected = { backStack.lastOrNull() == it },
                                onNavigate = { backStack.singletonToLast(it) },
                            )
                        },
                    )
                }

                // -- Without bottom bar --
                is Libraries -> entry(key) {
                    LibrariesScreen(onBackClick = { backStack.removeLastOrNull() })
                }

                is Search -> entry(key) {
                    SearchScreen(
                        query = key.query,
                        onBack = { backStack.removeLastOrNull() },
                        onListDetailClick = { query, section ->
                            backStack += SearchDetail(query = query, section)
                        },
                        onItemDetailClick = { type, id ->
                            backStack += Detail(type = type, id = id)
                        },
                    )
                }

                is SearchDetail -> entry(key) {
                    SearchDetailScreen(
                        query = key.query,
                        modelType = key.modelType,
                        onBackClick = { backStack.removeLastOrNull() },
                        onDetailClick = { type, id -> backStack += Detail(type = type, id = id) },
                    )
                }

                is Detail -> entry(key) {
                    MusicDetailScreen(
                        type = key.modelType,
                        id = key.id,
                        onBackClick = { backStack.removeLastOrNull() },
                        onDetailClick = { type, id -> backStack += Detail(type = type, id = id) },
                    )
                }
            }
        }
    }
}

private val navigationLogger = Logger.withTag("Nav3")

/**
 * Add entry to navigation, this not pass the key to the content.
 *
 * @param key the key for this NavEntry and the content.
 * @param T the type of the key for this NavEntry
 * @param transitionSpec the transition spec for this entry. See [NavDisplay.transitionSpec].
 * @param popTransitionSpec the transition spec when popping this entry from backstack.
 * See [NavDisplay.popTransitionSpec].
 * @param predictivePopTransitionSpec the transition spec when popping this entry from backstack using the predictive back gesture.
 * See [NavDisplay.predictivePopTransitionSpec].
 * @param metadata provides information to the display
 * @param content content for this entry to be displayed when this entry is active with [AnimatedContentScope] of [LocalNavAnimatedContentScope].
 */
private inline fun <T : @Serializable NavKey> entry(
    key: T,
    noinline transitionSpec: (AnimatedContentTransitionScope<*>.() -> ContentTransform?)? = null,
    noinline popTransitionSpec: (AnimatedContentTransitionScope<*>.() -> ContentTransform?)? = null,
    metadata: Map<String, Any> = emptyMap(),
    crossinline content: @Composable AnimatedContentScope.() -> Unit,
) = NavEntry(
    key = key,
    metadata = buildMap {
        putAll(metadata)
        transitionSpec?.let { putAll(NavDisplay.transitionSpec(transitionSpec)) }
        popTransitionSpec?.let { putAll(NavDisplay.popTransitionSpec(popTransitionSpec)) }
    },
    content = { _ ->
        with(LocalNavAnimatedContentScope.current) {
            content()
        }
    },
)

/**
 * Move an element to the last position in the backstack.
 * @receiver the navigation backstack
 * @param element the element to move
 */
private fun <T : @Serializable NavKey> NavBackStack<T>.singletonToLast(element: T) {
    if (lastOrNull() == element) return

    if (element is Home) {
        clearAndAdd(element)
        return
    }

    val lastIndex = indexOfLast { it == element }
    if (lastIndex != -1) {
        navigationLogger.v { "Moving $element to last position" }
        Snapshot.withMutableSnapshot {
            val lastElement = removeAt(lastIndex)
            removeAll { it == element }
            add(lastElement)
        }
        return
    }
    navigationLogger.v { "Adding $element to last position" }
    add(element)
}

/**
 * Move an element to the first position in the backstack.
 * @receiver the navigation backstack
 * @param element the element to move
 */
private fun <T : @Serializable NavKey> NavBackStack<T>.singletonToFirst(element: T) {
    if (firstOrNull() == element) return

    if (element is Home) {
        clearAndAdd(element)
        return
    }

    val lastIndex = indexOfLast { it == element }
    if (lastIndex != -1) {
        navigationLogger.v { "Moving $element to first position" }
        Snapshot.withMutableSnapshot {
            val lastElement = removeAt(lastIndex)
            removeAll { it == element }
            add(0, lastElement)
        }
        return
    }
    navigationLogger.v { "Adding $element to first position" }
    add(0, element)
}

/**
 * Clears the entire backstack and adds a single element.
 * This is useful for resetting the navigation to a specific screen, like the home screen.
 * @receiver the navigation backstack
 * @param element the single element to add to the backstack after clearing it.
 */
private fun <T : @Serializable NavKey> NavBackStack<T>.clearAndAdd(element: T) {
    navigationLogger.v { "Clearing backstack and adding $element" }
    Snapshot.withMutableSnapshot {
        clear()
        add(element)
    }
}
