package com.teEcclesia.identity.presentation.screen.academicYear

import com.teEcclesia.designsystem.components.button.AppButtonState
import com.teEcclesia.designsystem.navigation.BaseViewModel
import com.teEcclesia.designsystem.utils.UiText
import com.teEcclesia.identity.domain.repository.ProfileRepository
import com.teEcclesia.identity.presentation.util.getLocalizedErrorMessage
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.academic_year_updated_successfully
import teecclesia.designsystem.generated.resources.failed_to_load_academic_year
import teecclesia.designsystem.generated.resources.failed_to_update_academic_year
import teecclesia.designsystem.generated.resources.invalid_academic_year

class AcademicYearSettingsViewModel(
    private val profileRepository: ProfileRepository
) : BaseViewModel<AcademicYearSettingsUiState>(AcademicYearSettingsUiState()),
    AcademicYearSettingsInteractionListener {

    init {
        loadCurrentAcademicYear()
    }

    private fun loadCurrentAcademicYear(isRefresh: Boolean = false) {
        tryToCall(
            onStart = {
                updateState {
                    copy(
                        isLoading = !isRefresh,
                        isRefreshing = isRefresh,
                        isLoadFailed = false
                    )
                }
            },
            block = { profileRepository.getCurrentAcademicYear() },
            onSuccess = { year ->
                updateState {
                    copy(
                        academicYear = year.toString(),
                        isLoadFailed = false
                    )
                }
            },
            onError = { throwable ->
                updateState { copy(isLoadFailed = true) }
                showSnackBar(
                    title = UiText.StringRes(Res.string.failed_to_load_academic_year),
                    message = getLocalizedErrorMessage(throwable),
                    isSuccess = false
                )
            },
            onEnd = {
                updateState {
                    copy(
                        isLoading = false,
                        isRefreshing = false
                    )
                }
            }
        )
    }

    override fun onRetryLoad() {
        loadCurrentAcademicYear(isRefresh = false)
    }

    override fun onRefresh() {
        loadCurrentAcademicYear(isRefresh = true)
    }

    override fun onAcademicYearChanged(year: String) {
        val filtered = year.filter { it.isDigit() }.take(4)
        updateState {
            copy(
                academicYear = filtered,
                academicYearError = null
            )
        }
    }

    override fun onClickSave() {
        val yearInt = state.value.academicYear.toIntOrNull()
        if (yearInt == null || yearInt !in 2000..2100) {
            updateState {
                copy(academicYearError = UiText.StringRes(Res.string.invalid_academic_year))
            }
            return
        }
        updateState { copy(isConfirmDialogOpen = true) }
    }

    override fun onDismissConfirmDialog() {
        updateState { copy(isConfirmDialogOpen = false) }
    }

    override fun onConfirmSave() {
        val yearInt = state.value.academicYear.toIntOrNull() ?: return
        updateState { copy(isConfirmDialogOpen = false) }

        tryToCall(
            onStart = {
                updateState {
                    copy(
                        isSaving = true,
                        saveButtonState = AppButtonState.Loading
                    )
                }
            },
            block = { profileRepository.updateCurrentAcademicYear(yearInt) },
            onSuccess = {
                showSnackBar(
                    title = UiText.StringRes(Res.string.academic_year_updated_successfully),
                    message = null,
                    isSuccess = true
                )
                popBackStack()
            },
            onError = { throwable ->
                showSnackBar(
                    title = UiText.StringRes(Res.string.failed_to_update_academic_year),
                    message = getLocalizedErrorMessage(throwable),
                    isSuccess = false
                )
            },
            onEnd = {
                updateState {
                    copy(
                        isSaving = false,
                        saveButtonState = AppButtonState.Enabled
                    )
                }
            }
        )
    }

    override fun onClickBack() {
        popBackStack()
    }
}
