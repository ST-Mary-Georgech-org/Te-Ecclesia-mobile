plugins {
    id("teecclesia.kmp.compose.library")
    alias(libs.plugins.androidLint) apply false
}

compose {
    resources {
        publicResClass = true
    }
}

kotlin {
    android {
        namespace = "com.teEcclesia.designsystem"
        androidResources {
            enable = true
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlinx.datetime)
                
                implementation(libs.coil.compose)
                implementation(libs.coil.network.ktor)
                implementation(libs.coil.svg)

                implementation(libs.androidx.navigation3.ui)
                implementation(libs.androidx.lifecycle.viewmodel.navigation3)
                implementation(libs.compose.ui.backhandler)

                implementation(libs.koin.core)
                implementation(projects.logging)

                implementation(libs.qr.kit)
                implementation(libs.moko.permissions)
                implementation(libs.moko.permissions.compose)
                implementation(libs.moko.permissions.camera)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.androidx.activity.compose)
                implementation(libs.androidx.poolingcontainer)
                implementation(libs.firebase.crashlytics)
            }
        }
    }
}

dependencies {
    "androidRuntimeClasspath"(libs.compose.ui.tooling)
}