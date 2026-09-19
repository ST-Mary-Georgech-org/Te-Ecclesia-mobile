package com.teEcclesia.identity.presentation.screen.profile

import com.teEcclesia.designsystem.components.button.AppButtonState
import com.teEcclesia.designsystem.navigation.BaseViewModel
import com.teEcclesia.designsystem.utils.UiText
import com.teEcclesia.designsystem.utils.getLocalizedErrorMessage
import com.teEcclesia.identity.api.AcademicYearSettingsRoute
import com.teEcclesia.identity.api.AddUserRoute
import com.teEcclesia.identity.api.EditSuggestionsRoute
import com.teEcclesia.identity.api.DeletionRequestsRoute
import com.teEcclesia.identity.api.LoginRoute
import com.teEcclesia.identity.api.UsersSearchRoute
import com.teEcclesia.identity.domain.repository.AuthenticationRepository
import com.teEcclesia.identity.domain.repository.ProfileRepository
import com.teEcclesia.identity.domain.repository.SettingsRepository
import com.teEcclesia.identity.domain.service.AuthorizationService
import com.teEcclesia.identity.domain.util.AppLanguage
import com.teEcclesia.identity.domain.util.AppLocalizer
import com.teEcclesia.identity.domain.util.AppTheme
import com.teEcclesia.identity.presentation.screen.academicYear.AcademicYearSettingsViewModel.Companion.KEY_UPDATED_ACADEMIC_YEAR
import com.teEcclesia.notifications.api.NotificationsRoute
import com.teEcclesia.notifications.api.SendNotificationRoute
import com.teEcclesia.shared.domain.model.UserRole
import com.teEcclesia.shared.domain.push.NotificationPermissionHandler
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.account_deleted_successfully
import teecclesia.designsystem.generated.resources.couldnt_refresh_profile
import teecclesia.designsystem.generated.resources.failed_to_delete_account
import teecclesia.designsystem.generated.resources.not_implemented_yet

class ProfileViewModel(
    private val authenticationRepository: AuthenticationRepository,
    private val profileRepository: ProfileRepository,
    private val authorizationService: AuthorizationService,
    private val settingsRepository: SettingsRepository,
    private val appLocalizer: AppLocalizer,
    private val notificationPermissionHandler: NotificationPermissionHandler
) : BaseViewModel<ProfileScreenState>(ProfileScreenState()) {

    init {
        observeCachedProfile()
        loadUserProfile()
        checkNotificationPermission()
        listenForUpdatedAcademicYear()
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
                            imageUrl = cached.imageUrl,
                            whatsAppLink =  cached.whatsAppLink
                        )
                    }
                    if (cached.role == UserRole.ADMIN) {
                        if (state.value.currentAcademicYear.isBlank()) {
                            loadAcademicYear()
                        }
                        loadDeletionRequestsCount()
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
        val canSearch = authorizationService.canSearchUsers()
        val canAdd = authorizationService.canAddStudent()
        updateState {
            copy(
                canAddUser = canAdd,
                canSearchUsers = canSearch,
                whatsAppLink = whatsAppLink
            )
        }

        tryToCall(
            block = {
                profileRepository.getRegistrationProfile()
                val canSearch = authorizationService.canSearchUsers()
                val canAdd = authorizationService.canAddStudent()
                Pair(canSearch, canAdd)
            },
            onSuccess = { (canSearch, canAdd) ->
                updateState {
                    copy(
                        canAddUser = canAdd,
                        canSearchUsers = canSearch
                    )
                }
            },
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

    fun onClickSendNotification() {
        navigate(SendNotificationRoute)
    }

    fun onClickEditSuggestions(){
        navigate(EditSuggestionsRoute)
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

    fun onRefresh() {
        if (!state.value.isRefreshing) {
            updateState { copy(isRefreshing = true) }
            if (state.value.userRole == UserRole.ADMIN) {
                loadAcademicYear()
                loadDeletionRequestsCount()
            }
            tryToCall(
                block = {
                    profileRepository.getRegistrationProfile()
                    val canSearch = authorizationService.canSearchUsers()
                    val canAdd = authorizationService.canAddStudent()
                    Pair(canSearch, canAdd)
                },
                onSuccess = { (canSearch, canAdd) ->
                    updateState {
                        copy(
                            canAddUser = canAdd,
                            canSearchUsers = canSearch
                        )
                    }
                },
                onError = {
                    showSnackBar(
                        title = UiText.StringRes(Res.string.couldnt_refresh_profile),
                        isSuccess = false
                    )
                },
                onEnd = { updateState { copy(isRefreshing = false) } }
            )
        }
    }

    fun checkNotificationPermission() {
        notificationPermissionHandler.checkPermission { isGranted ->
            updateState { copy(isNotificationPermissionGranted = isGranted) }
        }
    }

    fun openNotificationSettings() {
        notificationPermissionHandler.openNotificationSettings()
    }

    private fun listenForUpdatedAcademicYear() {
        launch {
            getResult<String>(KEY_UPDATED_ACADEMIC_YEAR, consume = true).collect { updatedYear ->
                if (!updatedYear.isNullOrBlank()) {
                    updateState { copy(currentAcademicYear = updatedYear) }
                }
            }
        }
    }

    private fun loadAcademicYear() {
        tryToCall(
            block = { profileRepository.getCurrentAcademicYear() },
            onSuccess = { year ->
                updateState { copy(currentAcademicYear = year.toString()) }
            },
            onError = { }
        )
    }

    fun onClickEditAcademicYear() {
        navigate(AcademicYearSettingsRoute)
    }

    private fun loadDeletionRequestsCount() {
        if (state.value.userRole == UserRole.ADMIN) {
            tryToCall(
                block = { profileRepository.getDeletionRequestCount() },
                onSuccess = { count ->
                    updateState { copy(deletionRequestsCount = count) }
                },
                onError = { }
            )
        }
    }

    fun onClickDeletionRequests() {
        navigate(DeletionRequestsRoute)
    }

    fun onClickDeleteAccount() {
        updateState {
            copy(
                isDeleteAccountSheetVisible = true,
                deleteAccountReason = "",
                deleteAccountPassword = "",
                isDeleteAccountPasswordVisible = false,
                deleteAccountButtonState = AppButtonState.Enabled
            )
        }
    }

    fun onDeleteAccountReasonChange(reason: String) {
        updateState { copy(deleteAccountReason = reason) }
    }

    fun onDeleteAccountPasswordChange(password: String) {
        updateState { copy(deleteAccountPassword = password) }
    }

    fun onToggleDeleteAccountPasswordVisibility() {
        updateState { copy(isDeleteAccountPasswordVisible = !isDeleteAccountPasswordVisible) }
    }

    fun onDismissDeleteAccountSheet() {
        updateState { copy(isDeleteAccountSheetVisible = false) }
    }

    fun onConfirmDeleteAccount() {
        val reason = state.value.deleteAccountReason.trim()
        val password = state.value.deleteAccountPassword.trim()
        if (reason.isBlank() || password.isBlank()) {
            return
        }

        tryToCall(
            onStart = { updateState { copy(deleteAccountButtonState = AppButtonState.Loading) } },
            block = {
                profileRepository.requestAccountDeletion(reason = reason, password = password)
                authenticationRepository.clearAuthTokens()
            },
            onSuccess = {
                updateState {
                    copy(
                        isDeleteAccountSheetVisible = false,
                        deleteAccountButtonState = AppButtonState.Enabled
                    )
                }
                showSnackBar(
                    title = UiText.StringRes(Res.string.account_deleted_successfully),
                    isSuccess = true
                )
                resetTo(LoginRoute)
            },
            onError = { throwable ->
                updateState { copy(deleteAccountButtonState = AppButtonState.Enabled) }
                showSnackBar(
                    title = UiText.StringRes(Res.string.failed_to_delete_account),
                    message = getLocalizedErrorMessage(throwable),
                    isSuccess = false
                )
            }
        )
    }
}



