import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmDefaultMode
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
}

group = "io.github.kingg22"
version = "0.0.1"

kotlin {
    compilerOptions {
        extraWarnings.set(true)
        allWarningsAsErrors.set(true)
        optIn.addAll(
            "kotlin.contracts.ExperimentalContracts",
            "kotlin.time.ExperimentalTime",
            "kotlin.uuid.ExperimentalUuidApi",
            "androidx.compose.material3.ExperimentalMaterial3Api",
            "androidx.compose.animation.ExperimentalSharedTransitionApi",
        )
        freeCompilerArgs.add("-Xexpect-actual-classes")
        apiVersion.set(KotlinVersion.KOTLIN_2_3)
        languageVersion.set(apiVersion)
    }

    // Target declarations - add or remove as needed below. These define
    // which platforms this KMP module supports.
    // See: https://kotlinlang.org/docs/multiplatform-discover-project.html#targets
    androidLibrary {
        namespace = "$group.vibrion.composeApp"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
            jvmDefault.set(JvmDefaultMode.NO_COMPATIBILITY)
        }
        // why needs this?
        androidResources.enable = true
    }

    jvm()

    js {
        useEsModules()
        browser()
        binaries.executable()
    }

    @OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)
    wasmJs {
        useEsModules()
        browser()
        binaries.executable()
    }

    @OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
    dependencies {
        api(libs.jetbrains.multiplatform.resources)
        api(libs.jetbrains.compose.ui.tooling.preview)

        api(libs.jetbrains.compose.runtime)
        api(libs.jetbrains.compose.ui)
        api(libs.jetbrains.compose.foundation)
        api(libs.jetbrains.compose.material3)
        api(libs.jetbrains.compose.material.icons.extended)

        // androidx lifecycle
        api(libs.jetbrains.lifecycle.runtime.compose)

        // koin DI
        api(libs.koin.compose)
        api(libs.koin.compose.viewmodel)

        // navigation3
        api(libs.jetbrains.navigation3.ui)
        api(libs.jetbrains.lifecycle.viewmodel.nav3)

        // data store preferences
        api(libs.androidx.data.store.preferences.core)

        // paging3
        api(libs.androidx.paging3.compose)

        // ktor client
        api(libs.ktor.client.engine.cio)
        api(libs.ktor.client.encoding)

        // coil for async images
        api(libs.coil.compose)
        api(libs.coil.ktor3)
        api(libs.coil.network.cache.control)

        // kermit for logging
        api(libs.kermit)

        // aboutlibraries
        api(libs.aboutlibraries.compose.m3)

        // deezer
        api(libs.deezer.client.kt)

        // sentry KMP
        api(libs.sentry.kotlin.multiplatform)
    }

    // Source set declarations.
    // Declaring a target automatically creates a source set with the same name. By default, the
    // Kotlin Gradle Plugin creates additional source sets that depend on each other, since it is
    // common to share sources between related targets.
    // See: https://kotlinlang.org/docs/multiplatform-hierarchy.html
    sourceSets {
        androidMain {
            dependencies {
                // Add Android-specific dependencies here. Note that this source set depends on
                // commonMain by default and will correctly pull the Android artifacts of any KMP
                // dependencies declared in commonMain.

                // datastore with android extension
                api(libs.androidx.data.store.preferences.android)

                // exoplayer for android
                api(libs.androidx.media3.exoplayer)
                api(libs.androidx.media3.session)
            }
        }
    }
}

dependencies {
    ktlintRuleset(libs.ktlint.compose)
}

compose {
    resources {
        packageOfResClass = "$group.vibrion"
        nameOfResClass = "R"
    }
    desktop {
        application {
            mainClass = "$group.vibrion.MainKt"

            nativeDistributions {
                targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
                packageName = "$group.vibrion"
                packageVersion = "1.0.0"
            }
        }
    }
}

composeCompiler {
    reportsDestination.set(layout.buildDirectory.dir("reports/compose_reports"))
    metricsDestination.set(layout.buildDirectory.dir("reports/compose_metrics"))
}

ktlint {
    version.set(libs.versions.ktlint.pinterest)
}

configurations.configureEach {
    exclude(group = "io.sentry", module = "sentry-android-ndk")
}
