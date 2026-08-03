package com.teEcclesia.logging

interface CrashLogger {
    fun recordException(throwable: Throwable)
}
