package com.teEcclesia.designsystem.navigation

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import com.teEcclesia.designsystem.utils.UiText
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class SnackBarManager {
    private val _snackBarEvent = MutableSharedFlow<SnackBarEvent>(extraBufferCapacity = 1)
    val snackBarEvent = _snackBarEvent.asSharedFlow()

    fun showSnackBar(
        title: UiText,
        message: UiText? = null,
        isSuccess: Boolean = true,
        customLeadingIcon: Painter? = null,
        duration: Long? = null,
        iconTint: Color = Color.Unspecified
    ) {
        _snackBarEvent.tryEmit(
            SnackBarEvent(
                title = title,
                message = message,
                isSuccess = isSuccess,
                customLeadingIcon = customLeadingIcon,
                duration = duration,
                iconTint = iconTint
            )
        )
    }
}

data class SnackBarEvent(
    val title: UiText,
    val message: UiText? = null,
    val isSuccess: Boolean = true,
    val customLeadingIcon: Painter? = null,
    val duration: Long? = null,
    val iconTint: Color = Color.Unspecified
)
