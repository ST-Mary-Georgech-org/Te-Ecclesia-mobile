package com.teEcclesia.designsystem.utils

expect suspend fun compressImage(
    bytes: ByteArray,
    maxDimension: Int = 1920,
    quality: Int = 80
): ByteArray
