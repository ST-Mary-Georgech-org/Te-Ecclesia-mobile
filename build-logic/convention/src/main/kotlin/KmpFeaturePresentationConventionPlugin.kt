import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import com.teecclesia.convention.configureKotlinMultiplatform
import com.teecclesia.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.resources.ResourcesExtension
import org.jetbrains.compose.resources.ResourcesExtension.ResourceClassGeneration
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KmpFeaturePresentationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.findPlugin("kotlinMultiplatform").get().get().pluginId)
                apply(libs.findPlugin("androidKotlinMultiplatformLibrary").get().get().pluginId)
                apply(libs.findPlugin("composeMultiplatform").get().get().pluginId)
                apply(libs.findPlugin("composeCompiler").get().get().pluginId)
                apply(libs.findPlugin("kotlinx-serialization").get().get().pluginId)
            }

            configureKotlinMultiplatform()

            extensions.configure<ComposeExtension> {
                extensions.getByType<ResourcesExtension>().generateResClass = ResourceClassGeneration.Never
            }

            extensions.configure<KotlinMultiplatformExtension> {
                targets.withType(KotlinMultiplatformAndroidLibraryTarget::class.java).configureEach {
                    compileSdk = libs.findVersion("android-compileSdk").get().requiredVersion.toInt()
                    minSdk = libs.findVersion("android-minSdk").get().requiredVersion.toInt()

                    androidResources {
                        enable = true
                    }
                }

                sourceSets.configureEach {
                    if (name == "commonMain") {
                        dependencies {
                            implementation(libs.findLibrary("kotlin-stdlib").get())
                            implementation(libs.findLibrary("compose-runtime").get())
                            implementation(libs.findLibrary("compose-foundation").get())
                            implementation(libs.findLibrary("compose-ui").get())
                            compileOnly(libs.findLibrary("compose-material3").get())
                            implementation(libs.findLibrary("compose-resources").get())
                            implementation(libs.findLibrary("compose-preview").get())
                            implementation(libs.findLibrary("kotlinx-datetime").get())
                            implementation(libs.findLibrary("coil-compose").get())
                            implementation(libs.findBundle("koin").get())
                            implementation(libs.findLibrary("koin-compose-navigation3").get())
                            implementation(libs.findLibrary("androidx-navigation-compose").get())
                            implementation(libs.findLibrary("androidx-navigation3-ui").get())
                            implementation(libs.findLibrary("androidx-lifecycle-viewmodel-navigation3").get())
                        }
                    }
                    if (name == "androidMain") {
                        dependencies {
                            implementation(libs.findLibrary("compose-preview").get())
                            implementation(libs.findLibrary("androidx-activity-compose").get())
                        }
                    }
                }
            }
        }
    }
}
