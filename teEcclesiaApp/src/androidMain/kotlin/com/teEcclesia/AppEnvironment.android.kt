package com.teEcclesia

actual object AppEnvironment {
    var internalBaseUrl: String = ""
    var internalVersionName: String = ""
    var internalIsDebug: Boolean = false
    var internalIsKidsVersion: Boolean = false
    var internalIsCameraSupported: Boolean = true

    actual val baseUrl: String get() = internalBaseUrl
    actual val versionName: String get() = internalVersionName
    actual val isDebug: Boolean get() = internalIsDebug
    actual val isKidsVersion: Boolean get() = internalIsKidsVersion
    actual val isCameraSupported: Boolean get() = internalIsCameraSupported
}
