package com.teEcclesia.identity.presentation.screen.attendance.services

import com.teEcclesia.identity.domain.model.attendance.AttendeeUserPreview
import com.teEcclesia.identity.domain.model.attendance.ChurchService
import com.teEcclesia.identity.domain.model.attendance.ResponsibleServant
import com.teEcclesia.lookups.domain.model.LookupResponse
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

interface ServicesListInteractionListener {
    fun onClickAddService()
    fun onClickEditService(service: ChurchService)
    fun onClickDeleteService(service: ChurchService)
    fun onServiceNameChanged(name: String)
    fun onToggleStageSelection(stage: LookupResponse)
    fun onToggleStageSheet(visible: Boolean)
    fun onLoadNextStages()
    fun onRetryLoadStages()
    fun onServantSearchQueryChanged(query: String)
    fun onSelectServant(servant: AttendeeUserPreview)
    fun onRemoveServant(servant: ResponsibleServant)
    fun onDismissServantSuggestions()
    fun onConfirmSaveService()
    fun onConfirmDeleteService()
    fun onToggleAddRepeatedEvent()
    fun onEventNameChanged(name: String)
    fun onClickDatePicker()
    fun onDismissDatePicker()
    fun onDateSelected(date: LocalDate)
    fun onRepeatEveryChanged(duration: String)
    fun onClickStartTimePicker()
    fun onDismissStartTimePicker()
    fun onStartTimeSelected(time: LocalTime)
    fun onClickEndTimePicker()
    fun onDismissEndTimePicker()
    fun onEndTimeSelected(time: LocalTime)
    fun onDismissSheet()
    fun onClickService(service: ChurchService)
    fun onRefresh()
    fun onLoadMore()
}
