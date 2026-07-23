package com.teEcclesia

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
