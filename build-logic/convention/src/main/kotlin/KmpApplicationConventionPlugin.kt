import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import com.teecclesia.convention.configureKotlinMultiplatform
import com.teecclesia.convention.configureIosAppTargets
import com.teecclesia.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KmpApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.findPlugin("kotlinMultiplatform").get().get().pluginId)
                apply(libs.findPlugin("androidKotlinMultiplatformLibrary").get().get().pluginId)
                apply(libs.findPlugin("composeMultiplatform").get().get().pluginId)
                apply(libs.findPlugin("composeCompiler").get().get().pluginId)
            }

            configureIosAppTargets(
                xcFrameworkName = "TeEcclesiaApp",
                frameworkBaseName = "TeEcclesiaApp",
                isStaticFramework = true
            )
            configureKotlinMultiplatform(includeIosTargets = false)

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
                            implementation(libs.findLibrary("compose-runtime").get())
                            implementation(libs.findLibrary("compose-foundation").get())
                            implementation(libs.findLibrary("compose-material3").get())
                            implementation(libs.findLibrary("compose-ui").get())
                            implementation(libs.findLibrary("compose-resources").get())
                            implementation(libs.findLibrary("compose-preview").get())
                            implementation(libs.findLibrary("androidx-lifecycle-viewmodelCompose").get())
                            implementation(libs.findLibrary("androidx-lifecycle-runtimeCompose").get())
                            implementation(libs.findBundle("koin").get())
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
