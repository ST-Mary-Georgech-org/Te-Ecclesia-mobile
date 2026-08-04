package com.teEcclesia.logging

import com.google.firebase.crashlytics.FirebaseCrashlytics

class AndroidCrashLogger : CrashLogger {
    override fun recordException(throwable: Throwable) {
        FirebaseCrashlytics.getInstance().recordException(throwable)
    }

    override fun setCustomKey(key: String, value: String) {
        FirebaseCrashlytics.getInstance().setCustomKey(key, value)
    }

    override fun log(message: String) {
        FirebaseCrashlytics.getInstance().log(message)
    }
}
