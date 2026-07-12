package com.teEcclesia

actual object AppEnvironment {
    var internalBaseUrl: String = ""
    var internalVersionName: String = ""

    actual val baseUrl: String get() = internalBaseUrl
    actual val versionName: String get() = internalVersionName
}
