package com.teEcclesia.identity.presentation.screen.attendance.register

import com.teEcclesia.designsystem.navigation.BaseViewModel
import com.teEcclesia.identity.domain.model.attendance.EventAttendee
import com.teEcclesia.identity.domain.repository.AttendanceRepository

class AttendanceRegisterViewModel(
    eventId: Long,
    serviceName: String,
    eventName: String,
    private val attendanceRepository: AttendanceRepository
) : BaseViewModel<AttendanceRegisterUiState>(
    AttendanceRegisterUiState(eventId = eventId, serviceName = serviceName, eventName = eventName)
), AttendanceRegisterInteractionListener {

    init {
        loadAttendees()
    }

    private fun loadAttendees() {
        tryToCall(
            onStart = { updateState { copy(isLoading = true) } },
            block = { attendanceRepository.getAttendees(state.value.eventId) },
            onSuccess = { attendees ->
                updateState { copy(attendees = attendees) }
            },
            onError = { },
            onEnd = { updateState { copy(isLoading = false) } }
        )
    }

    override fun onClickBack() {
        popBackStack()
    }

    override fun onClickAddPerson() {
        updateState {
            copy(
                isAddPersonSheetOpen = true,
                userCodeInput = "",
                searchedUser = null,
                searchUserError = null
            )
        }
    }

    override fun onUserCodeChanged(code: String) {
        updateState { copy(userCodeInput = code, searchUserError = null) }
    }

    override fun onSearchUserByCode() {
        val code = state.value.userCodeInput.trim()
        if (code.isBlank()) return

        tryToCall(
            onStart = { updateState { copy(isSearchingUser = true, searchUserError = null) } },
            block = { attendanceRepository.getUserByCode(code) },
            onSuccess = { user ->
                updateState { copy(searchedUser = user) }
            },
            onError = {
                updateState { copy(searchUserError = "المستخدم غير موجود") }
            },
            onEnd = { updateState { copy(isSearchingUser = false) } }
        )
    }

    override fun onConfirmAddPerson() {
        val code = state.value.userCodeInput.trim()
        if (code.isBlank()) return

        tryToCall(
            onStart = { updateState { copy(isActionLoading = true) } },
            block = { attendanceRepository.addAttendee(state.value.eventId, code) },
            onSuccess = { newAttendee ->
                updateState {
                    val updatedList = (listOf(newAttendee) + attendees.filter { it.userId != newAttendee.userId })
                    copy(attendees = updatedList)
                }
                onDismissSheet()
            },
            onError = { },
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
                updateState { copy(attendees = attendees.filter { it.userId != attendee.userId }) }
                onDismissSheet()
            },
            onError = { },
            onEnd = { updateState { copy(isActionLoading = false) } }
        )
    }

    override fun onDismissSheet() {
        updateState {
            copy(
                isAddPersonSheetOpen = false,
                userCodeInput = "",
                searchedUser = null,
                searchUserError = null,
                isRemoveConfirmSheetOpen = false,
                removingAttendee = null
            )
        }
    }
}
