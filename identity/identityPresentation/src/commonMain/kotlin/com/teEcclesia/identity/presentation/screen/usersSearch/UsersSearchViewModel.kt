package com.teEcclesia.identity.presentation.screen.usersSearch

import androidx.lifecycle.viewModelScope
import com.teEcclesia.designsystem.navigation.BaseViewModel
import com.teEcclesia.designsystem.utils.UiText
import com.teEcclesia.identity.api.ReviewAndEditRequestRoute
import com.teEcclesia.identity.domain.model.ProfileResponse
import com.teEcclesia.identity.domain.model.UserRole
import com.teEcclesia.identity.domain.repository.ProfileRepository
import com.teEcclesia.identity.presentation.util.getLocalizedErrorMessage
import com.teEcclesia.identity.presentation.util.toPagedData
import com.teEcclesia.lookups.domain.model.LookupResponse
import com.teEcclesia.lookups.domain.repository.LookupRepository
import com.teEcclesia.shared.domain.utils.PageQuery
import kotlinx.coroutines.launch
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.failed_to_load_educational_stages
import teecclesia.designsystem.generated.resources.failed_to_load_users

import com.teEcclesia.identity.domain.service.AuthorizationService

class UsersSearchViewModel(
    private val profileRepository: ProfileRepository,
    private val lookupRepository: LookupRepository,
    private val authorizationService: AuthorizationService
) : BaseViewModel<UsersSearchUiState>(UsersSearchUiState()), UsersSearchInteractionListener {

    private val usersPaginator = createPaginator(
        loadPage = { page ->
            val s = state.value
            profileRepository.getApprovedUsers(
                search = s.searchQuery.ifBlank { null },
                stageId = s.selectedStage?.id,
                yearId = s.selectedYear?.id,
                role = s.selectedRole,
                page = page,
                size = 20
            ).toPagedData()
        },
        onSuccess = { items ->
            updateState { current ->
                current.copy(
                    users = if (current.page == 0) items.data else current.users + items.data,
                    hasMorePages = !items.isLastPage,
                    totalUsersCount = items.totalItems,
                    isLoading = false,
                    isRefreshing = false
                )
            }
        },
        onLoadUpdated = { loading ->
            if (!state.value.isRefreshing) {
                updateState { it.copy(isLoading = loading) }
            }
        },
        onError = { throwable ->
            updateState { it.copy(isLoading = false, isRefreshing = false) }
            throwable?.let { t ->
                showSnackBar(
                    title = UiText.StringRes(Res.string.failed_to_load_users),
                    message = getLocalizedErrorMessage(t),
                    isSuccess = false
                )
            }
        }
    )

    private val stagesPaginator = createPaginator(
        loadPage = { page ->
            lookupRepository.getEducationalStages(PageQuery(page = page, size = 20)).toPagedData()
        },
        onSuccess = { items ->
            updateState { current ->
                current.copy(
                    stages = current.stages + items.data
                )
            }
        },
        onLoadUpdated = { _ -> },
        onError = { throwable ->
            throwable?.let { t ->
                showSnackBar(
                    title = UiText.StringRes(Res.string.failed_to_load_educational_stages),
                    message = getLocalizedErrorMessage(t),
                    isSuccess = false
                )
            }
        }
    )

    init {
        initializeFiltersAndLoadUsers()
    }

    private fun initializeFiltersAndLoadUsers() {
        launch {
            val role = authorizationService.getUserRole()
            if (role == UserRole.KHADEM) {
                val stageId = authorizationService.getKhademStageId()
                val yearId = authorizationService.getKhademYearId()
                if (stageId != null) {
                    val allStages = lookupRepository.getEducationalStages(PageQuery(page = 0, size = 100)).data
                    val stage = allStages.find { it.id == stageId }
                    if (stage != null) {
                        val year = if (yearId != null) stage.subItems.find { it.id == yearId } else null
                        if (year != null) {
                            updateState {
                                it.copy(
                                    selectedStage = stage,
                                    selectedYear = year,
                                    stages = listOf(stage),
                                    years = listOf(year),
                                    isStageFilterLocked = true,
                                    isYearFilterLocked = true
                                )
                            }
                        } else {
                            updateState {
                                it.copy(
                                    selectedStage = stage,
                                    stages = listOf(stage),
                                    years = stage.subItems,
                                    isStageFilterLocked = true,
                                    isYearFilterLocked = false
                                )
                            }
                        }
                    }
                }
            } else {
                loadStages()
            }
            loadUsers(reset = true)
        }
    }

    private fun loadUsers(reset: Boolean = false) {
        launch {
            if (reset) {
                updateState { it.copy(page = 0, users = emptyList(), hasMorePages = true) }
                usersPaginator.reset()
            }
            usersPaginator.loadNextItems()
        }
    }

    private fun loadStages() {
        launch {
            stagesPaginator.loadNextItems()
        }
    }

    override fun onSearchQueryChanged(query: String) {
        updateState { it.copy(searchQuery = query) }
        loadUsers(reset = true)
    }

    override fun onRoleFilterSelected(role: UserRole?) {
        updateState { it.copy(selectedRole = role) }
        loadUsers(reset = true)
    }

    override fun onStageFilterSelected(stage: LookupResponse?) {
        if (state.value.isStageFilterLocked) return
        val years = stage?.subItems ?: emptyList()
        updateState { it.copy(selectedStage = stage, selectedYear = null, years = years) }
        loadUsers(reset = true)
    }

    override fun onYearFilterSelected(year: LookupResponse?) {
        if (state.value.isYearFilterLocked) return
        updateState { it.copy(selectedYear = year) }
        loadUsers(reset = true)
    }

    override fun onToggleFilterSheet(visible: Boolean) {
        updateState { it.copy(isFilterSheetVisible = visible) }
    }

    override fun onResetFilters() {
        updateState { current ->
            current.copy(
                selectedRole = null,
                selectedStage = if (current.isStageFilterLocked) current.selectedStage else null,
                selectedYear = if (current.isYearFilterLocked) current.selectedYear else null,
                years = if (current.isStageFilterLocked) current.years else emptyList()
            )
        }
        loadUsers(reset = true)
    }

    override fun onUserClicked(user: ProfileResponse) {
        navigate(ReviewAndEditRequestRoute(userId = user.id))
    }

    override fun onLoadMore() {
        if (state.value.hasMorePages && !state.value.isLoading) {
            launch {
                updateState { it.copy(page = it.page + 1) }
                usersPaginator.loadNextItems()
            }
        }
    }

    override fun onRefresh() {
        updateState { it.copy(isRefreshing = true) }
        loadUsers(reset = true)
    }

    override fun onClickBack() {
        popBackStack()
    }
}
