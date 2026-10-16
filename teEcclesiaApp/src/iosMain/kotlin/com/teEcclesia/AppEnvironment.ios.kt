package com.teEcclesia

import platform.Foundation.NSBundle
import kotlin.experimental.ExperimentalNativeApi
import kotlin.native.Platform

actual object AppEnvironment {
    actual val baseUrl: String =
        NSBundle.mainBundle.objectForInfoDictionaryKey("BASE_URL") as? String
            ?: throw Exception("BASE_URL not found")
    actual val versionName: String =
        NSBundle.mainBundle.objectForInfoDictionaryKey("CFBundleShortVersionString") as? String
            ?: throw Exception("App version not found")

    @OptIn(ExperimentalNativeApi::class)
    actual val isDebug: Boolean = Platform.isDebugBinary

    actual val isKidsVersion: Boolean =
        (NSBundle.mainBundle.objectForInfoDictionaryKey("IS_KIDS_VERSION") as? String)?.toBoolean()
            ?: ((NSBundle.mainBundle.objectForInfoDictionaryKey("CFBundleIdentifier") as? String)?.contains(".kids") == true)

    actual val isCameraSupported: Boolean =
        !isKidsVersion && (NSBundle.mainBundle.objectForInfoDictionaryKey("NSCameraUsageDescription") != null)
}
