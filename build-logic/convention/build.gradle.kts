plugins {
    `kotlin-dsl`
}

group = "com.teecclesia.convention"

dependencies {
    // We hardcode these versions here to avoid bootstrap issues with the version catalog 
    // in the build-logic's own build script. The convention plugins themselves 
    // will still use the version catalog at runtime.
    compileOnly("com.android.tools.build:gradle:9.2.1")
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:2.3.21")
    compileOnly("org.jetbrains.compose:compose-gradle-plugin:1.11.0")
    compileOnly("androidx.room:room-gradle-plugin:2.8.4")
}

gradlePlugin {
    plugins {
        register("kmpLibrary") {
            id = "teecclesia.kmp.library"
            implementationClass = "KmpLibraryConventionPlugin"
        }
        register("kmpComposeLibrary") {
            id = "teecclesia.kmp.compose.library"
            implementationClass = "KmpComposeLibraryConventionPlugin"
        }
        register("kmpFeatureApi") {
            id = "teecclesia.kmp.feature.api"
            implementationClass = "KmpFeatureApiConventionPlugin"
        }
        register("kmpFeaturePresentation") {
            id = "teecclesia.kmp.feature.presentation"
            implementationClass = "KmpFeaturePresentationConventionPlugin"
        }
        register("kmpFeatureData") {
            id = "teecclesia.kmp.feature.data"
            implementationClass = "KmpFeatureDataConventionPlugin"
        }
        register("kmpFeatureDomain") {
            id = "teecclesia.kmp.feature.domain"
            implementationClass = "KmpFeatureDomainConventionPlugin"
        }
        register("kmpApplication") {
            id = "teecclesia.kmp.application"
            implementationClass = "KmpApplicationConventionPlugin"
        }
        register("kmpRoom") {
            id = "teecclesia.kmp.room"
            implementationClass = "com.teecclesia.convention.KmpRoomConventionPlugin"
        }
    }
}
