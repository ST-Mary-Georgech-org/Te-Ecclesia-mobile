package com.teEcclesia.designsystem.navigation

import kotlinx.coroutines.CoroutineDispatcher

interface DispatcherProvider {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
}

expect fun getDispatcherProvider(): DispatcherProvider
