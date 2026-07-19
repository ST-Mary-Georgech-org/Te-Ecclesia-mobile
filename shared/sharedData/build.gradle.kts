plugins {
    id("teecclesia.kmp.feature.data")
}

kotlin {
    android {
        namespace = "com.teEcclesia.shared.data"
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.shared.sharedDomain)
            }
        }
    }
}
