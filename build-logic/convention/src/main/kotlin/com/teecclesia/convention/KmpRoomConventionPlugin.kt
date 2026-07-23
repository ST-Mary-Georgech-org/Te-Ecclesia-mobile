package com.teecclesia.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KmpRoomConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.google.devtools.ksp")
            pluginManager.apply("androidx.room")

            extensions.configure(androidx.room.gradle.RoomExtension::class.java) {
                schemaDirectory("$projectDir/schemas")
                generateKotlin = true
            }

            extensions.configure(KotlinMultiplatformExtension::class.java) {
                sourceSets.configureEach {
                    if (name == "commonMain") {
                        dependencies {
                            implementation(libs.findLibrary("androidx-room-runtime").get())
                            implementation(libs.findLibrary("androidx-sqlite-bundled").get())
                        }
                    }
                }
            }

            dependencies {
                add("kspAndroid", libs.findLibrary("androidx-room-compiler").get())
                add("kspIosArm64", libs.findLibrary("androidx-room-compiler").get())
                add("kspIosSimulatorArm64", libs.findLibrary("androidx-room-compiler").get())
            }
        }
    }
}
