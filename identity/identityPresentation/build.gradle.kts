plugins {
    id("teecclesia.kmp.feature.presentation")
}

kotlin {
    android {
        namespace = "com.teEcclesia.identity.presentation"
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.designSystem)
                implementation(projects.identity.identityDomain)
                implementation(projects.identity.identityApi)
                implementation(projects.lookups.lookupsDomain)
                implementation(projects.home.homeApi)
                implementation(projects.notifications.notificationsApi)
                implementation(projects.shared.sharedDomain)
                implementation(libs.filekit.compose)
                implementation(libs.filekit.dialogs)
                implementation(libs.filekit.core)
                implementation(libs.qr.kit)
            }
        }
    }
}

dependencies {
    "androidRuntimeClasspath"(libs.compose.ui.tooling)
}