package com.teEcclesia.appEntryPoint

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.lifecycle.ViewModel
import com.teEcclesia.designsystem.components.snackbar.SnackBarData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainEntryViewModel : ViewModel(), MainEntryInteractionListener {
    private val _state = MutableStateFlow(MainEntryState())
    val state = _state.asStateFlow()

    override fun onBottomNavigationChanged(isShowed: Boolean) {
        _state.update { it.copy(showBottomNavigation = isShowed) }
    }

    override fun setActiveFeature(feature: Feature) {
        _state.update { it.copy(activeFeature = feature) }
    }

    override fun showSnackBar(
        title: String,
        message: String?,
        isSuccess: Boolean,
        customLeadingIcon: Painter?,
        duration: Long?,
        iconTint: Color
    ) {
        _state.update {
            it.copy(
                isSnackBarVisible = true,
                snackBarData = SnackBarData(
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

    override fun hideSnackBar() {
        _state.update { it.copy(isSnackBarVisible = false) }
    }
}