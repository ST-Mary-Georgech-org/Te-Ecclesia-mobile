plugins {
    id("teecclesia.kmp.feature.data")
}

kotlin {
    android {
        namespace = "com.teEcclesia.identity.data"
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.identity.identityDomain)
                implementation(projects.shared.sharedData)
                implementation(projects.shared.sharedDomain)
                implementation(projects.lookups.lookupsData)
                implementation(libs.kmpnotifier)
            }
        }
    }
}