plugins {
    id("teecclesia.kmp.feature.data")
}

kotlin {
    android {
        namespace = "com.teEcclesia.lookups.data"
    }
    sourceSets {
        commonMain.dependencies {
            api(projects.lookups.lookupsDomain)
            implementation(projects.shared.sharedData)
            implementation(projects.shared.sharedDomain)
        }
    }
}
