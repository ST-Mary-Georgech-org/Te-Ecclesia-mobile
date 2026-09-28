package com.teEcclesia.designsystem.utils

import com.teEcclesia.shared.domain.exception.InternetException
import com.teEcclesia.shared.domain.exception.InvalidRequestException
import com.teEcclesia.shared.domain.exception.NoNetworkException
import com.teEcclesia.shared.domain.exception.ServerErrorException
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.error_invalid_request
import teecclesia.designsystem.generated.resources.error_no_internet
import teecclesia.designsystem.generated.resources.server_error
import teecclesia.designsystem.generated.resources.unknown_error

fun getLocalizedErrorMessage(throwable: Throwable?): UiText {
    return when (throwable) {
        is NoNetworkException, is InternetException -> UiText.StringRes(Res.string.error_no_internet)
        is ServerErrorException -> UiText.StringRes(Res.string.server_error)
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
