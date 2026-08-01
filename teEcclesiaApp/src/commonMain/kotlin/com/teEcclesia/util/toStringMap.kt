package com.teEcclesia.util

fun Map<String, Any?>.toStringMap(): Map<String, String> {
    return this.mapNotNull { (k, v) -> v?.let { k to it.toString() } }.toMap()
}
