package com.teEcclesia.logging.di

import com.teEcclesia.logging.IosCrashLogger
import com.teEcclesia.logging.CrashLogger
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformLoggingModule: Module = module {
    single<CrashLogger> { IosCrashLogger() }
}
