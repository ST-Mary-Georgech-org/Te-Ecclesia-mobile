import org.jetbrains.kotlin.gradle.plugin.mpp.Framework
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import java.util.Properties

plugins {
    id("teecclesia.kmp.application")
}

kotlin {
    android {
        namespace = "com.teEcclesia.library"
    }

    targets.withType<KotlinNativeTarget> {
        binaries.withType<Framework> {
            export(projects.logging)
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.androidx.navigation3.ui)
                implementation(libs.androidx.lifecycle.viewmodel.navigation3)
                
                implementation(libs.coil.compose)
                implementation(libs.coil.network.ktor)

                implementation(projects.designSystem)
                implementation(projects.identity.identityData)
                implementation(projects.identity.identityDomain)
                implementation(projects.identity.identityPresentation)
                implementation(projects.identity.identityApi)
                implementation(projects.home.homeApi)
                implementation(projects.home.homePresentation)
                
                implementation(projects.lookups.lookupsData)
                implementation(projects.lookups.lookupsDomain)
                implementation(projects.notifications.notificationsData)
                implementation(projects.notifications.notificationsDomain)
                implementation(projects.notifications.notificationsApi)
                implementation(projects.notifications.notificationsPresentation)
                implementation(libs.kmpnotifier)
                api(projects.logging)
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