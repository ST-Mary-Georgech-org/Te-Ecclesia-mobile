package com.teEcclesia.shared.domain.model

class SafeByteArray(val bytes: ByteArray) {
    val size: Int
        get() = bytes.size

    fun isEmpty(): Boolean = bytes.isEmpty()

    fun isNotEmpty(): Boolean = bytes.isNotEmpty()

    operator fun get(index: Int): Byte = bytes[index]

    fun toByteArray(): ByteArray = bytes

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SafeByteArray) return false
        return bytes.contentEquals(other.bytes)
    }

    override fun hashCode(): Int = bytes.contentHashCode()

    override fun toString(): String = "SafeByteArray(size=$size)"
}

fun ByteArray.toSafeByteArray(): SafeByteArray = SafeByteArray(this)

fun SafeByteArray?.toByteArrayOrNull(): ByteArray? = this?.bytes
