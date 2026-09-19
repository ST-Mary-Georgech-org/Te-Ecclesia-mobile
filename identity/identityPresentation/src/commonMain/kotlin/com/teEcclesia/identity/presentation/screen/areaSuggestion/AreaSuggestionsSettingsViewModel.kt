package com.teEcclesia.identity.presentation.screen.areaSuggestion

import com.teEcclesia.designsystem.navigation.BaseViewModel
import com.teEcclesia.designsystem.utils.UiText
import com.teEcclesia.designsystem.utils.getLocalizedErrorMessage
import com.teEcclesia.identity.presentation.util.toPagedData
import com.teEcclesia.lookups.domain.model.AreaResponse
import com.teEcclesia.lookups.domain.repository.LookupRepository
import com.teEcclesia.shared.domain.utils.PageQuery
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.failed_to_delete_service
import teecclesia.designsystem.generated.resources.failed_to_save_service

class AreaSuggestionsSettingsViewModel(
    private val lookupRepository: LookupRepository
    ) : BaseViewModel<AreaSuggestionsSettingsUiState>(AreaSuggestionsSettingsUiState()),
    AreaSuggestionsSettingsInteractionListener {

    init {
        searchAreas()
    }

    private fun searchAreas() {
        tryToCall(
            block = {
                lookupRepository.getAreasForSuggestions(query = "", pageQuery = PageQuery(page = 0, size = 20))
                    .toPagedData()
            },
            onStart = { updateState { copy(isLoading = true, isLoadFailed = false) } },
            onSuccess = { items ->
                updateState {
                    copy(areas = items.data, isEditAreaSheetVisible = true, isLoadFailed = false)
                }
            },
            onError = { _ ->
                updateState { copy(isLoadFailed = true, isEditAreaSheetVisible = true) }
            },
            onEnd = { updateState { copy(isLoading = false) } }
        )
    }

    override fun onClickAddArea() {
        updateState {
            copy(
                isAddEditSheetOpen = true,
                editingArea = null,
                areaNameInput = "",
            )
        }
    }

    override fun onAreaNameChanged(name : String) {
        updateState { copy(areaNameInput = name) }
    }

    override fun onClickEditArea(areaResponse: AreaResponse) {
        updateState {
            copy(
                isAddEditSheetOpen = true,
                editingArea = areaResponse,
                areaNameInput = areaResponse.name,
            )
        }
    }

    override fun onClickDeleteArea(areaResponse: AreaResponse) {
        updateState {
            copy(
                isDeleteConfirmSheetOpen = true,
                deletingArea = areaResponse
            )
        }
    }

    override fun onClickSaveArea() {
        val input = state.value.areaNameInput.trim()
        if (input.isBlank()) return

        tryToCall(
            onStart = { updateState { copy(isActionLoading = true) } },
            block = {
                if (state.value.editingArea == null) {
                    lookupRepository.createArea(
                        AreaResponse(id = 0, name = input)
                    )
                } else {
                    state.value.editingArea?.let { area ->
                        lookupRepository.updateArea(
                            AreaResponse(
                                id = area.id,
                                name = input
                            )
                        )
                    }
                }
            },
            onSuccess = {
                onDismissSheet()
                searchAreas()
            },
            onError = { throwable ->
                showSnackBar(
                    title = UiText.StringRes(Res.string.failed_to_save_service),
                    message = getLocalizedErrorMessage(throwable),
                    isSuccess = false
                )
            },
            onEnd = { updateState { copy(isActionLoading = false) } }
        )
    }

    override fun onDismissSheet() {
        updateState { copy(isAddEditSheetOpen = false) }
    }

    override fun onDismissConfirmDialog() {
        updateState { copy(
            isAddEditSheetOpen = false,
            areaNameInput = "",
            isDeleteConfirmSheetOpen = false,
            deletingArea = null
        ) }
    }

    override fun onConfirmAreaDelete() {
        val service = state.value.deletingArea ?: return
        tryToCall(
            onStart = { updateState { copy(isActionLoading = true) } },
            block = { lookupRepository.deleteArea(service.id) },
            onSuccess = {
                onDismissSheet()
                searchAreas()
            },
            onError = { throwable ->
                showSnackBar(
                    title = UiText.StringRes(Res.string.failed_to_delete_service),
                    message = getLocalizedErrorMessage(throwable),
                    isSuccess = false
                )
            },
            onEnd = { updateState { copy(
                isActionLoading = false,
                isDeleteConfirmSheetOpen = false
            ) } }
        )
    }

    override fun onRetryLoad() {
        searchAreas()
    }

    override fun onRefresh() {
        searchAreas()
    }

    override fun onClickBack() {
        popBackStack()
    }

}
