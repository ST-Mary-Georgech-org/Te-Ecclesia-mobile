package com.teEcclesia.identity.presentation.screen.attendance.register

import com.teEcclesia.designsystem.navigation.BaseViewModel
import com.teEcclesia.designsystem.utils.UiText
import com.teEcclesia.identity.domain.model.attendance.AttendeeUserPreview
import com.teEcclesia.identity.domain.model.attendance.EventAttendee
import com.teEcclesia.identity.domain.repository.AttendanceRepository
import com.teEcclesia.identity.presentation.util.getLocalizedErrorMessage
import com.teEcclesia.identity.presentation.util.toPagedData
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.failed_to_load_attendees
import teecclesia.designsystem.generated.resources.failed_to_register_member
import kotlin.time.Duration.Companion.milliseconds

class AttendanceRegisterViewModel(
    eventId: Long,
    serviceName: String,
    eventName: String,
    isResponsible: Boolean,
    private val attendanceRepository: AttendanceRepository
) : BaseViewModel<AttendanceRegisterUiState>(
    AttendanceRegisterUiState(eventId = eventId, serviceName = serviceName, eventName = eventName, isResponsible = isResponsible)
), AttendanceRegisterInteractionListener {

    private val pageSize = 20
    private var searchJob: Job? = null

    private val attendeesPaginator = createPaginator(
        loadPage = { page ->
            attendanceRepository.getAttendees(
                eventId = state.value.eventId,
                page = page,
                size = pageSize
            ).toPagedData()
        },
        onSuccess = { items ->
            updateState { current ->
                current.copy(
                    isLoading = false,
                    isPagingLoading = false,
                    isRefreshing = false,
                    attendees = current.attendees + items.data,
                    totalAttendees = items.totalItems,
                    isLastPage = items.isLastPage
                )
            }
        },
        onLoadUpdated = { loading ->
            updateState { current ->
                if (current.attendees.isEmpty() && !current.isRefreshing) {
                    current.copy(isLoading = loading)
                } else if (!current.isRefreshing) {
                    current.copy(isPagingLoading = loading)
                } else {
                    current
                }
            }
        },
        onReset = {
            updateState { it.copy(attendees = emptyList(), isLastPage = false) }
        },
        onError = { throwable ->
            updateState { current ->
                current.copy(
                    isLoading = false,
                    isPagingLoading = false,
                    isRefreshing = false
                )
            }
            throwable?.let { t ->
                showSnackBar(
                    title = UiText.StringRes(Res.string.failed_to_load_attendees),
                    message = getLocalizedErrorMessage(t),
                    isSuccess = false
                )
            }
        }
    )

    init {
        loadAttendees()
    }

    private fun loadAttendees() {
        attendeesPaginator.reset()
    }

    override fun onRefresh() {
        updateState { it.copy(isRefreshing = true) }
        loadAttendees()
    }

    override fun onLoadMore() {
        if (!state.value.isLastPage && !state.value.isPagingLoading && !state.value.isLoading) {
            attendeesPaginator.loadNextItems()
        }
    }

    override fun onClickBack() {
        popBackStack()
    }

    override fun onToggleScanner() {
        updateState { copy(isScannerOpen = !isScannerOpen) }
    }

    override fun onCloseScanner() {
        updateState { copy(isScannerOpen = false) }
    }

    override fun onUserCodeChanged(code: String) {
        updateState { copy(userCodeInput = code, searchUserError = null) }
        searchJob?.cancel()
        val query = code.trim()
        if (query.length >= 2) {
            searchJob = launch {
                delay(300.milliseconds)
                searchCandidates(query)
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
            copy(
                isSuggestionsDropdownVisible = false,
                suggestedUsers = emptyList(),
                userCodeInput = user.code ?: ""
            )
        }
        val identifier = user.code?.takeIf { it.isNotBlank() } ?: user.id
        addMemberByCode(identifier)
    }

    override fun onDismissSuggestions() {
        updateState { copy(isSuggestionsDropdownVisible = false) }
    }

    override fun onManualSubmit() {
        val code = state.value.userCodeInput.trim()
        if (code.isNotBlank()) {
            addMemberByCode(code)
        }
    }

    override fun onQrCodeScanned(code: String) {
        val trimmed = code.trim()
        if (trimmed.isNotBlank()) {
            updateState { copy(userCodeInput = trimmed, searchUserError = null) }
            addMemberByCode(trimmed)
        }
    }

    private fun addMemberByCode(code: String) {
        tryToCall(
            onStart = {
                searchJob?.cancel()
                updateState {
                    copy(
                        isActionLoading = true,
                        searchUserError = null,
                        isSuggestionsDropdownVisible = false,
                        suggestedUsers = emptyList()
                    )
                }
            },
            block = { attendanceRepository.addAttendee(state.value.eventId, code) },
            onSuccess = { newAttendee ->
                updateState {
                    val alreadyInList = attendees.any { it.userId == newAttendee.userId }
                    val updatedList = listOf(newAttendee) + attendees.filter { it.userId != newAttendee.userId }
                    val newTotal = if (!alreadyInList) totalAttendees + 1 else totalAttendees
                    copy(
                        attendees = updatedList,
                        totalAttendees = newTotal,
                        userCodeInput = "",
                        searchUserError = null,
                        isSuggestionsDropdownVisible = false,
                        suggestedUsers = emptyList()
                    )
                }
            },
            onError = { throwable ->
                val error = throwable.message?.takeIf { it.isNotBlank() }?.let { UiText.DynamicString(it) }
                    ?: UiText.StringRes(Res.string.failed_to_register_member)
                updateState { copy(searchUserError = error) }
            },
            onEnd = { updateState { copy(isActionLoading = false) } }
        )
    }

    override fun onClickRemoveAttendee(attendee: EventAttendee) {
        updateState {
            copy(
                isRemoveConfirmSheetOpen = true,
                removingAttendee = attendee
            )
        }
    }

    override fun onConfirmRemoveAttendee() {
        val attendee = state.value.removingAttendee ?: return
        tryToCall(
            onStart = { updateState { copy(isActionLoading = true) } },
            block = { attendanceRepository.removeAttendee(state.value.eventId, attendee.userId) },
            onSuccess = {
                updateState {
                    copy(
                        attendees = attendees.filter { it.userId != attendee.userId },
                        totalAttendees = (totalAttendees - 1).coerceAtLeast(0)
                    )
                }
                onDismissSheet()
            },
            onError = { },
            onEnd = { updateState { copy(isActionLoading = false) } }
        )
    }

    override fun onDismissSheet() {
        updateState {
            copy(
                isRemoveConfirmSheetOpen = false,
                removingAttendee = null
            )
        }
    }
}
