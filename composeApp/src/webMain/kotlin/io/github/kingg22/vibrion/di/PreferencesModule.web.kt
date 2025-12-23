package io.github.kingg22.vibrion.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath
import org.koin.core.module.Module
import org.koin.dsl.module

actual val preferenceModule: Module
    get() = module {
        single<DataStore<Preferences>> { _ ->
            PreferenceDataStoreFactory.createWithPath { DATA_STORE_FILENAME.toPath() }
        }
    }
