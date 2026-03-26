import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.cocoapods)
    id("com.google.gms.google-services")
}

val localProperties = Properties()
val localPropertiesFile: File = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}

val appVersionName =
    project.property("VERSION_MAJOR").toString() + "." + project.property("VERSION_MINOR")
        .toString()

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "TeEcclesiaApp"
            isStatic = true
        }
    }

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        version = "1.0"
        ios.deploymentTarget = "16.2"
        podfile = project.file("iosApp/Podfile")

        framework {
            baseName = "TeEcclesiaApp"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.bundles.koin)

            implementation(projects.designSystem)
            implementation(projects.identityData)
            implementation(projects.identityDomain)
            implementation(projects.identityPresentation)
            implementation(projects.identityApi)
            implementation(projects.homeApi)
            implementation(projects.homePresentation)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.teEcclesia"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.teEcclesia"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = appVersionName

        val baseUrl = localProperties.getProperty("BASE_URL", "")
        buildConfigField("String", "BASE_URL", "\"${baseUrl}\"")
    }
    signingConfigs {
        if (project.hasProperty("KEYSTORE_STORE_FILE") || System.getenv("KEYSTORE_STORE_FILE") != null) {
            create("release") {
                val keystorePath = project.loadProperty(
                    path = "local.properties",
                    propertyName = "KEYSTORE_STORE_FILE",
                )

                storeFile = file(keystorePath)
                storePassword = project.loadProperty("local.properties", "KEYSTORE_STORE_PASSWORD")
                keyAlias = project.loadProperty("local.properties", "KEYSTORE_KEY_ALIAS")
                keyPassword = project.loadProperty("local.properties", "KEYSTORE_KEY_PASSWORD")
            }
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            if (signingConfigs.findByName("release") != null) {
                signingConfig = signingConfigs.getByName("release")
            }

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

fun Project.loadProperty(
    path: String,
    propertyName: String,
): String {
    val properties = Properties()
    val propertiesFile = project.rootProject.file(path)

    if (propertiesFile.exists()) {
        properties.load(propertiesFile.inputStream())
        return properties.getProperty(propertyName)
            ?: System.getenv(propertyName)
            ?: throw GradleException("Property '$propertyName' not found in $path or environment")
    } else {
        // Fallback to environment variable for CI/CD
        return System.getenv(propertyName)
            ?: throw GradleException("Property file '$path' not found and '$propertyName' not in environment")
    }
}