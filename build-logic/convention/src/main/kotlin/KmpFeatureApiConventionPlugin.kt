import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import com.teecclesia.convention.configureKotlinMultiplatform
import com.teecclesia.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KmpFeatureApiConventionPlugin : Plugin<Project> {
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

            extensions.configure<KotlinMultiplatformExtension> {
                targets.withType(KotlinMultiplatformAndroidLibraryTarget::class.java).configureEach {
                    compileSdk = libs.findVersion("android-compileSdk").get().requiredVersion.toInt()
                    minSdk = libs.findVersion("android-minSdk").get().requiredVersion.toInt()
                }

                sourceSets.configureEach {
                    if (name == "commonMain") {
                        dependencies {
                            implementation(libs.findLibrary("androidx-navigation3-ui").get())
                            implementation(libs.findLibrary("compose-runtime").get())
                            implementation(libs.findLibrary("compose-ui").get())
                        }
                    }
                }
            }
        }
    }
}
