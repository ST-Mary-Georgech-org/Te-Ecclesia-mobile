package com.teEcclesia.identity.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object SplashRoute : NavKey

@Serializable
data object LoginRoute : NavKey

@Serializable
data class SignUpRoute(val isEditMode: Boolean = false) : NavKey

@Serializable
data class VerifyPhoneRoute(val phone: String, val isForgetPasswordFlow: Boolean) : NavKey

@Serializable
data object ProfileRoute : NavKey

@Serializable
data object PendingApprovalRoute : NavKey

@Serializable
data object RegistrationRequestsRoute : NavKey
