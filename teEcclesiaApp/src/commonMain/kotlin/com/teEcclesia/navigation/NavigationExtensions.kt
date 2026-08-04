package com.teEcclesia.navigation

fun <T> MutableList<T>.replaceAll(elements: Collection<T>) {
    if (elements.isEmpty()) return
    val oldSize = size
    addAll(elements)
    subList(0, oldSize).clear()
}
