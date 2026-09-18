package com.teEcclesia.notifications.presentation.screen.adminSend

import com.teEcclesia.designsystem.components.button.AppButtonState
import com.teEcclesia.designsystem.navigation.BaseViewModel
import com.teEcclesia.designsystem.utils.UiText
import com.teEcclesia.identity.domain.model.attendance.AttendeeUserPreview
import com.teEcclesia.identity.domain.repository.AttendanceRepository
import com.teEcclesia.lookups.domain.model.LookupResponse
import com.teEcclesia.lookups.domain.repository.LookupRepository
import com.teEcclesia.notifications.domain.model.AdminSendNotificationParam
import com.teEcclesia.notifications.domain.repository.NotificationRepository
import com.teEcclesia.designsystem.utils.getLocalizedErrorMessage
import com.teEcclesia.notifications.presentation.util.toPagedData
import com.teEcclesia.shared.domain.model.UserRole
import com.teEcclesia.shared.domain.utils.PageQuery
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.failed_to_send_notification
import teecclesia.designsystem.generated.resources.notification_sent_successfully
import teecclesia.designsystem.generated.resources.please_enter_notification_body
import teecclesia.designsystem.generated.resources.please_enter_notification_title
import teecclesia.designsystem.generated.resources.please_select_recipients
import kotlin.time.Duration.Companion.milliseconds

class AdminSendNotificationViewModel(
    private val notificationRepository: NotificationRepository,
    private val attendanceRepository: AttendanceRepository,
    private val lookupRepository: LookupRepository
) : BaseViewModel<AdminSendNotificationUiState>(AdminSendNotificationUiState()),
    AdminSendNotificationInteractionListener {

    private val pageSize = 20
    private var searchJob: Job? = null
    private var payloadCounter: Int = 0

    private val stagesPaginator = createPaginator(
        initialKey = 0,
        loadPage = { page ->
            lookupRepository.getEducationalStages(
                pageQuery = PageQuery(page = page, size = pageSize),
                forRole = state.value.selectedRole
            ).toPagedData()
        },
        onSuccess = { items ->
            updateState {
                copy(
                    educationalStages = educationalStages + items.data,
                    isLoadingStages = false,
                    isStageLoadFailed = false
                )
            }
        },
        onLoadUpdated = { loading ->
            updateState { copy(isLoadingStages = loading) }
        },
        onReset = {
            updateState { copy(educationalStages = emptyList(), isLoadingStages = false, isStageLoadFailed = false) }
        },
        onError = {
            updateState { copy(isLoadingStages = false, isStageLoadFailed = true) }
        }
    )

    init {
        loadEducationalStages()
    }

    private fun loadEducationalStages() {
        stagesPaginator.reset()
    }

    override fun onClickBack() {
        popBackStack()
    }

    override fun onTargetModeSelected(mode: RecipientTargetMode) {
        updateState { copy(targetMode = mode) }
    }

    override fun onTitleChanged(title: String) {
        updateState { copy(title = title, titleError = null) }
    }

    override fun onBodyChanged(body: String) {
        updateState { copy(body = body, bodyError = null) }
    }

    override fun onUserSearchQueryChanged(query: String) {
        updateState { copy(userSearchQuery = query) }
        searchJob?.cancel()
        val trimmed = query.trim()
        if (trimmed.length >= 2) {
            searchJob = launch {
                delay(300.milliseconds)
                searchCandidates(trimmed)
            }
        } else {
            updateState {
                copy(
                    suggestedUsers = emptyList(),
                    isSuggestionsDropdownVisible = false,
                    isSearchingSuggestions = false
                )
            }
        }
    }

    private fun searchCandidates(query: String) {
        tryToCall(
            onStart = { updateState { copy(isSearchingSuggestions = true) } },
            block = { attendanceRepository.searchUsers(query) },
            onSuccess = { users ->
                updateState {
                    copy(
                        suggestedUsers = users,
                        isSuggestionsDropdownVisible = users.isNotEmpty()
                    )
                }
            },
            onError = {
                updateState { copy(suggestedUsers = emptyList(), isSuggestionsDropdownVisible = false) }
            },
            onEnd = { updateState { copy(isSearchingSuggestions = false) } }
        )
    }

    override fun onSelectSuggestedUser(user: AttendeeUserPreview) {
        updateState {
            val current = selectedUsers
            val updated = if (current.none { it.id == user.id }) current + user else current
            copy(
                selectedUsers = updated,
                userSearchQuery = "",
                suggestedUsers = emptyList(),
                isSuggestionsDropdownVisible = false
            )
        }
    }

    override fun onRemoveSelectedUser(user: AttendeeUserPreview) {
        updateState {
            copy(selectedUsers = selectedUsers.filter { it.id != user.id })
        }
    }

    override fun onDismissSuggestionsDropdown() {
        updateState { copy(isSuggestionsDropdownVisible = false) }
    }

    override fun onToggleRoleDropdown() {
        updateState { copy(isRoleDropdownExpanded = !isRoleDropdownExpanded) }
    }

    override fun onSelectRole(role: UserRole?) {
        updateState {
            val stage = if (role == null || role == UserRole.KHADEM || role == UserRole.MAKHDOOM) {
                selectedEducationalStage
            } else {
                null
            }
            copy(
                selectedRole = role,
                selectedEducationalStage = stage,
                isRoleDropdownExpanded = false
            )
        }
        if (role == null || role == UserRole.KHADEM || role == UserRole.MAKHDOOM) {
            stagesPaginator.reset()
        }
    }

    override fun onDismissRoleDropdown() {
        updateState { copy(isRoleDropdownExpanded = false) }
    }

    override fun onToggleStageSheet(isVisible: Boolean) {
        updateState { copy(isStageSheetVisible = isVisible) }
    }

    override fun onSelectStage(stage: LookupResponse?) {
        updateState { copy(selectedEducationalStage = stage, isStageSheetVisible = false) }
    }

    override fun onLoadNextStages() {
        if (!state.value.isLoadingStages && !state.value.isStageLoadFailed) {
            stagesPaginator.loadNextItems()
        }
    }

    override fun onRetryLoadStages() {
        stagesPaginator.reset()
    }

    override fun onTogglePayloadExpanded() {
        updateState { copy(isPayloadExpanded = !isPayloadExpanded) }
    }

    override fun onAddPayloadItem() {
        val nextId = (++payloadCounter).toString()
        updateState {
            copy(
                payloadItems = payloadItems + PayloadItemUiState(id = nextId),
                isPayloadExpanded = true
            )
        }
    }

    override fun onPayloadKeyChanged(id: String, key: String) {
        updateState {
            copy(
                payloadItems = payloadItems.map {
                    if (it.id == id) it.copy(key = key) else it
                }
            )
        }
    }

    override fun onPayloadValueChanged(id: String, value: String) {
        updateState {
            copy(
                payloadItems = payloadItems.map {
                    if (it.id == id) it.copy(value = value) else it
                }
            )
        }
    }

    override fun onRemovePayloadItem(id: String) {
        updateState {
            copy(payloadItems = payloadItems.filter { it.id != id })
        }
    }

    override fun onClickSend() {
        val currentState = state.value
        var hasError = false

        if (currentState.title.trim().isBlank()) {
            updateState { copy(titleError = UiText.StringRes(Res.string.please_enter_notification_title)) }
            hasError = true
        }

        if (currentState.body.trim().isBlank()) {
            updateState { copy(bodyError = UiText.StringRes(Res.string.please_enter_notification_body)) }
            hasError = true
        }

        if (currentState.targetMode == RecipientTargetMode.SPECIFIC_USERS && currentState.selectedUsers.isEmpty()) {
            showSnackBar(
                title = UiText.StringRes(Res.string.please_select_recipients),
                isSuccess = false
            )
            hasError = true
        }

        if (hasError) return

        val userIds = if (currentState.targetMode == RecipientTargetMode.SPECIFIC_USERS) {
            currentState.selectedUsers.map { it.id }
        } else {
            null
        }

        val role = if (currentState.targetMode == RecipientTargetMode.TARGET_GROUP) {
            currentState.selectedRole
        } else {
            null
        }

        val educationalStageId = if (currentState.targetMode == RecipientTargetMode.TARGET_GROUP && currentState.isEducationalStageVisible) {
            currentState.selectedEducationalStage?.id
        } else {
            null
        }

        val payloadMap = currentState.payloadItems
            .filter { it.key.isNotBlank() }
            .associate { it.key.trim() to it.value.trim() }

        val param = AdminSendNotificationParam(
            title = currentState.title.trim(),
            body = currentState.body.trim(),
            userIds = userIds,
            role = role,
            educationalStageId = educationalStageId,
            dataPayload = payloadMap.ifEmpty { null }
        )

        tryToCall(
            onStart = { updateState { copy(actionButtonState = AppButtonState.Loading, isSuccessBannerVisible = false) } },
            block = { notificationRepository.sendAdminNotification(param) },
            onSuccess = {
                updateState { copy(isSuccessBannerVisible = true) }
            },
            onError = { throwable ->
                showSnackBar(
                    title = UiText.StringRes(Res.string.failed_to_send_notification),
                    message = getLocalizedErrorMessage(throwable),
                    isSuccess = false
                )
            },
            onEnd = { updateState { copy(actionButtonState = AppButtonState.Enabled) } }
        )
    }

    override fun onDismissSuccessBanner() {
        updateState { copy(isSuccessBannerVisible = false) }
    }
}
