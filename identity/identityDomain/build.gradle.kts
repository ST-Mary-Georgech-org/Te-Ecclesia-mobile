plugins {
    id("teecclesia.kmp.feature.domain")
}

kotlin {
    android {
        namespace = "com.teEcclesia.identity.domain"
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.lookups.lookupsDomain)
            }
        }
    }
}