plugins {
    id("teecclesia.kmp.feature.data")
}

kotlin {
    android {
        namespace = "com.teEcclesia.notifications.data"
    }
    sourceSets {
        commonMain.dependencies {
            implementation(projects.notifications.notificationsDomain)
            implementation(projects.identity.identityDomain)
            api(projects.shared.sharedDomain)
            implementation(projects.shared.sharedData)
            implementation(projects.designSystem)
            implementation(libs.compose.resources)
            api(libs.kmpnotifier)
        }
    }
}
