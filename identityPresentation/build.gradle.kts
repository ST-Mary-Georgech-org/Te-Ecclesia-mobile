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
                implementation(projects.identityDomain)
                implementation(projects.identityApi)
                implementation(projects.homeApi)
            }
        }
    }
}

dependencies {
    "androidRuntimeClasspath"(libs.compose.ui.tooling)
}