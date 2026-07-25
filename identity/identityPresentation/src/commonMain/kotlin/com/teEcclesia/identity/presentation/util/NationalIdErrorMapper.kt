package com.teEcclesia.identity.presentation.util

import com.teEcclesia.designsystem.utils.UiText
import com.teEcclesia.shared.domain.utils.validation.NationalIdValidationError
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.invalid_national_id_length
import teecclesia.designsystem.generated.resources.invalid_national_id_century
import teecclesia.designsystem.generated.resources.invalid_national_id_date
import teecclesia.designsystem.generated.resources.invalid_national_id_governorate

fun NationalIdValidationError.toUiText(): UiText = when (this) {
    NationalIdValidationError.INVALID_LENGTH -> UiText.StringRes(Res.string.invalid_national_id_length)
    NationalIdValidationError.INVALID_CENTURY -> UiText.StringRes(Res.string.invalid_national_id_century)
    NationalIdValidationError.INVALID_DATE -> UiText.StringRes(Res.string.invalid_national_id_date)
    NationalIdValidationError.INVALID_GOVERNORATE -> UiText.StringRes(Res.string.invalid_national_id_governorate)
}
