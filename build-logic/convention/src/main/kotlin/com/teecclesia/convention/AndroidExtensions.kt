package com.teecclesia.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project

internal fun Project.configureAndroidLibrary(extension: LibraryExtension) {
    extension.apply {
        compileSdk = libs.findVersion("android-compileSdk").get().requiredVersion.toInt()
        defaultConfig {
            minSdk = libs.findVersion("android-minSdk").get().requiredVersion.toInt()
        }
    }
}

internal fun Project.configureAndroidApplication(extension: ApplicationExtension) {
    extension.apply {
        compileSdk = libs.findVersion("android-compileSdk").get().requiredVersion.toInt()
        defaultConfig {
            minSdk = libs.findVersion("android-minSdk").get().requiredVersion.toInt()
            targetSdk = libs.findVersion("android-targetSdk").get().requiredVersion.toInt()
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }
        buildFeatures {
            buildConfig = true
        }
        packaging {
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }
        }
    }
}
