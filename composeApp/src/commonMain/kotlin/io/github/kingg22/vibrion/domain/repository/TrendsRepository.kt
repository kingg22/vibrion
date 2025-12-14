package io.github.kingg22.vibrion.domain.repository

import io.github.kingg22.vibrion.domain.model.ArtistInfo
import io.github.kingg22.vibrion.domain.model.DownloadableItem
import io.github.kingg22.vibrion.domain.model.GenreInfo

/** Refers to all top tracks, genres, artist and others type can be displayed in Home screen */
interface TrendsRepository {
    fun buildTracksPagedSource(): PagedSource<Int, DownloadableItem>
    fun buildGenresPagedSource(): PagedSource<Int, GenreInfo>
    fun buildArtistsPagedSource(): PagedSource<Int, ArtistInfo>
    fun buildAlbumsPagedSource(): PagedSource<Int, DownloadableItem>
    fun buildPlaylistsPagedSource(): PagedSource<Int, DownloadableItem>
}
