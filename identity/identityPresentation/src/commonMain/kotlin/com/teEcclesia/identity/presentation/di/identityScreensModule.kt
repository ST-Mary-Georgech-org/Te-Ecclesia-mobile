package com.teEcclesia.identity.presentation.di

import com.teEcclesia.identity.presentation.screen.attendance.events.EventsListViewModel
import com.teEcclesia.identity.presentation.screen.attendance.register.AttendanceRegisterViewModel
import com.teEcclesia.identity.presentation.screen.attendance.services.ServicesListViewModel
import com.teEcclesia.identity.presentation.screen.login.LoginViewModel
import com.teEcclesia.identity.presentation.screen.pendingApproval.PendingApprovalViewModel
import com.teEcclesia.identity.presentation.screen.profile.ProfileViewModel
import com.teEcclesia.identity.presentation.screen.register.RegisterViewModel
import com.teEcclesia.identity.presentation.screen.requests.RegistrationRequestsViewModel
import com.teEcclesia.identity.presentation.screen.resetPassword.createNewPassword.CreateNewPasswordViewModel
import com.teEcclesia.identity.presentation.screen.resetPassword.forgotPassword.ForgotPasswordViewModel
import com.teEcclesia.identity.presentation.screen.resetPassword.verifyEmail.VerifyEmailResetPasswordViewModel
import com.teEcclesia.identity.presentation.screen.resetPassword.verifyPhone.VerifyPhoneResetPasswordViewModel
import com.teEcclesia.identity.presentation.screen.reviewRequest.ReviewAndEditRequestViewModel
import com.teEcclesia.identity.presentation.screen.usersSearch.UsersSearchViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

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
    viewModelOf(::RegistrationRequestsViewModel)
    viewModelOf(::UsersSearchViewModel)
    viewModelOf(::ServicesListViewModel)
    viewModel { parameters ->
        EventsListViewModel(
            serviceId = parameters.get(),
            serviceName = parameters.get(),
            attendanceRepository = get()
        )
    }
    viewModel { parameters ->
        AttendanceRegisterViewModel(
            eventId = parameters.get(),
            serviceName = parameters.get(),
            eventName = parameters.get(),
            attendanceRepository = get()
        )
    }
    viewModel { parameters ->
        ReviewAndEditRequestViewModel(
            userId = parameters.getOrNull<String>(),
            isFromSearch = parameters.getOrNull<Boolean>() ?: false,
            profileRepository = get(),
            registerRepository = get(),
            lookupRepository = get(),
            authorizationService = get()
        )
    }
    viewModel { parameters ->
        ForgotPasswordViewModel(
            initialKey = parameters.getOrNull<String>() ?: "",
            resetPasswordRepository = get()
        )
    }
    viewModel { parameters ->
        VerifyPhoneResetPasswordViewModel(
            phone = parameters.get(),
            token = parameters.get(),
            link = parameters.get(),
            resetPasswordRepository = get()
        )
    }
    viewModel { parameters ->
        VerifyEmailResetPasswordViewModel(
            email = parameters.get(),
            resetPasswordRepository = get()
        )
    }
    viewModel { parameters ->
        CreateNewPasswordViewModel(
            key = parameters.get(),
            otp = parameters.get(),
            isPhone = parameters.get(),
            resetPasswordRepository = get()
        )
    }
}
