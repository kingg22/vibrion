package io.github.kingg22.vibrion.data.datasource.music

import co.touchlab.kermit.Logger
import io.github.kingg22.deezer.client.gw.DeezerGwClient
import io.github.kingg22.deezer.client.gw.objects.GwTrack
import io.github.kingg22.deezer.client.gw.requests.MediaRequest
import io.github.kingg22.deezer.client.gw.requests.MediaRequest.Companion.toMediaRequest
import io.github.kingg22.deezer.client.gw.withTokenCheck
import io.github.kingg22.deezer.client.utils.ExperimentalDeezerClient
import io.github.kingg22.deezer.client.utils.HttpClientBuilder
import io.github.kingg22.deezer.client.utils.UnofficialDeezerApi
import io.github.kingg22.vibrion.core.application.DownloadOrchestrator
import io.github.kingg22.vibrion.core.domain.model.Download
import io.github.kingg22.vibrion.core.domain.model.DownloadProvider
import io.github.kingg22.vibrion.core.domain.model.MusicMetadata
import io.github.kingg22.vibrion.data.PlatformHelper
import io.github.kingg22.vibrion.domain.model.DownloadableAlbum
import io.github.kingg22.vibrion.domain.model.DownloadablePlaylist
import io.github.kingg22.vibrion.domain.model.DownloadableSingle
import io.github.kingg22.vibrion.ext.DeezerId3
import io.github.kingg22.vibrion.ext.toMusicMetadata
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch

@OptIn(UnofficialDeezerApi::class)
class DeezerGwDataSource(
    private val httpClientBuilder: HttpClientBuilder,
    private val orchestrator: DownloadOrchestrator,
    private val platformHelper: PlatformHelper,
) {
    companion object {
        private val logger = Logger.withTag("DeezerGwDataSource")
    }
    private lateinit var gwClient: DeezerGwClient
    private lateinit var latestToken: String

    suspend fun canDownload(token: String): Boolean = try {
        val result = DeezerGwClient.verifyArl(token, httpClientBuilder)
        if (result != null) {
            logger.e { "Token is invalid because is $result" }
        }
        result == null
    } catch (e: Exception) {
        currentCoroutineContext().ensureActive()
        logger.e(e) { "Error verifying token" }
        false
    }

    suspend fun downloadAlbum(album: DownloadableAlbum, token: String) = try {
        restartIfNecessary(token)
        val pagedTracks = gwClient.withTokenCheck {
            tracks.getTracksAlbum(album.id.toLong())
        }
        downloadTracks(
            title = album.title,
            trackTokens = pagedTracks.data.map { it.trackToken },
            getTrack = { pagedTracks.data[it] },
            type = "album",
        )
    } catch (e: Exception) {
        currentCoroutineContext().ensureActive()
        logger.e(e) { "Error downloading album with id '${album.id}'" }
        false
    }

    suspend fun downloadPlaylist(playlist: DownloadablePlaylist, token: String) = try {
        restartIfNecessary(token)
        val pagedTracks = gwClient.withTokenCheck {
            tracks.getTracksPlaylist(playlist.id.toLong())
        }
        downloadTracks(
            title = playlist.title,
            trackTokens = pagedTracks.data.map { it.trackToken },
            getTrack = { pagedTracks.data[it] },
            type = "playlist",
        )
    } catch (e: Exception) {
        currentCoroutineContext().ensureActive()
        logger.e(e) { "Error downloading playlist with id '${playlist.id}'" }
        false
    }

    suspend fun downloadSingle(single: DownloadableSingle, token: String): Boolean {
        return try {
            restartIfNecessary(token)
            val track = gwClient.withTokenCheck {
                tracks.getTrackData(single.id.toLong())
            }
            // Don't call withTokenCheck because the previous request has already verified the token
            val mediaResult = gwClient.getMedias(track.trackToken.toMediaRequest())
            val urls = mediaResult.getAllUrls()

            if (urls.isEmpty()) {
                logger.e { "No URLs found for single ${single.id}" }
                return false
            }

            coroutineScope {
                launch {
                    platformHelper.foregroundServiceRequired()
                    val download = buildDownload(
                        url = urls.first(),
                        fallbacks = urls.drop(1).toSet(),
                        title = single.title,
                        filePath = "vibrion/${single.title}",
                        trackId = single.id.toLong(),
                        metadata = track.toTrack().toMusicMetadata(),
                    )
                    orchestrator.createDownload(platformHelper.enhanceDownload(download))
                    logger.i { "Single '${single.title}' successfully enqueued" }
                }
            }
            true
        } catch (e: Exception) {
            currentCoroutineContext().ensureActive()
            logger.e(e) { "Error downloading single '${single.title}'" }
            false
        }
    }

    private suspend inline fun downloadTracks(
        title: String,
        trackTokens: List<String>,
        crossinline getTrack: (Int) -> GwTrack,
        type: String,
    ): Boolean {
        logger.d { "Start downloading $type '$title'" }

        val mediaResult = gwClient.withTokenCheck {
            getMedias(MediaRequest(trackTokens)).getAllMedias()
        }
        if (mediaResult.isEmpty()) {
            logger.e { "No media found for $type '$title'" }
            return false
        }

        coroutineScope {
            platformHelper.foregroundServiceRequired()
            val folder = "vibrion/$title"

            trackTokens.indices.forEach { index ->
                val track = getTrack(index)
                val urls = mediaResult[index].getAllUrls()
                if (urls.isEmpty()) {
                    logger.e { "Track ${track.sngId} has no downloadable URLs" }
                    return@forEach
                }

                val download = buildDownload(
                    url = urls.first(),
                    fallbacks = urls.drop(1).toSet(),
                    title = track.sngTitle,
                    filePath = "$folder/${track.sngTitle}",
                    trackId = track.sngId,
                    metadata = track.toTrack().toMusicMetadata(),
                )

                launch(SupervisorJob()) {
                    try {
                        orchestrator.createDownload(platformHelper.enhanceDownload(download))
                        logger.i { "Track '${track.sngTitle}' with id '${track.sngId}' successfully enqueued" }
                    } catch (e: Exception) {
                        logger.e(e) { "Error enqueueing track ${track.sngId}" }
                    }
                }
            }
        }

        logger.i { "All tracks for $type '$title' successfully enqueued" }
        return true
    }

    private fun buildDownload(
        url: String,
        fallbacks: Set<String>,
        title: String,
        filePath: String,
        trackId: Long,
        metadata: MusicMetadata,
    ) = Download(
        url = url,
        fileName = title,
        filePath = filePath,
        provider = DownloadProvider.DeezerId3,
        processMetadata = DownloadProvider.deezerProcessMetadata(trackId),
        musicMetadata = metadata,
        fallbacksUrl = fallbacks,
    )

    private suspend fun restartIfNecessary(token: String) {
        val shouldInit = !::latestToken.isInitialized || !::gwClient.isInitialized
        if (shouldInit || latestToken != token) {
            logger.d { if (shouldInit) "Initializing Deezer client" else "Restarting client with new token" }
            try {
                if (!shouldInit) {
                    @OptIn(ExperimentalDeezerClient::class)
                    gwClient.httpClient.use { it.coroutineContext.cancel() }
                }
                gwClient = DeezerGwClient.initialize(token, httpClientBuilder.copy())
                latestToken = token
            } catch (e: IllegalArgumentException) {
                logger.e(e) { "Failed to (re)initialize Deezer client" }
                throw IllegalStateException("Token changed without verification", e)
            }
        }
    }
}
