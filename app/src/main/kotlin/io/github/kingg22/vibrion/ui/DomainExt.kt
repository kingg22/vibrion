package io.github.kingg22.vibrion.ui

import androidx.paging.PagingSource
import androidx.paging.PagingState
import io.github.kingg22.vibrion.R
import io.github.kingg22.vibrion.domain.model.Quality
import io.github.kingg22.vibrion.domain.model.Quality.*
import io.github.kingg22.vibrion.domain.model.ThemeMode
import io.github.kingg22.vibrion.domain.model.ThemeMode.*
import io.github.kingg22.vibrion.domain.repository.PagedSource
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive

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
