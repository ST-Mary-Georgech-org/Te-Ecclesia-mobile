package com.teEcclesia.identity.presentation.screen.attendance.register

import com.teEcclesia.designsystem.navigation.BaseViewModel
import com.teEcclesia.designsystem.utils.UiText
import com.teEcclesia.identity.domain.model.attendance.EventAttendee
import com.teEcclesia.identity.domain.repository.AttendanceRepository
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.failed_to_register_member

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

    override fun onToggleScanner() {
        updateState { copy(isScannerOpen = !isScannerOpen) }
    }

    override fun onCloseScanner() {
        updateState { copy(isScannerOpen = false) }
    }

    override fun onUserCodeChanged(code: String) {
        updateState { copy(userCodeInput = code, searchUserError = null) }
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
            onStart = { updateState { copy(isActionLoading = true, searchUserError = null) } },
            block = { attendanceRepository.addAttendee(state.value.eventId, code) },
            onSuccess = { newAttendee ->
                updateState {
                    val updatedList = (listOf(newAttendee) + attendees.filter { it.userId != newAttendee.userId })
                    copy(attendees = updatedList, userCodeInput = "", searchUserError = null)
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
                isRemoveConfirmSheetOpen = false,
                removingAttendee = null
            )
        }
    }
}
