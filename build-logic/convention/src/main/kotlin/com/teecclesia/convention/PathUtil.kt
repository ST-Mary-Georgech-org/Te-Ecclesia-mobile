package com.teecclesia.convention

import org.gradle.api.Project
import java.util.Locale

internal fun Project.pathToFrameworkName(): String {
    val parts = path.split(":", "-", "_", " ")
    return parts.joinToString("") { part ->
        if (part.isEmpty()) {
            ""
        } else {
            part.replaceFirstChar { it.titlecase(Locale.ROOT) }
        }
    }
}
