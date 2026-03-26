package com.teEcclesia.identity.presentation.navigation

import kotlinx.serialization.Serializable

interface BaseRoute

@Serializable
data object HomeRoute : BaseRoute

@Serializable
data object LoginRoute : BaseRoute

@Serializable
data object SignUpRoute : BaseRoute


@Serializable
data class VerifyPhoneRoute(val phone: String, val isForgetPasswordFlow: Boolean) : BaseRoute

@Serializable
data object ProfileRoute : BaseRoute
