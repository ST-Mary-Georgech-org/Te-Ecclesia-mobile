package com.teEcclesia.designsystem.components.pdf

data class PdfPage(
    val pngData: ByteArray,
    val width: Int,
    val height: Int
) {
    val aspectRatio: Float get() = if (height > 0) width.toFloat() / height.toFloat() else 1f

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as PdfPage

        if (!pngData.contentEquals(other.pngData)) return false
        if (width != other.width) return false
        if (height != other.height) return false

        return true
    }

    override fun hashCode(): Int {
        var result = pngData.contentHashCode()
        result = 31 * result + width
        result = 31 * result + height
        return result
    }
}
