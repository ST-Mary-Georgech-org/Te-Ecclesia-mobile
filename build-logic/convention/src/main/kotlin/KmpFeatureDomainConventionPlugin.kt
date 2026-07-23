import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import com.teecclesia.convention.configureKotlinMultiplatform
import com.teecclesia.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KmpFeatureDomainConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.findPlugin("kotlinMultiplatform").get().get().pluginId)
                apply(libs.findPlugin("androidKotlinMultiplatformLibrary").get().get().pluginId)
                apply(libs.findPlugin("androidLint").get().get().pluginId)
            }

            configureKotlinMultiplatform()

            extensions.configure<KotlinMultiplatformExtension> {
                targets.withType(KotlinMultiplatformAndroidLibraryTarget::class.java).configureEach {
                    compileSdk = libs.findVersion("android-compileSdk").get().requiredVersion.toInt()
                    minSdk = libs.findVersion("android-minSdk").get().requiredVersion.toInt()

                    withDeviceTestBuilder {
                        sourceSetTreeName = "test"
                    }.configure {
                        instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                    }
                }

                sourceSets.configureEach {
                    if (name == "commonMain") {
                        dependencies {
                            implementation(libs.findLibrary("kotlin-stdlib").get())
                            implementation(libs.findLibrary("koin-core").get())
                            implementation(libs.findLibrary("kotlinx-coroutines-core").get())
                            implementation(libs.findLibrary("kotlinx-datetime").get())
                        }
                    }
                }
            }
        }
    }
}
