package com.teEcclesia.identity.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

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

@Serializable
data class ReviewAndEditRequestRoute(val userId: String) : NavKey

@Serializable
data class EditUserRoute(val userId: String) : NavKey

@Serializable
data object AddUserRoute : NavKey

@Serializable
data object UsersSearchRoute : NavKey

@Serializable
data class ForgotPasswordRoute(val key: String = "") : NavKey

@Serializable
data class VerifyPhoneResetPasswordRoute(val phone: String, val token: String, val link: String) : NavKey

@Serializable
data class VerifyEmailResetPasswordRoute(val email: String) : NavKey

@Serializable
data class CreateNewPasswordRoute(val key: String, val otp: String, val isPhone: Boolean) : NavKey

@Serializable
data object AttendanceServicesRoute : NavKey

@Serializable
data class AttendanceRegisterRoute(
    val eventId: Long,
    val serviceName: String,
    val eventName: String
) : NavKey

@Serializable
data class AttendanceEventsRoute(
    val serviceId: Long,
    val serviceName: String
) : NavKey