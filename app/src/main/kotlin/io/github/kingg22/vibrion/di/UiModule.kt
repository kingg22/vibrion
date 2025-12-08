package io.github.kingg22.vibrion.di

import io.github.kingg22.vibrion.ui.screens.detail.DetailViewModel
import io.github.kingg22.vibrion.ui.screens.download.DownloadViewModel
import io.github.kingg22.vibrion.ui.screens.home.HomeViewModel
import io.github.kingg22.vibrion.ui.screens.search.SearchHistoryViewModel
import io.github.kingg22.vibrion.ui.screens.search.SearchViewModel
import io.github.kingg22.vibrion.ui.screens.settings.SettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val uiModule = module(false) {
    // View Models
    viewModel { SearchHistoryViewModel(get(), get(), get()) }
    viewModel { SearchViewModel(get(), get(), get(), get()) }
    viewModel { DownloadViewModel(get(), get()) }
    viewModel { DetailViewModel(get()) }
    viewModel { HomeViewModel(get(), get(), get(), get(), get()) }
} + module(true) {
    viewModel { SettingsViewModel(get(), get()) }
}
