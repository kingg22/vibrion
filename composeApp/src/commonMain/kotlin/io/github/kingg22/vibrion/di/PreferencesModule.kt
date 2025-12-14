package io.github.kingg22.vibrion.di

import org.koin.core.module.Module

/** A Koin module containing only a one data store preferences */
expect val preferenceModule: Module

/** Name of the data store preferences for vibrion, include `.preferences_pb` as suffix */
const val DATA_STORE_FILENAME = "datastore/vibrion.settings.preferences_pb"
