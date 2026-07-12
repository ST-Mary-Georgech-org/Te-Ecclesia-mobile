import java.util.Properties

plugins {
    id("teecclesia.kmp.application")
}

kotlin {
    android {
        namespace = "com.teEcclesia.library"
    }

    swiftPMDependencies {
        swiftPackage(
            url = url("https://github.com/firebase/firebase-ios-sdk.git"),
            version = from("12.13.0"),
            products = listOf(product("FirebaseCore"))
        )
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.androidx.navigation3.ui)
                implementation(libs.androidx.lifecycle.viewmodel.navigation3)
                
                implementation(projects.designSystem)
                implementation(projects.identity.identityData)
                implementation(projects.identity.identityDomain)
                implementation(projects.identity.identityPresentation)
                implementation(projects.identity.identityApi)
                implementation(projects.home.homeApi)
                implementation(projects.home.homePresentation)
            }
        }
        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
        androidMain {
            dependencies {
                implementation(libs.androidx.poolingcontainer)
                implementation(libs.androidx.core.ktx)
            }
        }
    }
}

dependencies {
    "androidRuntimeClasspath"(libs.compose.ui.tooling)
}

tasks.register("syncIosConfig") {
    group = "ios"
    val localPropsFile = rootProject.layout.projectDirectory.file("local.properties")
    val configFile = layout.projectDirectory.file("../iosApp/Configuration/Config.xcconfig")
    inputs.file(localPropsFile).optional()
    outputs.file(configFile)

    doLast {
        val properties = Properties()
        val localProps = localPropsFile.asFile
        if (localProps.exists()) {
            localProps.inputStream().use {
                properties.load(it)
            }
        }

        val baseUrl = properties.getProperty("BASE_URL").orEmpty()
        val escapedUrl = baseUrl.replace("//", "/$()/")
        val configFileOnDisk = configFile.asFile

        if (configFileOnDisk.exists()) {
            val lines = configFileOnDisk.readLines().toMutableList()
            val index = lines.indexOfFirst { it.startsWith("BASE_URL") }
            val newLine = "BASE_URL = $escapedUrl"

            if (index != -1) {
                lines[index] = newLine
            } else {
                lines.add(newLine)
            }
            configFileOnDisk.writeText(lines.joinToString("\n"))
        }
    }
}

tasks.matching { it.name.contains("Framework") }.configureEach {
    dependsOn("syncIosConfig")
}