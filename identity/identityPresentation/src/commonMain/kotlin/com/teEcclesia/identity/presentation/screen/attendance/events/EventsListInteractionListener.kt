package com.teEcclesia.identity.presentation.screen.attendance.events

import com.teEcclesia.identity.domain.model.attendance.ServiceEvent

interface EventsListInteractionListener {
    fun onClickBack()
    fun onClickAddEvent()
    fun onClickEditEvent(event: ServiceEvent)
    fun onClickDeleteEvent(event: ServiceEvent)
    fun onEventNameChanged(name: String)
    fun onEventDateChanged(date: String)
    fun onStartTimeChanged(time: String)
    fun onEndTimeChanged(time: String)
    fun onConfirmSaveEvent()
    fun onConfirmDeleteEvent()
    fun onDismissSheet()
    fun onClickEvent(event: ServiceEvent)
}
