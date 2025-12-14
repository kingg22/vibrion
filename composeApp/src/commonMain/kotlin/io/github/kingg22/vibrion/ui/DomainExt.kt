package io.github.kingg22.vibrion.ui

import androidx.paging.PagingSource
import androidx.paging.PagingState
import io.github.kingg22.vibrion.*
import io.github.kingg22.vibrion.domain.model.ArtistInfo
import io.github.kingg22.vibrion.domain.model.DownloadableAlbum
import io.github.kingg22.vibrion.domain.model.DownloadableItem
import io.github.kingg22.vibrion.domain.model.DownloadablePlaylist
import io.github.kingg22.vibrion.domain.model.DownloadableSingle
import io.github.kingg22.vibrion.domain.model.ModelType
import io.github.kingg22.vibrion.domain.model.Quality
import io.github.kingg22.vibrion.domain.model.Quality.AUTO
import io.github.kingg22.vibrion.domain.model.Quality.BEST
import io.github.kingg22.vibrion.domain.model.Quality.HIGH
import io.github.kingg22.vibrion.domain.model.Quality.LOW
import io.github.kingg22.vibrion.domain.model.Quality.MEDIUM
import io.github.kingg22.vibrion.domain.model.ThemeMode
import io.github.kingg22.vibrion.domain.model.ThemeMode.DARK
import io.github.kingg22.vibrion.domain.model.ThemeMode.LIGHT
import io.github.kingg22.vibrion.domain.model.ThemeMode.SYSTEM
import io.github.kingg22.vibrion.domain.repository.PagedSource
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive

fun DownloadableItem.getModelType() = when (this) {
    is DownloadableAlbum -> ModelType.ALBUM
    is DownloadablePlaylist -> ModelType.PLAYLIST
    is DownloadableSingle -> ModelType.SINGLE
}

fun ArtistInfo.getModelType() = ModelType.ARTIST

fun ModelType.getDisplayName() = when (this) {
    ModelType.ARTIST -> R.string.artists
    ModelType.ALBUM -> R.string.albums
    ModelType.PLAYLIST -> R.string.playlists
    ModelType.SINGLE -> R.string.songs
    ModelType.USER -> R.string.users
    ModelType.GENRE -> R.string.genres
}

fun Quality.getDisplayName() = when (this) {
    AUTO -> R.string.auto
    LOW -> R.string.low
    MEDIUM -> R.string.medium
    HIGH -> R.string.high
    BEST -> R.string.best
}

fun ThemeMode.getDisplayName() = when (this) {
    SYSTEM -> R.string.system
    LIGHT -> R.string.light
    DARK -> R.string.dark
}

/**
 * Converts a [PagedSource] to a [PagingSource].
 *
 * @receiver The [PagedSource] to convert.
 * @return A [PagingSource] that can be used with a [androidx.paging.Pager].
 * @see PagingSource
 */
fun <Key : Any, Value : Any> PagedSource<Key, Value>.asPagingSource(): PagingSource<Key, Value> =
    DomainPagingSource(this)

private class DomainPagingSource<Key : Any, Value : Any>(private val source: PagedSource<Key, Value>) :
    PagingSource<Key, Value>() {
    override suspend fun load(params: LoadParams<Key>): LoadResult<Key, Value> = try {
        val page = source.loadPage(params.key, params.loadSize)
        LoadResult.Page(data = page.data, prevKey = page.prevKey, nextKey = page.nextKey)
    } catch (e: Exception) {
        currentCoroutineContext().ensureActive()
        LoadResult.Error(e)
    }

    override fun getRefreshKey(state: PagingState<Key, Value>): Key? = state.anchorPosition?.let { position ->
        val closest = state.closestPageToPosition(position)
        closest?.prevKey ?: closest?.nextKey
    }
}
