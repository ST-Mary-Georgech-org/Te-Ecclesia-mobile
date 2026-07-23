package com.teEcclesia.di.utils

import com.teEcclesia.appEntryPoint.MainEntryViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class DIHelper : KoinComponent {
    fun getMainEntryViewModel(): MainEntryViewModel = this.get()
}
