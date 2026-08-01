package com.teEcclesia.identity.presentation.screen.attendance.services

import com.teEcclesia.identity.domain.model.attendance.ChurchService

interface ServicesListInteractionListener {
    fun onClickAddService()
    fun onClickEditService(service: ChurchService)
    fun onClickDeleteService(service: ChurchService)
    fun onServiceNameChanged(name: String)
    fun onConfirmSaveService()
    fun onConfirmDeleteService()
    fun onDismissSheet()
    fun onClickService(service: ChurchService)
}
