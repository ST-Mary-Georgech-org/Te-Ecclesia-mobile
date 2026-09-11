package com.teEcclesia.identity.presentation.screen.academicYear

interface AcademicYearSettingsInteractionListener {
    fun onAcademicYearChanged(year: String)
    fun onClickSave()
    fun onConfirmSave()
    fun onDismissConfirmDialog()
    fun onClickBack()
    fun onRetryLoad()
    fun onRefresh()
}
