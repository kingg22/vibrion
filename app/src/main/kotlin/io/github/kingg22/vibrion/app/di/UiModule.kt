package io.github.kingg22.vibrion.app.di

import io.github.kingg22.vibrion.app.ui.screens.search.SearchViewModel
import io.github.kingg22.vibrion.app.ui.screens.settings.SettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel { SearchViewModel(get(), get(), get(), get()) }
    viewModel { SettingsViewModel(get()) }
}
