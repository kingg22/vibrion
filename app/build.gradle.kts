import org.jetbrains.kotlin.gradle.dsl.JvmDefaultMode
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.sentry)
    alias(libs.plugins.sentry.compiler)
}

group = "io.github.kingg22"
version = "0.0.9"

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
        // freeCompilerArgs.add("-Xexpect-actual-classes")
        apiVersion.set(KotlinVersion.KOTLIN_2_2)
        languageVersion.set(apiVersion)
        jvmTarget.set(JvmTarget.JVM_11)
        jvmDefault.set(JvmDefaultMode.NO_COMPATIBILITY)
    }
}

val doMinify = project.hasProperty("minify") && project.property("minify") == "true"
val doShrink = project.hasProperty("shrink") && project.property("shrink") == "true"

android {
    namespace = "$group.vibrion"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "$group.vibrion"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.compileSdk.get().toInt()
        versionCode = 9
        versionName = project.version.toString()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/LICENSE.md"
            excludes += "/META-INF/LICENSE-notice.md"
        }
    }
    signingConfigs {
        if (System.getenv("KEYSTORE_FILE") != null) {
            create("release") {
                storeFile = file(System.getenv("KEYSTORE_FILE"))
                storePassword = System.getenv("KEYSTORE_PASSWORD")
                keyAlias = System.getenv("KEY_ALIAS")
                keyPassword = System.getenv("KEY_PASSWORD")
            }
        }
    }
    buildTypes {
        release {
            signingConfig = signingConfigs.findByName("release")

            isMinifyEnabled = doMinify
            isShrinkResources = doShrink
            if ((doMinify || doShrink) && signingConfig == null) {
                logger.warn("Minify release without a signing key, this will fail in runtime")
            }
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true
        shaders = false
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui.tooling.preview)

    // android activity
    implementation(libs.androidx.activity.compose)

    // KTOR ENFORCE TO USE SL4FJ
    runtimeOnly(libs.slf4j.android)

    // koin DI android specific
    implementation(libs.koin.androidx.startup)

    // glance for android widgets with special compose API
    implementation(libs.androidx.glance.appwidget)
    implementation(libs.androidx.glance.material3)
    implementation(libs.androidx.glance.appwidget.preview)
    implementation(libs.androidx.glance.preview)

    // exoplayer for android
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.session)

    // sentry sdk for android
    implementation(platform(libs.sentry.bom))
    implementation(libs.sentry.kt)
    implementation(libs.sentry.android)
    implementation(libs.sentry.compose.android)
    implementation(libs.sentry.ktor.client)

    implementation(libs.compose.runtime)
    implementation(libs.compose.foundation)
    implementation(libs.compose.material3)
    implementation(libs.compose.material.icons.extended)
    implementation(libs.compose.ui)

    // androidx lifecycle
    implementation(libs.androidx.lifecycle.runtime.compose)

    // koin DI
    implementation(libs.koin.compose)
    implementation(libs.koin.compose.viewmodel)

    // navigation3
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.lifecycle.viewmodel.nav3)

    // data store preferences
    implementation(libs.androidx.data.store.preferences)

    // paging3
    implementation(libs.androidx.paging3.compose)

    // ktor client
    implementation(libs.ktor.client.engine.cio)
    implementation(libs.ktor.client.encoding)

    // coil for async images
    implementation(libs.coil.compose)
    implementation(libs.coil.ktor3)
    implementation(libs.coil.network.cache.control)

    // kermit for logging
    implementation(libs.kermit)

    // aboutlibraries
    implementation(libs.aboutlibraries.compose.m3)

    // deezer
    implementation(libs.deezer.client.kt)

    debugImplementation(platform(libs.compose.bom))
    debugImplementation(libs.compose.ui.tooling)
}

composeCompiler {
    reportsDestination.set(layout.buildDirectory.dir("reports/compose_reports"))
    metricsDestination.set(layout.buildDirectory.dir("reports/compose_metrics"))
}

/*
aboutLibraries {
    library {
        duplicationRule.set(com.mikepenz.aboutlibraries.plugin.DuplicateRule.GROUP)
        duplicationMode.set(com.mikepenz.aboutlibraries.plugin.DuplicateMode.MERGE)
    }
    export {
        outputFile.set(file("src/main/res/raw/aboutlibraries.json"))
        prettyPrint.set(true)
    }
}
 */

val hasSentryToken = System.getenv("SENTRY_AUTH_TOKEN") != null
logger.info("Sentry source code context upload token: $hasSentryToken")
if ((doMinify || doShrink) && !hasSentryToken) {
    logger.warn(
        "Minify release without a sentry token, all sentry reports for this build ($version) will contain obfuscated code",
    )
}

sentry {
    autoInstallation.enabled.set(false)
    org.set("kingg22")
    projectName.set("vibrion")
    telemetry.set(false)

    if (hasSentryToken) {
        authToken.set(System.getenv("SENTRY_AUTH_TOKEN"))
        includeSourceContext.set(true)
        autoUploadProguardMapping.set(true)
        autoUploadSourceContext.set(true)
    } else {
        includeSourceContext.set(false)
        autoUploadProguardMapping.set(false)
        autoUploadSourceContext.set(false)
    }
}

configurations.configureEach {
    exclude(group = "io.sentry", module = "sentry-android-ndk")
}
