import com.mikepenz.aboutlibraries.plugin.DuplicateMode
import com.mikepenz.aboutlibraries.plugin.DuplicateRule
import org.jetbrains.kotlin.gradle.dsl.JvmDefaultMode
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.aboutLibraries)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.poko)
}

group = "io.github.kingg22"
version = "0.0.4"

kotlin {
    compilerOptions {
        extraWarnings.set(true)
        allWarningsAsErrors.set(true)
        optIn.add("androidx.compose.material3.ExperimentalMaterial3Api")
        // freeCompilerArgs.add("-Xexpect-actual-classes")
        jvmTarget.set(JvmTarget.JVM_11)
        jvmDefault.set(JvmDefaultMode.NO_COMPATIBILITY)
    }
}

android {
    namespace = "$group.vibrion"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "$group.vibrion"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.compileSdk.get().toInt()
        versionCode = 4
        versionName = project.version.toString()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            // ONlY to run with AS IDE
            // signingConfig = signingConfigs.getByName("debug")
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

    // koin DI android specific
    implementation(libs.koin.androidx.startup)

    // glance for android widgets with special compose API
    implementation(libs.androidx.glance.appwidget)
    implementation(libs.androidx.glance.material3)
    implementation(libs.androidx.glance.appwidget.preview)
    implementation(libs.androidx.glance.preview)

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

    // coil for async images
    implementation(libs.coil.compose)
    implementation(libs.coil.ktor3)
    implementation(libs.coil.network.cache.control)

    // kermit for logging
    implementation(libs.kermit)

    // aboutlibraries
    implementation(libs.aboutlibraries.compose.m3)

    // internal projects
    implementation(projects.deezerSdkKt)
    implementation(projects.vibrionCore)
    implementation(projects.integrations.deezerCore)
    implementation(projects.integrations.id3Core)

    debugImplementation(platform(libs.compose.bom))
    debugImplementation(libs.compose.ui.tooling)
    ktlintRuleset(libs.ktlint.compose)
}

composeCompiler {
    reportsDestination.set(layout.buildDirectory.dir("reports/compose_reports"))
    metricsDestination.set(layout.buildDirectory.dir("reports/compose_metrics"))
}

aboutLibraries {
    library {
        duplicationRule.set(DuplicateRule.GROUP)
        duplicationMode.set(DuplicateMode.MERGE)
    }
    export {
        outputFile.set(file("src/main/res/raw/aboutlibraries.json"))
        prettyPrint.set(true)
    }
}

ktlint {
    version.set(libs.versions.ktlint.pinterest.get())
}
