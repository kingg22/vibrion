package io.github.kingg22.vibrion.app.di

import io.github.kingg22.vibrion.app.ui.screens.search.SearchViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { SearchViewModel() }
}
