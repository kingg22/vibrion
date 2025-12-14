package io.github.kingg22.vibrion.data.repository

import co.touchlab.kermit.Logger
import io.github.kingg22.vibrion.data.datasource.music.DeezerApiDataSource
import io.github.kingg22.vibrion.data.model.toDomain
import io.github.kingg22.vibrion.domain.model.ArtistInfo
import io.github.kingg22.vibrion.domain.model.DownloadableItem
import io.github.kingg22.vibrion.domain.model.GenreInfo
import io.github.kingg22.vibrion.domain.model.VibrionPage
import io.github.kingg22.vibrion.domain.repository.PagedSource
import io.github.kingg22.vibrion.domain.repository.TrendsRepository

class TrendsRepositoryImpl(private val api: DeezerApiDataSource) : TrendsRepository {
    companion object {
        private val logger = Logger.withTag("TrendsRepository")
    }

    override fun buildTracksPagedSource() = PagedSource<Int, DownloadableItem> { key, size ->
        logger.d { "Loading trends tracks with key $key and size $size" }

        val offset = key ?: 0
        val response = api.getTopTracksTrends(index = offset, limit = size)
        val data = response?.data?.map { it.toDomain() } ?: emptyList()

        val nextKey = if (data.size < size) null else offset + size
        val prevKey = if (offset == 0) null else maxOf(offset - size, 0)

        logger.d {
            "Loaded trends tracks with key $key and size $size. " +
                "Next key: $nextKey. Previous key: $prevKey. Total: ${response?.total}."
        }

        VibrionPage(data = data, prevKey = prevKey, nextKey = nextKey)
    }

    override fun buildGenresPagedSource() = PagedSource<Int, GenreInfo> { key, size ->
        logger.d { "Loading genres with key $key and size $size" }

        val offset = key ?: 0
        val response = api.getGenres(index = offset, limit = size)
        val data = response?.data?.map { it.toDomain() } ?: emptyList()

        val nextKey = if (data.size < size) null else offset + size
        val prevKey = if (offset == 0) null else maxOf(offset - size, 0)

        logger.d {
            "Loaded genres with key $key and size $size. " +
                "Next key: $nextKey. Previous key: $prevKey. Total: ${response?.total}."
        }

        VibrionPage(data = data, prevKey = prevKey, nextKey = nextKey)
    }

    override fun buildArtistsPagedSource() = PagedSource<Int, ArtistInfo> { key, size ->
        logger.d { "Loading trends artists with key $key and size $size" }

        val offset = key ?: 0
        val response = api.getArtistTrends(index = offset, limit = size)
        val data = response?.data?.map { it.toDomain() } ?: emptyList()

        val nextKey = if (data.size < size) null else offset + size
        val prevKey = if (offset == 0) null else maxOf(offset - size, 0)

        logger.d {
            "Loaded trends artist with key $key and size $size. " +
                "Next key: $nextKey. Previous key: $prevKey. Total: ${response?.total}."
        }

        VibrionPage(data = data, prevKey = prevKey, nextKey = nextKey)
    }

    override fun buildAlbumsPagedSource() = PagedSource<Int, DownloadableItem> { key, size ->
        logger.d { "Loading trends albums with key $key and size $size" }

        val offset = key ?: 0
        val response = api.getAlbumTrends(index = offset, limit = size)
        val data = response?.data?.map { it.toDomain() } ?: emptyList()

        val nextKey = if (data.size < size) null else offset + size
        val prevKey = if (offset == 0) null else maxOf(offset - size, 0)

        logger.d {
            "Loaded trends albums with key $key and size $size. " +
                "Next key: $nextKey. Previous key: $prevKey. Total: ${response?.total}."
        }

        VibrionPage(data = data, prevKey = prevKey, nextKey = nextKey)
    }

    override fun buildPlaylistsPagedSource() = PagedSource<Int, DownloadableItem> { key, size ->
        logger.d { "Loading trends playlists with key $key and size $size" }

        val offset = key ?: 0
        val response = api.getTopPlaylistTrends(index = offset, limit = size)
        val data = response?.data?.map { it.toDomain() } ?: emptyList()

        val nextKey = if (data.size < size) null else offset + size
        val prevKey = if (offset == 0) null else maxOf(offset - size, 0)

        logger.d {
            "Loaded trends playlists with key $key and size $size. " +
                "Next key: $nextKey. Previous key: $prevKey. Total: ${response?.total}."
        }

        VibrionPage(data = data, prevKey = prevKey, nextKey = nextKey)
    }
}
