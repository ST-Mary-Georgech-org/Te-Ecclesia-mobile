package com.teEcclesia.identity.presentation.util

import com.teEcclesia.designsystem.utils.UiText
import com.teEcclesia.shared.domain.utils.validation.PasswordValidationError
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.password_min_length
import teecclesia.designsystem.generated.resources.password_need_uppercase
import teecclesia.designsystem.generated.resources.password_need_lowercase
import teecclesia.designsystem.generated.resources.password_need_digit
import teecclesia.designsystem.generated.resources.password_need_special_char

fun PasswordValidationError.toUiText(): UiText = when (this) {
    PasswordValidationError.TOO_SHORT -> UiText.StringRes(Res.string.password_min_length)
    PasswordValidationError.NO_UPPERCASE -> UiText.StringRes(Res.string.password_need_uppercase)
    PasswordValidationError.NO_LOWERCASE -> UiText.StringRes(Res.string.password_need_lowercase)
    PasswordValidationError.NO_DIGIT -> UiText.StringRes(Res.string.password_need_digit)
    PasswordValidationError.NO_SPECIAL_CHAR -> UiText.StringRes(Res.string.password_need_special_char)
}
