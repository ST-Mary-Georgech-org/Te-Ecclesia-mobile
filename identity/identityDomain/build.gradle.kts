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
                implementation(projects.shared.sharedDomain)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.androidx.core.ktx)
            }
        }
    }
}