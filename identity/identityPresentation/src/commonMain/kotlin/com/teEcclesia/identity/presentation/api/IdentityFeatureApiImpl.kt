package com.teEcclesia.identity.presentation.api

import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import com.teEcclesia.identity.api.AddUserRoute
import com.teEcclesia.identity.api.AttendanceEventsRoute
import com.teEcclesia.identity.api.AttendanceRegisterRoute
import com.teEcclesia.identity.api.AttendanceServicesRoute
import com.teEcclesia.identity.api.CreateNewPasswordRoute
import com.teEcclesia.identity.api.ForgotPasswordRoute
import com.teEcclesia.identity.api.IdentityFeatureApi
import com.teEcclesia.identity.api.LoginRoute
import com.teEcclesia.identity.api.PendingApprovalRoute
import com.teEcclesia.identity.api.ProfileRoute
import com.teEcclesia.identity.api.RegistrationRequestsRoute
import com.teEcclesia.identity.api.ReviewAndEditRequestRoute
import com.teEcclesia.identity.api.EditUserRoute
import com.teEcclesia.identity.api.SignUpRoute
import com.teEcclesia.identity.api.SplashRoute
import com.teEcclesia.identity.api.UsersSearchRoute
import com.teEcclesia.identity.api.VerifyEmailResetPasswordRoute
import com.teEcclesia.identity.api.VerifyPhoneResetPasswordRoute
import com.teEcclesia.identity.presentation.screen.attendance.events.EventsListScreen
import com.teEcclesia.identity.presentation.screen.attendance.register.AttendanceRegisterScreen
import com.teEcclesia.identity.presentation.screen.attendance.services.ServicesListScreen
import com.teEcclesia.identity.presentation.screen.login.LoginScreen
import com.teEcclesia.identity.presentation.screen.pendingApproval.PendingApprovalScreen
import com.teEcclesia.identity.presentation.screen.profile.ProfileScreen
import com.teEcclesia.identity.presentation.screen.register.RegisterScreen
import com.teEcclesia.identity.presentation.screen.requests.RegistrationRequestsScreen
import com.teEcclesia.identity.presentation.screen.resetPassword.createNewPassword.CreateNewPasswordScreen
import com.teEcclesia.identity.presentation.screen.resetPassword.forgotPassword.ForgotPasswordScreen
import com.teEcclesia.identity.presentation.screen.resetPassword.verifyEmail.VerifyEmailResetPasswordScreen
import com.teEcclesia.identity.presentation.screen.resetPassword.verifyPhone.VerifyPhoneResetPasswordScreen
import com.teEcclesia.identity.presentation.screen.reviewRequest.ReviewAndEditRequestScreen
import com.teEcclesia.identity.presentation.screen.splash.SplashScreen
import com.teEcclesia.identity.presentation.screen.usersSearch.UsersSearchScreen

class IdentityFeatureApiImpl : IdentityFeatureApi {

    override fun invoke(): (NavKey) -> NavEntry<NavKey> {
        return entryProvider {
            entry<SplashRoute> { SplashScreen() }
            entry<LoginRoute> { LoginScreen() }
            entry<SignUpRoute> { route -> RegisterScreen(isEditMode = route.isEditMode) }
            entry<PendingApprovalRoute> { PendingApprovalScreen() }
            entry<ProfileRoute> { ProfileScreen() }
            entry<RegistrationRequestsRoute> { RegistrationRequestsScreen() }
            entry<ReviewAndEditRequestRoute> { route -> ReviewAndEditRequestScreen(userId = route.userId, isFromSearch = false) }
            entry<EditUserRoute> { route -> ReviewAndEditRequestScreen(userId = route.userId, isFromSearch = true) }
            entry<AddUserRoute> { ReviewAndEditRequestScreen(userId = null, isFromSearch = false) }
            entry<UsersSearchRoute> { UsersSearchScreen() }
            entry<ForgotPasswordRoute> { route -> ForgotPasswordScreen(initialKey = route.key) }
            entry<VerifyPhoneResetPasswordRoute> { route ->
                VerifyPhoneResetPasswordScreen(
                    phone = route.phone,
                    token = route.token,
                    link = route.link
                )
            }
            entry<VerifyEmailResetPasswordRoute> { route ->
                VerifyEmailResetPasswordScreen(email = route.email)
            }
            entry<CreateNewPasswordRoute> { route ->
                CreateNewPasswordScreen(
                    key = route.key,
                    otp = route.otp,
                    isPhone = route.isPhone
                )
            }
            entry<AttendanceServicesRoute> {
                ServicesListScreen()
            }
            entry<AttendanceEventsRoute> { route ->
                EventsListScreen(
                    serviceId = route.serviceId,
                    serviceName = route.serviceName
                )
            }
            entry<AttendanceRegisterRoute> { route ->
                AttendanceRegisterScreen(
                    eventId = route.eventId,
                    serviceName = route.serviceName,
                    eventName = route.eventName
                )
            }
        }
    }
}
