package com.teEcclesia.identity.presentation.screen.attendance.events

import com.teEcclesia.identity.domain.model.attendance.ServiceEvent
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

interface EventsListInteractionListener {
    fun onClickBack()
    fun onClickAddEvent()
    fun onClickEditEvent(event: ServiceEvent)
    fun onClickDeleteEvent(event: ServiceEvent)
    fun onEventNameChanged(name: String)
    fun onClickDatePicker()
    fun onDismissDatePicker()
    fun onDateSelected(date: LocalDate)
    fun onClickStartTimePicker()
    fun onDismissStartTimePicker()
    fun onStartTimeSelected(time: LocalTime)
    fun onClickEndTimePicker()
    fun onDismissEndTimePicker()
    fun onEndTimeSelected(time: LocalTime)
    fun onConfirmSaveEvent()
    fun onConfirmDeleteEvent()
    fun onDismissSheet()
    fun onClickEvent(event: ServiceEvent)
    fun onRefresh()
    fun onLoadMore()
}
