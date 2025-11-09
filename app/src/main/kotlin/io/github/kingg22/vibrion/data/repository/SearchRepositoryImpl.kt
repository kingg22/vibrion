package io.github.kingg22.vibrion.data.repository

import co.touchlab.kermit.Logger
import io.github.kingg22.vibrion.data.datasource.music.DeezerApiDataSource
import io.github.kingg22.vibrion.data.model.toDomain
import io.github.kingg22.vibrion.domain.model.ArtistInfo
import io.github.kingg22.vibrion.domain.model.DownloadableAlbum
import io.github.kingg22.vibrion.domain.model.DownloadablePlaylist
import io.github.kingg22.vibrion.domain.model.DownloadableSingle
import io.github.kingg22.vibrion.domain.model.VibrionPage
import io.github.kingg22.vibrion.domain.repository.PagedSource
import io.github.kingg22.vibrion.domain.repository.SearchRepository

@JvmInline
value class SearchRepositoryImpl(private val api: DeezerApiDataSource) : SearchRepository {
    override suspend fun findSingle(id: String): DownloadableSingle? = api.findSingle(id.toLong())?.toDomain()

    override suspend fun findPlaylist(id: String): DownloadablePlaylist? = api.findPlaylist(id.toLong())?.toDomain()

    override suspend fun findAlbum(id: String): DownloadableAlbum? = api.findAlbum(id.toLong())?.toDomain()

    override suspend fun findArtist(id: String): ArtistInfo? = api.findArtist(id.toLong())?.toDomain()

    override suspend fun searchSingles(query: String, limit: Int): List<DownloadableSingle> =
        api.searchSingle(query, limit, 0)?.data?.map { it.toDomain() } ?: emptyList()

    override suspend fun searchPlaylists(query: String, limit: Int): List<DownloadablePlaylist> =
        api.searchPlaylist(query, limit, 0)?.data?.map { it.toDomain() } ?: emptyList()

    override suspend fun searchAlbums(query: String, limit: Int): List<DownloadableAlbum> =
        api.searchAlbum(query, limit, 0)?.data?.map { it.toDomain() } ?: emptyList()

    override suspend fun searchArtists(query: String, limit: Int): List<ArtistInfo> =
        api.searchArtist(query, limit, 0)?.data?.map { it.toDomain() } ?: emptyList()

    override fun buildPagedSearchSingles(query: String) = PagedSource<Int, DownloadableSingle> { key, size ->
        logger.d { "Loading search singles for query $query with key $key and size $size" }

        val offset = key ?: 0
        val response = api.searchSingle(query, size, offset)
        val data = response?.data?.map { it.toDomain() } ?: emptyList()

        val nextKey = if (data.size < size) null else offset + size
        val prevKey = if (offset == 0) null else maxOf(offset - size, 0)

        logger.d {
            "Loaded search singles for query '$query' with key $key and size $size. " +
                "Next key: $nextKey. Previous key: $prevKey. Total: ${response?.total}."
        }

        VibrionPage(data = data, prevKey = prevKey, nextKey = nextKey)
    }

    override fun buildPagedSearchPlaylists(query: String) = PagedSource<Int, DownloadablePlaylist> { key, size ->
        logger.d { "Loading search playlists for query $query with key $key and size $size" }
        val offset = key ?: 0
        val response = api.searchPlaylist(query, size, offset)
        val data = response?.data?.map { it.toDomain() } ?: emptyList()
        val nextKey = if (data.size < size) null else offset + size
        val prevKey = if (offset == 0) null else maxOf(offset - size, 0)

        logger.d {
            "Loaded search playlists for query '$query' with key $key and size $size. " +
                "Next key: $nextKey. Previous key: $prevKey. Total: ${response?.total}."
        }
        VibrionPage(data = data, prevKey = prevKey, nextKey = nextKey)
    }

    override fun buildPagedSearchAlbums(query: String) = PagedSource<Int, DownloadableAlbum> { key, size ->
        logger.d { "Loading search albums for query $query with key $key and size $size" }
        val offset = key ?: 0
        val response = api.searchAlbum(query, size, offset)

        val data = response?.data?.map { it.toDomain() } ?: emptyList()
        val nextKey = if (data.size < size) null else offset + size
        val prevKey = if (offset == 0) null else maxOf(offset - size, 0)

        logger.d {
            "Loaded search albums for query '$query' with key $key and size $size. " +
                "Next key: $nextKey. Previous key: $prevKey. Total: ${response?.total}."
        }

        VibrionPage(data = data, prevKey = prevKey, nextKey = nextKey)
    }

    override fun buildPagedSearchArtists(query: String): PagedSource<Int, ArtistInfo> = PagedSource { key, size ->
        logger.d { "Loading search artists for query $query with key $key and size $size" }
        val offset = key ?: 0
        val response = api.searchArtist(query, size, offset)

        val data = response?.data?.map { it.toDomain() } ?: emptyList()
        val nextKey = if (data.size < size) null else offset + size
        val prevKey = if (offset == 0) null else maxOf(offset - size, 0)

        logger.d {
            "Loaded search artists for query '$query' with key $key and size $size. " +
                "Next key: $nextKey. Previous key: $prevKey. Total: ${response?.total}."
        }

        VibrionPage(data = data, prevKey = prevKey, nextKey = nextKey)
    }

    companion object {
        private val logger = Logger.withTag("SearchRepository")
    }
}
