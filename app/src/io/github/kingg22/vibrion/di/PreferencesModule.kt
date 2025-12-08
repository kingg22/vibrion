package io.github.kingg22.vibrion.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/** A Koin module containing only a one data store preferences */
val preferenceModule = module {
    single { androidContext().dataStore }
}

private val Context.dataStore by preferencesDataStore(DATA_STORE_FILENAME.removeSuffix(".preferences_pb"))

/** Name of the data store preferences for vibrion, include `.preferences_pb` as suffix */
const val DATA_STORE_FILENAME = "datastore/vibrion.settings.preferences_pb"
