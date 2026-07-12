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
                implementation(projects.identity.identityDomain)
                implementation(projects.identity.identityApi)
                implementation(projects.home.homeApi)
            }
        }
    }
}

dependencies {
    "androidRuntimeClasspath"(libs.compose.ui.tooling)
}