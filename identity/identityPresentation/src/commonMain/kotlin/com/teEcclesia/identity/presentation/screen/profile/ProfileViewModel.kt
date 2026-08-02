package com.teEcclesia.identity.presentation.screen.profile

import com.teEcclesia.designsystem.components.button.AppButtonState
import com.teEcclesia.designsystem.navigation.BaseViewModel
import com.teEcclesia.designsystem.utils.UiText
import com.teEcclesia.identity.api.AddUserRoute
import com.teEcclesia.identity.api.LoginRoute
import com.teEcclesia.identity.api.UsersSearchRoute
import com.teEcclesia.identity.domain.repository.AuthenticationRepository
import com.teEcclesia.identity.domain.repository.ProfileRepository
import com.teEcclesia.identity.domain.repository.SettingsRepository
import com.teEcclesia.identity.domain.service.AuthorizationService
import com.teEcclesia.identity.domain.util.AppLanguage
import com.teEcclesia.identity.domain.util.AppLocalizer
import com.teEcclesia.identity.domain.util.AppTheme
import com.teEcclesia.notifications.api.NotificationsRoute
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.not_implemented_yet

class ProfileViewModel(
    private val authenticationRepository: AuthenticationRepository,
    private val profileRepository: ProfileRepository,
    private val authorizationService: AuthorizationService,
    private val settingsRepository: SettingsRepository,
    private val appLocalizer: AppLocalizer
) : BaseViewModel<ProfileScreenState>(ProfileScreenState()) {

    init {
        observeCachedProfile()
        loadUserProfile()
    }

    private fun observeCachedProfile() {
        tryToCollect(
            block = { settingsRepository.observeCachedProfile() },
            onEach = { cached ->
                if (cached != null) {
                    updateState {
                        copy(
                            userRole = cached.role,
                            fullName = cached.fullName,
                            displayName = cached.displayName,
                            userCode = cached.code,
                            imageUrl = cached.imageUrl
                        )
                    }
                }
            },
            onError = { }
        )
    }

    private fun loadUserProfile() {
        val language = settingsRepository.getCurrentAppLanguage()
        val deviceLanguage = appLocalizer.getDeviceLanguageIso()
        val enOrAr = if (deviceLanguage.startsWith(AppLanguage.ARABIC.iso)) AppLanguage.ARABIC else AppLanguage.ENGLISH
        val activeLanguage = when (language) {
            AppLanguage.DEFAULT -> enOrAr
            else -> language
        }

        updateState {
            copy(currentLanguage = activeLanguage)
        }

        tryToCall(
            block = {
                val canSearch = authorizationService.canSearchUsers()
                val canAdd = authorizationService.canAddStudent()
                updateState {
                    copy(
                        canAddUser = canAdd,
                        canSearchUsers = canSearch
                    )
                }
                profileRepository.getRegistrationProfile()
            },
            onSuccess = { },
            onError = { }
        )
    }

    fun onClickNotifications() {
        navigate(NotificationsRoute)
    }

    fun onLanguageSelected(language: AppLanguage) {
        tryToCall(
            block = { settingsRepository.applyLanguage(language) },
            onSuccess = {
                updateState { copy(currentLanguage = language) }
            },
            onError = { }
        )
    }

    fun onThemeSelected(theme: AppTheme) {
        tryToCall(
            block = { settingsRepository.applyAppTheme(theme) },
            onSuccess = { },
            onError = { }
        )
    }

    fun onClickEditProfile() {
        showSnackBar(
            title = UiText.StringRes(Res.string.not_implemented_yet),
            isSuccess = true
        )
        // TODO: Implement edit profile navigation or action
    }

    fun onClickAddUser() {
        navigate(AddUserRoute)
    }

    fun onClickSearchUsers() {
        navigate(UsersSearchRoute)
    }

    fun onClickLogout() {
        tryToCall(
            onStart = { updateState { copy(actionButtonState = AppButtonState.Loading) } },
            block = { authenticationRepository.logout() },
            onSuccess = { },
            onError = { },
            onEnd = { updateState { copy(actionButtonState = AppButtonState.Enabled) } }
        )
        resetTo(LoginRoute)
    }
}
