package com.teEcclesia.logging

import platform.Foundation.NSLog

object IosCrashLoggerBridge {
    var delegate: ((Throwable) -> Unit)? = null
    var customKeyDelegate: ((String, String) -> Unit)? = null
    var logDelegate: ((String) -> Unit)? = null
}

class IosCrashLogger : CrashLogger {
    override fun recordException(throwable: Throwable) {
        val delegate = IosCrashLoggerBridge.delegate
        if (delegate != null) {
            delegate(throwable)
        } else {
            NSLog("CrashLogger iOS: No delegate registered. Exception: ${throwable.message}")
        }
    }

    override fun setCustomKey(key: String, value: String) {
        IosCrashLoggerBridge.customKeyDelegate?.invoke(key, value)
    }

    override fun log(message: String) {
        IosCrashLoggerBridge.logDelegate?.invoke(message)
    }
}
