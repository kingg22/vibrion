package io.github.kingg22.vibrion.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toOkioPath
import org.koin.core.module.Module
import org.koin.dsl.module
import kotlin.io.path.Path

actual val preferenceModule: Module = module {
    single<DataStore<Preferences>> { _ ->
        val path = if (System.getProperty("os.name").contains("Windows", ignoreCase = true)) {
            Path(
                System.getenv("APPDATA"),
                "Vibrion",
                DATA_STORE_FILENAME,
            ).toOkioPath()
        } else {
            Path(
                System.getProperty("user.home"),
                ".config",
                "Vibrion",
                DATA_STORE_FILENAME,
            ).toOkioPath()
        }

        PreferenceDataStoreFactory.createWithPath {
            path
        }
    }
}
