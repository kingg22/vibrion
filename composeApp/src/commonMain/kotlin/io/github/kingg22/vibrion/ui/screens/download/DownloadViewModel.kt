package io.github.kingg22.vibrion.ui.screens.download

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import io.github.kingg22.vibrion.domain.model.DownloadableItem
import io.github.kingg22.vibrion.domain.service.DownloadService
import io.github.kingg22.vibrion.domain.usecase.settings.LoadTokenUseCase
import io.sentry.kotlin.multiplatform.Sentry
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DownloadViewModel(
    private val downloadService: DownloadService,
    loadTokenUseCase: LoadTokenUseCase,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default,
) : ViewModel() {
    companion object {
        private val logger = Logger.withTag("DownloadViewModel")
    }
    private val token = loadTokenUseCase()

    @OptIn(ExperimentalCoroutinesApi::class)
    val canDownloadState = token
        .distinctUntilChanged()
        .mapLatest { token ->
            logger.d { "Token changed for download." }

            if (token == null) {
                logger.d { "Token is null, can't download." }
                return@mapLatest CanDownloadState.Unavailable
            }

            // emit(CanDownloadState.Loading)

            try {
                val canDownload = downloadService.canDownload(token)
                if (canDownload) {
                    logger.i { "Can download with current token." }
                    CanDownloadState.Success
                } else {
                    logger.w { "Can't download with the current token" }
                    CanDownloadState.Unavailable
                }
            } catch (e: Exception) {
                currentCoroutineContext().ensureActive()
                logger.e(e) { "Error while checking if can download." }
                CanDownloadState.Error
            }
        }
        .catch { e ->
            logger.e(e) { "Unexpected error while loading token for download." }
            emit(CanDownloadState.Error)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = CanDownloadState.Idle,
        )

    fun download(item: DownloadableItem) {
        /*val transaction = Sentry.startTransaction(
            "Download ${item.getModelType()}",
            "download",
        )
         */
        Sentry.configureScope { scope ->
            // scope.transaction = transaction
            scope.setExtra("item", item.toString())
        }
        if (!canDownloadState.value.isSuccess) {
            logger.e { "Can't download, state is not success, this is an implementation error." }
            Sentry.captureMessage("Can't download, state is not success, item: $item")
            // transaction.finish(SpanStatus.INTERNAL_ERROR)
            return
        }
        logger.v { "Try to download $item" }
        viewModelScope.launch {
            val currentToken = withContext(defaultDispatcher) { token.filterNotNull().firstOrNull() }
            if (currentToken == null) {
                logger.e { "Can't download, token is null, this is an implementation error." }
                Sentry.captureMessage("Can't download, user token is null, item: $item")
                // transaction.finish(SpanStatus.INTERNAL_ERROR)
                return@launch
            }
            logger.d { "Downloading $item" }
            try {
                downloadService.downloadItem(item, currentToken)
                // transaction.finish(SpanStatus.OK)
            } catch (e: Exception) {
                currentCoroutineContext().ensureActive()
                Sentry.captureException(e)
                // transaction.finish(SpanStatus.INTERNAL_ERROR)
            }
        }
    }

    enum class CanDownloadState {
        Idle,
        Loading,
        Success,
        Unavailable,
        Error,
        ;

        val isSuccess
            get() = this == Success
    }
}
