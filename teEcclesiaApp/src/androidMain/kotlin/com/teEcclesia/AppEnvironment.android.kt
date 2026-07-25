package com.teEcclesia

actual object AppEnvironment {
    var internalBaseUrl: String = ""
    var internalVersionName: String = ""
    var internalIsDebug: Boolean = false

    actual val baseUrl: String get() = internalBaseUrl
    actual val versionName: String get() = internalVersionName
    actual val isDebug: Boolean get() = internalIsDebug
}
