package com.teEcclesia.identity.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object LoginRoute : NavKey

@Serializable
data object SignUpRoute : NavKey

@Serializable
data class VerifyPhoneRoute(val phone: String, val isForgetPasswordFlow: Boolean) : NavKey

@Serializable
data object ProfileRoute : NavKey
