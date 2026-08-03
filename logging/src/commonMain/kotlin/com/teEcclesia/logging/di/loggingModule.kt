package com.teEcclesia.logging.di

import org.koin.core.module.Module
import org.koin.dsl.module

expect val platformLoggingModule: Module

val loggingModule = module {
    includes(platformLoggingModule)
}
