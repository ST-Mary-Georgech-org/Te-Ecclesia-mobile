package com.teEcclesia.identity.presentation.screen.areaSuggestion

import com.teEcclesia.lookups.domain.model.AreaResponse

interface AreaSuggestionsSettingsInteractionListener {

    fun onLoadMore()
    fun onClickAddArea()
    fun onAreaNameChanged(name: String)
    fun onClickEditArea(areaResponse: AreaResponse)
    fun onClickDeleteArea(areaResponse: AreaResponse)
    fun onClickSaveArea()
    fun onConfirmAreaDelete()
    fun onDismissSheet()
    fun onDismissConfirmDialog()
    fun onClickBack()
    fun onRetryLoad()
    fun onRefresh()
}
