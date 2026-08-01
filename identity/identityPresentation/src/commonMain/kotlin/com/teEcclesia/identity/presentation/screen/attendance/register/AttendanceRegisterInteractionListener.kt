package com.teEcclesia.identity.presentation.screen.attendance.register

import com.teEcclesia.identity.domain.model.attendance.EventAttendee

interface AttendanceRegisterInteractionListener {
    fun onClickBack()
    fun onClickAddPerson()
    fun onUserCodeChanged(code: String)
    fun onSearchUserByCode()
    fun onConfirmAddPerson()
    fun onClickRemoveAttendee(attendee: EventAttendee)
    fun onConfirmRemoveAttendee()
    fun onDismissSheet()
}
