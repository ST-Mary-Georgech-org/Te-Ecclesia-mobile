package com.teEcclesia.util

import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import com.teEcclesia.home.api.HomeRoute
import com.teEcclesia.identity.api.SplashRoute
import com.teEcclesia.identity.api.LoginRoute
import com.teEcclesia.identity.api.ProfileRoute
import com.teEcclesia.identity.api.RegistrationRequestsRoute
import com.teEcclesia.identity.api.SignUpRoute
import com.teEcclesia.identity.api.VerifyPhoneRoute
import com.teEcclesia.identity.api.AddUserRoute
import com.teEcclesia.identity.api.AttendanceEventsRoute
import com.teEcclesia.identity.api.AttendanceRegisterRoute
import com.teEcclesia.identity.api.AttendanceServicesRoute
import com.teEcclesia.identity.api.CreateNewPasswordRoute
import com.teEcclesia.identity.api.EditUserRoute
import com.teEcclesia.identity.api.ForgotPasswordRoute
import com.teEcclesia.identity.api.PendingApprovalRoute
import com.teEcclesia.identity.api.ReviewAndEditRequestRoute
import com.teEcclesia.identity.api.UsersSearchRoute
import com.teEcclesia.identity.api.VerifyEmailResetPasswordRoute
import com.teEcclesia.identity.api.VerifyPhoneResetPasswordRoute
import com.teEcclesia.notifications.api.NotificationsRoute
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

fun buildNavigationSerializerConfig(): SavedStateConfiguration = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(SplashRoute::class, SplashRoute.serializer())
            subclass(LoginRoute::class, LoginRoute.serializer())
            subclass(SignUpRoute::class, SignUpRoute.serializer())
            subclass(VerifyPhoneRoute::class, VerifyPhoneRoute.serializer())
            subclass(ProfileRoute::class, ProfileRoute.serializer())
            subclass(HomeRoute::class, HomeRoute.serializer())
            subclass(NotificationsRoute::class, NotificationsRoute.serializer())
            subclass(RegistrationRequestsRoute::class, RegistrationRequestsRoute.serializer())
            subclass(PendingApprovalRoute::class, PendingApprovalRoute.serializer())
            subclass(ReviewAndEditRequestRoute::class, ReviewAndEditRequestRoute.serializer())
            subclass(AddUserRoute::class, AddUserRoute.serializer())
            subclass(UsersSearchRoute::class, UsersSearchRoute.serializer())
            subclass(ForgotPasswordRoute::class, ForgotPasswordRoute.serializer())
            subclass(VerifyPhoneResetPasswordRoute::class, VerifyPhoneResetPasswordRoute.serializer())
            subclass(VerifyEmailResetPasswordRoute::class, VerifyEmailResetPasswordRoute.serializer())
            subclass(CreateNewPasswordRoute::class, CreateNewPasswordRoute.serializer())
            subclass(AttendanceEventsRoute::class, AttendanceEventsRoute.serializer())
            subclass(AttendanceServicesRoute::class, AttendanceServicesRoute.serializer())
            subclass(AttendanceRegisterRoute::class, AttendanceRegisterRoute.serializer())
            subclass(EditUserRoute::class, EditUserRoute.serializer())
        }
    }
}
