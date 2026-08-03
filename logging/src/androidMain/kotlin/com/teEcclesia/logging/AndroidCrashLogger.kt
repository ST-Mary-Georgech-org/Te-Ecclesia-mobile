package com.teEcclesia.logging

import com.google.firebase.crashlytics.FirebaseCrashlytics

class AndroidCrashLogger : CrashLogger {
    override fun recordException(throwable: Throwable) {
        FirebaseCrashlytics.getInstance().recordException(throwable)
    }
}
