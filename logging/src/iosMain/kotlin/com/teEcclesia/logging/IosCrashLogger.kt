package com.teEcclesia.logging

import kotlin.experimental.ExperimentalNativeApi
import platform.Foundation.NSLog

object IosCrashLoggerBridge {
    init {
        setupUnhandledHook()
    }

    var delegate: ((Throwable, String, String) -> Unit)? = null
    var customKeyDelegate: ((String, String) -> Unit)? = null
    var logDelegate: ((String) -> Unit)? = null

    @OptIn(ExperimentalNativeApi::class)
    fun setupUnhandledHook() {
        setUnhandledExceptionHook { throwable ->
            val message = throwable.message ?: "Unknown Kotlin Exception"
            val stackTrace = throwable.stackTraceToString()
            val del = delegate
            if (del != null) {
                del(throwable, message, stackTrace)
            } else {
                NSLog("CrashLogger iOS: Unhandled exception but no delegate: %s\n%s", message, stackTrace)
            }
        }
    }
}

@OptIn(ExperimentalNativeApi::class)
class IosCrashLogger : CrashLogger {

    override fun recordException(throwable: Throwable) {
        val message = throwable.message ?: "Unknown Kotlin Exception"
        val stackTrace = throwable.stackTraceToString()
        val delegate = IosCrashLoggerBridge.delegate
        if (delegate != null) {
            delegate(throwable, message, stackTrace)
        } else {
            NSLog("CrashLogger iOS: No delegate registered. Exception: $message")
        }
    }

    override fun setCustomKey(key: String, value: String) {
        IosCrashLoggerBridge.customKeyDelegate?.invoke(key, value)
    }

    override fun log(message: String) {
        IosCrashLoggerBridge.logDelegate?.invoke(message)
    }
}
