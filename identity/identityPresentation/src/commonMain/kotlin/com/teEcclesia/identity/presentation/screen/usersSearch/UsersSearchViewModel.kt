package com.teEcclesia.identity.presentation.screen.usersSearch

import com.teEcclesia.designsystem.navigation.BaseViewModel
import com.teEcclesia.designsystem.utils.UiText
import com.teEcclesia.identity.api.EditUserRoute
import com.teEcclesia.identity.domain.model.ProfileResponse
import com.teEcclesia.shared.domain.model.UserRole
import com.teEcclesia.identity.domain.repository.ProfileRepository
import com.teEcclesia.identity.presentation.util.getLocalizedErrorMessage
import com.teEcclesia.identity.presentation.util.toPagedData
import com.teEcclesia.lookups.domain.model.LookupResponse
import com.teEcclesia.lookups.domain.repository.LookupRepository
import com.teEcclesia.shared.domain.utils.PageQuery
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
                    users = current.users + items.data,
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
        onReset = {
            updateState { it.copy(users = emptyList(), hasMorePages = true) }
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
        onReset = {
            updateState { it.copy(stages = emptyList()) }
        },
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
        tryToCall(
            block = {
                val role = authorizationService.getUserRole()
                val canApprove = authorizationService.canApproveRequests()
                if (role == UserRole.KHADEM && !canApprove) {
                    val khademStageId = authorizationService.getKhademStageId()
                    val khademYearId = authorizationService.getKhademYearId()
                    val respStageIds = authorizationService.getResponsibleStageIds()
                    val respYearIds = authorizationService.getResponsibleYearIds()

                    val allStages = lookupRepository.getEducationalStages(PageQuery(page = 0, size = 100)).data

                    val allowedStages = allStages.mapNotNull { stage ->
                        val isDirectStage = (stage.id == khademStageId) || respStageIds.contains(stage.id)
                        if (isDirectStage) {
                            if (stage.id == khademStageId && !respStageIds.contains(stage.id) && khademYearId != null) {
                                val allowedYears = stage.subItems.filter { it.id == khademYearId || respYearIds.contains(it.id) }
                                stage.copy(subItems = allowedYears)
                            } else {
                                stage
                            }
                        } else {
                            val allowedYears = stage.subItems.filter { respYearIds.contains(it.id) }
                            if (allowedYears.isNotEmpty()) {
                                stage.copy(subItems = allowedYears)
                            } else null
                        }
                    }

                    if (allowedStages.size == 1) {
                        val singleStage = allowedStages.first()
                        val availableYears = singleStage.subItems
                        if (availableYears.size == 1) {
                            val singleYear = availableYears.first()
                            updateState {
                                it.copy(
                                    selectedStage = singleStage,
                                    selectedYear = singleYear,
                                    stages = allowedStages,
                                    years = availableYears,
                                    isStageFilterLocked = true,
                                    isYearFilterLocked = true
                                )
                            }
                        } else {
                            updateState {
                                it.copy(
                                    selectedStage = singleStage,
                                    selectedYear = null,
                                    stages = allowedStages,
                                    years = availableYears,
                                    isStageFilterLocked = true,
                                    isYearFilterLocked = false
                                )
                            }
                        }
                    } else if (allowedStages.isNotEmpty()) {
                        updateState {
                            it.copy(
                                selectedStage = null,
                                selectedYear = null,
                                stages = allowedStages,
                                years = emptyList(),
                                isStageFilterLocked = false,
                                isYearFilterLocked = false
                            )
                        }
                    } else {
                        loadStages()
                    }
                } else {
                    loadStages()
                }
            },
            onSuccess = {
                loadUsers()
            },
            onError = { throwable ->
                showSnackBar(
                    title = UiText.StringRes(Res.string.failed_to_load_educational_stages),
                    message = getLocalizedErrorMessage(throwable),
                    isSuccess = false
                )
                loadUsers()
            }
        )
    }

    private fun loadUsers() {
        launch {
            usersPaginator.reset()
        }
    }

    private fun loadStages() {
        launch {
            stagesPaginator.loadNextItems()
        }
    }

    override fun onSearchQueryChanged(query: String) {
        updateState { it.copy(searchQuery = query) }
        loadUsers()
    }

    override fun onRoleFilterSelected(role: UserRole?) {
        updateState { it.copy(selectedRole = role) }
        loadUsers()
    }

    override fun onStageFilterSelected(stage: LookupResponse?) {
        if (state.value.isStageFilterLocked) return
        val years = stage?.subItems ?: emptyList()
        updateState { it.copy(selectedStage = stage, selectedYear = null, years = years) }
        loadUsers()
    }

    override fun onYearFilterSelected(year: LookupResponse?) {
        if (state.value.isYearFilterLocked) return
        updateState { it.copy(selectedYear = year) }
        loadUsers()
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
        loadUsers()
    }

    override fun onUserClicked(user: ProfileResponse) {
        navigate(EditUserRoute(userId = user.id))
    }

    override fun onLoadMore() {
        if (state.value.hasMorePages && !state.value.isLoading) {
            launch {
                usersPaginator.loadNextItems()
            }
        }
    }

    override fun onRefresh() {
        updateState { it.copy(isRefreshing = true) }
        launch {
            stagesPaginator.reset()
        }
        initializeFiltersAndLoadUsers()
    }

    override fun onClickBack() {
        popBackStack()
    }
}
