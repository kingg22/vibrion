package io.github.kingg22.vibrion.data.datasource.music

import co.touchlab.kermit.Logger
import io.github.kingg22.deezer.client.api.DeezerApiClient
import io.github.kingg22.deezer.client.api.routes.SearchRoutes.Companion.buildAdvancedQuery
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive

@JvmInline
value class DeezerApiDataSource(private val api: DeezerApiClient) {
    suspend fun findSingle(id: Long) = try {
        api.tracks.getById(id)
    } catch (e: Exception) {
        currentCoroutineContext().ensureActive()
        logger.e("Error while fetching single with id $id", e)
        null
    }

    suspend fun findPlaylist(id: Long) = try {
        api.playlists.getById(id)
    } catch (e: Exception) {
        currentCoroutineContext().ensureActive()
        logger.e("Error while fetching playlist with id $id", e)
        null
    }

    suspend fun findAlbum(id: Long) = try {
        api.albums.getById(id)
    } catch (e: Exception) {
        currentCoroutineContext().ensureActive()
        logger.e("Error while fetching album with id $id", e)
        null
    }

    suspend fun findArtist(id: Long) = try {
        api.artists.getById(id)
    } catch (e: Exception) {
        currentCoroutineContext().ensureActive()
        logger.e("Error while fetching artist with id $id", e)
        null
    }

    suspend fun searchSingle(query: String, limit: Int, offset: Int) = try {
        api.searches.search(buildAdvancedQuery(q = query), index = offset, limit = limit)
    } catch (e: Exception) {
        currentCoroutineContext().ensureActive()
        logger.e("Error while searching singles with query $query", e)
        null
    }

    suspend fun searchAlbum(query: String, limit: Int, offset: Int) = try {
        api.searches.searchAlbum(buildAdvancedQuery(q = query), index = offset, limit = limit)
    } catch (e: Exception) {
        currentCoroutineContext().ensureActive()
        logger.e("Error while searching albums with query $query", e)
        null
    }

    suspend fun searchPlaylist(query: String, limit: Int, offset: Int) = try {
        api.searches.searchPlaylist(buildAdvancedQuery(q = query), index = offset, limit = limit)
    } catch (e: Exception) {
        currentCoroutineContext().ensureActive()
        logger.e("Error while searching playlists with query $query", e)
        null
    }

    suspend fun searchArtist(query: String, limit: Int, offset: Int) = try {
        api.searches.searchArtist(buildAdvancedQuery(q = query), index = offset, limit = limit)
    } catch (e: Exception) {
        currentCoroutineContext().ensureActive()
        logger.e("Error while searching artists with query $query", e)
        null
    }

    suspend fun getGenres(index: Int? = null, limit: Int? = null) = try {
        api.genres.getAll(index = index, limit = limit)
    } catch (e: Exception) {
        currentCoroutineContext().ensureActive()
        logger.e("Error while fetching genres", e)
        null
    }

    suspend fun getArtistTrends(index: Int? = null, limit: Int? = null) = try {
        api.charts.getArtists(index = index, limit = limit)
    } catch (e: Exception) {
        currentCoroutineContext().ensureActive()
        logger.e("Error while fetching artists trends", e)
        null
    }

    suspend fun getAlbumTrends(index: Int? = null, limit: Int? = null) = try {
        api.charts.getAlbums(index = index, limit = limit)
    } catch (e: Exception) {
        currentCoroutineContext().ensureActive()
        logger.e("Error while fetching albums trends", e)
        null
    }

    suspend fun getTopTracksTrends(index: Int? = null, limit: Int? = null) = try {
        api.charts.getTracks(index = index, limit = limit)
    } catch (e: Exception) {
        currentCoroutineContext().ensureActive()
        logger.e("Error while fetching tracks trends", e)
        null
    }

    suspend fun getTopPlaylistTrends(index: Int? = null, limit: Int? = null) = try {
        api.charts.getPlaylists(index = index, limit = limit)
    } catch (e: Exception) {
        currentCoroutineContext().ensureActive()
        logger.e("Error while fetching playlists trends", e)
        null
    }

    companion object {
        private val logger = Logger.withTag("DeezerApiDataSource")
    }
}
