package com.teEcclesia.identity.data.utils

fun getContentTypeAndFilename(bytes: ByteArray, defaultBase: String): Pair<String, String> {
    val isPdf = bytes.size >= 4 &&
            bytes[0] == 0x25.toByte() &&
            bytes[1] == 0x50.toByte() &&
            bytes[2] == 0x44.toByte() &&
            bytes[3] == 0x46.toByte()
    return if (isPdf) {
        "application/pdf" to "$defaultBase.pdf"
    } else {
        "image/jpeg" to "$defaultBase.jpg"
    }
}
