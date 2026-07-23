package com.teEcclesia.identity.presentation.di

import com.teEcclesia.identity.presentation.screen.login.LoginViewModel
import com.teEcclesia.identity.presentation.screen.pendingApproval.PendingApprovalViewModel
import com.teEcclesia.identity.presentation.screen.register.RegisterViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

import com.teEcclesia.identity.presentation.screen.profile.ProfileViewModel

val identityScreensModule = module {
    viewModelOf(::LoginViewModel)
    viewModel { parameters ->
        RegisterViewModel(
            isEditMode = parameters.get<Boolean>(),
            registerRepository = get(),
            lookupRepository = get(),
            authenticationRepository = get(),
            authorizationService = get(),
            profileRepository = get()
        )
    }
    viewModelOf(::PendingApprovalViewModel)
    viewModelOf(::ProfileViewModel)
}
