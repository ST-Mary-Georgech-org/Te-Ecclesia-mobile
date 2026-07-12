import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeCompiler)
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

    buildFeatures {
        compose = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
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
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
    }
}

dependencies {
    implementation(projects.teEcclesiaApp)
    implementation(projects.identityDomain)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.compose.ui.tooling.preview)
    debugImplementation(libs.compose.ui.tooling)

    implementation(libs.koin.android)
    implementation(libs.koin.core)
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
