package com.teEcclesia.logging

import platform.Foundation.NSLog

object IosCrashLoggerBridge {
    var delegate: ((Throwable) -> Unit)? = null
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
}
