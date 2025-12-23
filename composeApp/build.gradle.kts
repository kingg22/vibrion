import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmDefaultMode
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    alias(libs.plugins.aboutLibraries)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
}

group = "io.github.kingg22"
version = "0.0.1"

enum class OS(val id: String) {
    Linux("linux"),
    Windows("windows"),
    MacOS("macos"),
}

enum class Arch(val id: String) {
    X64("x64"),
    Arm64("arm64"),
}

data class Target(val os: OS, val arch: Arch) {
    val id: String get() = "${os.id}-${arch.id}"
}

val currentTarget by lazy {
    Target(currentOS, currentArch)
}

val currentArch: Arch by lazy {
    when (val osArch = System.getProperty("os.arch")) {
        "x86_64", "amd64" -> Arch.X64
        "aarch64" -> Arch.Arm64
        else -> error("Unsupported OS arch: $osArch")
    }
}

val currentOS: OS by lazy {
    val os = System.getProperty("os.name")
    when {
        os.equals("Mac OS X", ignoreCase = true) -> OS.MacOS
        os.startsWith("Win", ignoreCase = true) -> OS.Windows
        os.startsWith("Linux", ignoreCase = true) -> OS.Linux
        else -> error("Unknown OS name: $os")
    }
}

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

                // sentry for android
                implementation(project.dependencies.platform(libs.sentry.bom))
                api(libs.sentry.ktor.client)

                // exoplayer for android
                api(libs.androidx.media3.exoplayer)
                api(libs.androidx.media3.session)
            }
        }
        jvmMain {
            dependencies {
                // Add JVM-specific dependencies here.
                api(
                    "org.jetbrains.compose.desktop:desktop-jvm-${currentTarget.id}:${libs.versions.compose.multiplatform.get()}",
                )
                api(libs.kotlinx.coroutines.swing)

                // sentry for kotlin
                implementation(project.dependencies.platform(libs.sentry.bom))
                api(libs.sentry.ktor.client)
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

aboutLibraries {
    export {
        // Define the output path (including fileName). Modifying this will disable the automatic meta data discovery for supported platforms.
        outputFile.set(file("src/commonMain/composeResources/files/aboutlibraries.json"))
        // Enable pretty printing for the generated JSON file
        prettyPrint.set(true)
    }
    library {
        // Enable the duplication mode, allows to merge, or link dependencies which relate
        duplicationMode.set(com.mikepenz.aboutlibraries.plugin.DuplicateMode.MERGE)
        // Configure the duplication rule, to match "duplicates" with
        duplicationRule.set(com.mikepenz.aboutlibraries.plugin.DuplicateRule.GROUP)
    }
}

configurations.configureEach {
    exclude(group = "io.sentry", module = "sentry-android-ndk")
}
