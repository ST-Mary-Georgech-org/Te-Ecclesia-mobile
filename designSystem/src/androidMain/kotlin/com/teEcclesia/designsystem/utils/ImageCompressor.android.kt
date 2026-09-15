package com.teEcclesia.designsystem.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlin.math.max
import kotlin.math.roundToInt

actual suspend fun compressImage(
    bytes: ByteArray,
    maxDimension: Int,
    quality: Int
): ByteArray = withContext(Dispatchers.Default) {
    if (bytes.isEmpty() || isPdf(bytes)) {
        return@withContext bytes
    }

    try {
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        BitmapFactory.decodeByteArray(bytes, 0, bytes.size, options)

        val origWidth = options.outWidth
        val origHeight = options.outHeight

        if (origWidth <= 0 || origHeight <= 0) {
            return@withContext bytes
        }

        var sampleSize = 1
        val maxOriginalDim = max(origWidth, origHeight)
        if (maxOriginalDim > maxDimension) {
            sampleSize = (maxOriginalDim.toFloat() / maxDimension).roundToInt().coerceAtLeast(1)
        }

        val decodeOptions = BitmapFactory.Options().apply {
            inSampleSize = sampleSize
        }
        val decodedBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size, decodeOptions)
            ?: return@withContext bytes

        val orientation = try {
            val exif = ExifInterface(ByteArrayInputStream(bytes))
            exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        } catch (_: Exception) {
            ExifInterface.ORIENTATION_NORMAL
        }

        val matrix = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.postScale(-1f, 1f)
            ExifInterface.ORIENTATION_FLIP_VERTICAL -> matrix.postScale(1f, -1f)
        }

        val curWidth = decodedBitmap.width
        val curHeight = decodedBitmap.height
        val curMaxDim = max(curWidth, curHeight)
        if (curMaxDim > maxDimension) {
            val scale = maxDimension.toFloat() / curMaxDim
            matrix.postScale(scale, scale)
        }

        val finalBitmap = if (!matrix.isIdentity) {
            val transformed = Bitmap.createBitmap(
                decodedBitmap,
                0,
                0,
                curWidth,
                curHeight,
                matrix,
                true
            )
            if (transformed != decodedBitmap) {
                decodedBitmap.recycle()
            }
            transformed
        } else {
            decodedBitmap
        }

        val outputStream = ByteArrayOutputStream()
        finalBitmap.compress(Bitmap.CompressFormat.JPEG, quality.coerceIn(0, 100), outputStream)
        finalBitmap.recycle()

        val compressedBytes = outputStream.toByteArray()
        if (compressedBytes.isNotEmpty()) compressedBytes else bytes
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
