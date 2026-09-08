package com.teEcclesia.identity.presentation.screen.attendance.register

import com.teEcclesia.identity.domain.model.attendance.EventAttendee

interface AttendanceRegisterInteractionListener {
    fun onClickBack()
    fun onToggleScanner()
    fun onCloseScanner()
    fun onUserCodeChanged(code: String)
    fun onManualSubmit()
    fun onQrCodeScanned(code: String)
    fun onClickRemoveAttendee(attendee: EventAttendee)
    fun onConfirmRemoveAttendee()
    fun onDismissSheet()
}
