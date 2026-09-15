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
                implementation(projects.identity.identityDomain)
                implementation(projects.lookups.lookupsDomain)
                implementation(projects.shared.sharedDomain)
                implementation(libs.coil.compose)
            }
        }
    }
}

dependencies {
    "androidRuntimeClasspath"(libs.compose.ui.tooling)
}
