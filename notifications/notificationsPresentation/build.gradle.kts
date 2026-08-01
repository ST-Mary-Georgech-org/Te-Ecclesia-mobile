plugins {
    id("teecclesia.kmp.feature.presentation")
}

kotlin {
    android {
        namespace = "com.teEcclesia.notifications.presentation"
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.designSystem)
                implementation(projects.notifications.notificationsDomain)
                implementation(projects.notifications.notificationsApi)
                implementation(projects.shared.sharedDomain)
            }
        }
    }
}

dependencies {
    "androidRuntimeClasspath"(libs.compose.ui.tooling)
}
