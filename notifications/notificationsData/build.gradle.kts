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
            implementation(projects.shared.sharedDomain)
            implementation(projects.shared.sharedData)
            implementation(libs.kmpnotifier)
        }
    }
}
