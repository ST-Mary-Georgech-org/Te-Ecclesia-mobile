plugins {
    id("teecclesia.kmp.library")
}

kotlin {
    android {
        namespace = "com.teEcclesia.logging"
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.koin.core)
            }
        }
        androidMain {
            dependencies {
                implementation(libs.firebase.crashlytics)
            }
        }
    }
}
