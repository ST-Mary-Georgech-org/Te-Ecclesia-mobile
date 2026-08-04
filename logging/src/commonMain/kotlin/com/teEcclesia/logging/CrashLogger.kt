package com.teEcclesia.logging

interface CrashLogger {
    fun recordException(throwable: Throwable)
    fun setCustomKey(key: String, value: String)
    fun log(message: String)
}
