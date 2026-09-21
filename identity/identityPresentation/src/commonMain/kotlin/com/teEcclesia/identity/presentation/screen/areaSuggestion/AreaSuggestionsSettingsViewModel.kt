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
import teecclesia.designsystem.generated.resources.failed_to_load_areas
import teecclesia.designsystem.generated.resources.failed_to_save_service
import kotlin.collections.plus

class AreaSuggestionsSettingsViewModel(
    private val lookupRepository: LookupRepository
    ) : BaseViewModel<AreaSuggestionsSettingsUiState>(AreaSuggestionsSettingsUiState()),
    AreaSuggestionsSettingsInteractionListener {

    private val pageSize = 20

    private val areasPaginator = createPaginator(
        loadPage = { page ->
            lookupRepository.getAreasForSuggestions(
                query = "",
                pageQuery = PageQuery(page = page, size = pageSize)
            ).toPagedData()
        },
        onSuccess = { items ->
            updateState { current ->
                current.copy(
                    isLoading = false,
                    isPagingLoading = false,
                    isRefreshing = false,
                    areas = current.areas + items.data,
                    isLastPage = items.isLastPage
                )
            }
        },
        onLoadUpdated = { loading ->
            updateState { current ->
                if (current.areas.isEmpty() && !current.isRefreshing) {
                    current.copy(isLoading = loading)
                } else if (!current.isRefreshing) {
                    current.copy(isPagingLoading = loading)
                } else {
                    current
                }
            }
        },
        onReset = {
            updateState { it.copy(areas = emptyList(), isLastPage = false) }
        },
        onError = { throwable ->
            updateState { current ->
                current.copy(
                    isLoading = false,
                    isPagingLoading = false,
                    isRefreshing = false
                )
            }
            throwable?.let { t ->
                showSnackBar(
                    title = UiText.StringRes(Res.string.failed_to_load_areas),
                    message = getLocalizedErrorMessage(t),
                    isSuccess = false
                )
            }
        }
    )

    init {
        searchAreas()
    }

    private fun searchAreas() {
        areasPaginator.reset()
    }

    override fun onLoadMore() {
        if (!state.value.isLastPage && !state.value.isPagingLoading && !state.value.isLoading) {
            areasPaginator.loadNextItems()
        }
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
        updateState { copy(isRefreshing = true) }
        searchAreas()
    }

    override fun onClickBack() {
        popBackStack()
    }

}
