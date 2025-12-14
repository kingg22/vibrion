package io.github.kingg22.vibrion.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val preferenceModule: Module
    get() = module {
        single<DataStore<Preferences>> { androidContext().dataStore }
    }

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    DATA_STORE_FILENAME.removeSuffix(".preferences_pb"),
)
