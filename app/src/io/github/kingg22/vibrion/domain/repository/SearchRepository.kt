package io.github.kingg22.vibrion.domain.repository

import io.github.kingg22.vibrion.domain.model.ArtistInfo
import io.github.kingg22.vibrion.domain.model.DownloadableAlbum
import io.github.kingg22.vibrion.domain.model.DownloadablePlaylist
import io.github.kingg22.vibrion.domain.model.DownloadableSingle

interface SearchRepository {
    suspend fun findSingle(id: String): DownloadableSingle?
    suspend fun findPlaylist(id: String): DownloadablePlaylist?
    suspend fun findAlbum(id: String): DownloadableAlbum?
    suspend fun findArtist(id: String): ArtistInfo?

    suspend fun searchSingles(query: String, limit: Int = 10): List<DownloadableSingle>
    suspend fun searchPlaylists(query: String, limit: Int = 10): List<DownloadablePlaylist>
    suspend fun searchAlbums(query: String, limit: Int = 10): List<DownloadableAlbum>
    suspend fun searchArtists(query: String, limit: Int = 10): List<ArtistInfo>

    /**
     * Setup a paged source for the given query.
     * @see PagedSource.loadPage
     */
    fun buildPagedSearchSingles(query: String): PagedSource<Int, DownloadableSingle>
    fun buildPagedSearchPlaylists(query: String): PagedSource<Int, DownloadablePlaylist>
    fun buildPagedSearchAlbums(query: String): PagedSource<Int, DownloadableAlbum>
    fun buildPagedSearchArtists(query: String): PagedSource<Int, ArtistInfo>
}
