plugins {
    id("teecclesia.kmp.feature.domain")
}

kotlin {
    android {
        namespace = "com.teEcclesia.notifications.domain"
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.shared.sharedDomain)
            }
        }
    }
}
