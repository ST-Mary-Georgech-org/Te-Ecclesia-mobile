import groovy.json.JsonOutput
import org.gradle.api.artifacts.ProjectDependency

plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.androidKotlinMultiplatformLibrary) apply false
    alias(libs.plugins.androidLint) apply false
    alias(libs.plugins.kotlinx.serialization) apply false
    id("com.google.gms.google-services") version "4.4.4" apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.room) apply false
}

abstract class ExportModuleDepsTask : DefaultTask() {
    @get:Input
    abstract val moduleJson: Property<String>

    @TaskAction
    fun run() {
        println(moduleJson.get())
    }
}

tasks.register<ExportModuleDepsTask>("exportModuleDeps") {
    val allModulePaths = rootProject.subprojects
        .filter { it.buildFile.exists() }
        .map { it.path }
        .toSet()

    val directDependencies = mutableMapOf<String, MutableSet<String>>()

    rootProject.subprojects
        .filter { it.buildFile.exists() }
        .forEach { project ->
            val projectDeps = mutableSetOf<String>()
            project.configurations
                .matching {
                    it.name.contains("implementation", ignoreCase = true) ||
                    it.name.contains("api", ignoreCase = true)
                }
                .forEach { configuration ->
                    configuration.dependencies.forEach { dependency ->
                        if (dependency is ProjectDependency && allModulePaths.contains(dependency.path)) {
                            projectDeps.add(dependency.path)
                        }
                    }
                }
            directDependencies[project.path] = projectDeps
        }

    val reverseDependencies = mutableMapOf<String, MutableSet<String>>()
    directDependencies.keys.forEach { module -> reverseDependencies[module] = mutableSetOf() }
    directDependencies.forEach { (module, deps) ->
        deps.forEach { dep -> reverseDependencies[dep]?.add(module) }
    }

    fun collectAllDependents(
        module: String,
        visited: MutableSet<String> = mutableSetOf()
    ): Set<String> {
        if (!visited.add(module)) return emptySet()
        val directDependents = reverseDependencies[module] ?: emptySet()
        return directDependents + directDependents.flatMap { collectAllDependents(it, visited) }
    }

    val modulesWithDependents =
        directDependencies.keys.associateWith { collectAllDependents(it) }

    val rootDirPath = rootProject.projectDir.toPath()
    val moduleInfo = rootProject.subprojects
        .filter { it.buildFile.exists() }
        .associate { project ->
            val hasAndroid = project.pluginManager.hasPlugin("com.android.application") ||
                    project.pluginManager.hasPlugin("com.android.library") ||
                    project.pluginManager.hasPlugin("com.android.kotlin.multiplatform.library")

            val hasIos = runCatching {
                val kotlinExt = project.extensions.findByName("kotlin")
                if (kotlinExt != null) {
                    val targets = kotlinExt.javaClass.getMethod("getTargets").invoke(kotlinExt) as? Iterable<*>
                    targets?.any { target ->
                        val name = target?.javaClass?.getMethod("getName")?.invoke(target)?.toString()?.lowercase().orEmpty()
                        name.contains("ios") || name.contains("apple")
                    } == true
                } else false
            }.getOrDefault(false)

            val relDir = rootDirPath.relativize(project.projectDir.toPath()).toString().replace('\\', '/')

            project.path to mapOf(
                "dir" to relDir,
                "hasAndroid" to hasAndroid,
                "hasIos" to hasIos,
                "dependents" to (modulesWithDependents[project.path] ?: emptySet<String>())
            )
        }

    val output = mapOf("modules" to moduleInfo)
    moduleJson.set(JsonOutput.toJson(output))
}