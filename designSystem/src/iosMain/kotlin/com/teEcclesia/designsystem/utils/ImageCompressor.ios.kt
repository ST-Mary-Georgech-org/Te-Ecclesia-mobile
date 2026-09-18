package com.teEcclesia.designsystem.utils

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.refTo
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.useContents
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import platform.CoreFoundation.CFDataCreateWithBytesNoCopy
import platform.CoreFoundation.kCFAllocatorDefault
import platform.CoreFoundation.kCFAllocatorNull
import platform.CoreGraphics.CGRectMake
import platform.CoreGraphics.CGSizeMake
import platform.Foundation.NSData
import platform.UIKit.UIGraphicsBeginImageContextWithOptions
import platform.UIKit.UIGraphicsEndImageContext
import platform.UIKit.UIGraphicsGetImageFromCurrentImageContext
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.posix.memcpy
import kotlin.math.max

@OptIn(ExperimentalForeignApi::class)
actual suspend fun compressImage(
    bytes: ByteArray,
    maxDimension: Int,
    quality: Int
): ByteArray = withContext(Dispatchers.Default) {
    if (bytes.isEmpty() || isPdf(bytes)) {
        return@withContext bytes
    }

    try {
        val nsData = bytes.toNSData() ?: return@withContext bytes
        val uiImage = UIImage.imageWithData(nsData) ?: return@withContext bytes

        val origWidth = uiImage.size.useContents { width }
        val origHeight = uiImage.size.useContents { height }

        val maxDim = max(origWidth, origHeight)
        val targetImage = if (maxDim > maxDimension) {
            val scale = maxDimension.toDouble() / maxDim
            val targetWidth = origWidth * scale
            val targetHeight = origHeight * scale
            val targetSize = CGSizeMake(targetWidth, targetHeight)

            UIGraphicsBeginImageContextWithOptions(targetSize, false, 1.0)
            uiImage.drawInRect(CGRectMake(0.0, 0.0, targetWidth, targetHeight))
            val resizedImage = UIGraphicsGetImageFromCurrentImageContext()
            UIGraphicsEndImageContext()
            resizedImage ?: uiImage
        } else {
            uiImage
        }

        val compressionQuality = (quality.coerceIn(0, 100) / 100.0)
        val compressedData = UIImageJPEGRepresentation(targetImage, compressionQuality)
            ?: return@withContext bytes

        compressedData.toByteArray()
    } catch (_: Throwable) {
        bytes
    }
}

private fun isPdf(bytes: ByteArray): Boolean {
    return bytes.size >= 4 &&
            bytes[0] == 0x25.toByte() &&
            bytes[1] == 0x50.toByte() &&
            bytes[2] == 0x44.toByte() &&
            bytes[3] == 0x46.toByte()
}

@OptIn(ExperimentalForeignApi::class)
private fun ByteArray.toNSData(): NSData? {
    if (isEmpty()) return null
    return usePinned { pinned ->
        val cfData = CFDataCreateWithBytesNoCopy(
            kCFAllocatorDefault,
            pinned.addressOf(0).reinterpret(),
            size.toLong(),
            kCFAllocatorNull
        )
        cfData?.let { platform.Foundation.CFBridgingRelease(it) as? NSData }
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun NSData.toByteArray(): ByteArray {
    val bytes = ByteArray(this.length.toInt())
    if (bytes.isNotEmpty()) {
        memcpy(bytes.refTo(0), this.bytes, this.length)
    }
    return bytes
}
