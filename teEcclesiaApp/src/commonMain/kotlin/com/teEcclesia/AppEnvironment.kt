package com.teEcclesia

expect object AppEnvironment {
    val baseUrl: String
    val versionName: String
    val isDebug: Boolean
    val isKidsVersion: Boolean
    val isCameraSupported: Boolean
}

