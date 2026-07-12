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
                implementation(projects.identityDomain)
            }
        }
    }
}