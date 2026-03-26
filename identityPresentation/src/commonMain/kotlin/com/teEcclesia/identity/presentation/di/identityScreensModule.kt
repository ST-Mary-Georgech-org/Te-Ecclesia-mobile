package com.teEcclesia.identity.presentation.di

import com.teEcclesia.identity.presentation.navigation.effector.Effector
import com.teEcclesia.identity.presentation.navigation.effector.EffectorImpl
import com.teEcclesia.identity.presentation.screen.login.LoginViewModel
import com.teEcclesia.identity.presentation.screen.signup.SignUpViewModel
import com.teEcclesia.identity.presentation.screen.verifyPhone.VerifyPhoneViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val identityScreensModule = module {
    singleOf(::EffectorImpl) bind Effector::class
    viewModelOf(::SignUpViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::VerifyPhoneViewModel)
}