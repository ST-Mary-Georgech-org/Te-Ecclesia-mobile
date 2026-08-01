package com.teEcclesia.identity.presentation.util

import com.teEcclesia.designsystem.utils.UiText
import com.teEcclesia.shared.domain.exception.EmailNotVerifiedException
import com.teEcclesia.shared.domain.exception.InternetException
import com.teEcclesia.shared.domain.exception.InvalidCredentialsException
import com.teEcclesia.shared.domain.exception.InvalidRequestException
import com.teEcclesia.shared.domain.exception.NoNetworkException
import com.teEcclesia.shared.domain.exception.PhoneNotVerifiedException
import com.teEcclesia.shared.domain.exception.TooManyRequestsException
import com.teEcclesia.shared.domain.exception.UnAuthorizedException
import com.teEcclesia.shared.domain.exception.UserIsBlockedException
import com.teEcclesia.shared.domain.exception.UsernameOrPhoneNumberAlreadyExistsException
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.error_account_already_exists
import teecclesia.designsystem.generated.resources.error_email_not_verified
import teecclesia.designsystem.generated.resources.error_invalid_credentials
import teecclesia.designsystem.generated.resources.error_invalid_request
import teecclesia.designsystem.generated.resources.error_no_internet
import teecclesia.designsystem.generated.resources.error_phone_not_verified
import teecclesia.designsystem.generated.resources.error_too_many_requests
import teecclesia.designsystem.generated.resources.error_unauthorized
import teecclesia.designsystem.generated.resources.error_user_not_verified
import com.teEcclesia.shared.domain.exception.DuplicatePhoneException
import com.teEcclesia.shared.domain.exception.ServerErrorException
import teecclesia.designsystem.generated.resources.error_duplicate_phone_forgot_password
import teecclesia.designsystem.generated.resources.server_error
import teecclesia.designsystem.generated.resources.unknown_error

fun getLocalizedErrorMessage(throwable: Throwable?): UiText {
    return when (throwable) {
        is DuplicatePhoneException -> UiText.StringRes(Res.string.error_duplicate_phone_forgot_password)
        is ServerErrorException -> UiText.StringRes(Res.string.server_error)
        is UsernameOrPhoneNumberAlreadyExistsException -> UiText.StringRes(Res.string.error_account_already_exists)
        is PhoneNotVerifiedException -> UiText.StringRes(Res.string.error_phone_not_verified)
        is EmailNotVerifiedException -> UiText.StringRes(Res.string.error_email_not_verified)
        is NoNetworkException, is InternetException -> UiText.StringRes(Res.string.error_no_internet)
        is UnAuthorizedException -> UiText.StringRes(Res.string.error_unauthorized)
        is InvalidCredentialsException -> UiText.StringRes(Res.string.error_invalid_credentials)
        is UserIsBlockedException -> UiText.StringRes(Res.string.error_user_not_verified)
        is TooManyRequestsException -> UiText.StringRes(Res.string.error_too_many_requests)
        is InvalidRequestException -> {
            val msg = throwable.message
            if (!msg.isNullOrBlank() && msg != "Invalid request") {
                UiText.DynamicString(msg)
            } else {
                UiText.StringRes(Res.string.error_invalid_request)
            }
        }
        else -> {
            val msg = throwable?.message
            if (!msg.isNullOrBlank() && msg != "Unknown error" && msg != "An error occurred") {
                UiText.DynamicString(msg)
            } else {
                UiText.StringRes(Res.string.unknown_error)
            }
        }
    }
}
