package com.teEcclesia.appEntryPoint

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavKey
import com.teEcclesia.designsystem.components.snackbar.SnackBarData
import com.teEcclesia.designsystem.navigation.BaseViewModel
import com.teEcclesia.designsystem.utils.asStringSuspend
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainEntryViewModel : BaseViewModel<MainEntryState>(MainEntryState()),
    MainEntryInteractionListener {

    init {
        viewModelScope.launch {
            snackBarManager.snackBarEvent.collectLatest { event ->
                val resolvedTitle = event.title.asStringSuspend()
                val resolvedMessage = event.message?.asStringSuspend()
                updateState {
                    it.copy(
                        isSnackBarVisible = true,
                        snackBarData = SnackBarData(
                            title = resolvedTitle,
                            message = resolvedMessage,
                            isSuccess = event.isSuccess,
                            customLeadingIcon = event.customLeadingIcon,
                            duration = event.duration,
                            iconTint = event.iconTint
                        )
                    )
                }
            }
        }
    }

    override fun onBottomNavigationChanged(isShowed: Boolean) {
        updateState { it.copy(showBottomNavigation = isShowed) }
    }

    override fun showSnackBar(
        title: String,
        message: String?,
        isSuccess: Boolean,
        customLeadingIcon: Painter?,
        duration: Long?,
        iconTint: Color
    ) {
        updateState {
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
        updateState { it.copy(isSnackBarVisible = false) }
    }

    override fun resetToRoute(route: NavKey, forceNavigate: Boolean) {
        resetTo(route, forceNavigate)
    }

    override fun navigateToRoute(route: NavKey, forceNavigate: Boolean) {
        navigate(route, forceNavigate)
    }
}
