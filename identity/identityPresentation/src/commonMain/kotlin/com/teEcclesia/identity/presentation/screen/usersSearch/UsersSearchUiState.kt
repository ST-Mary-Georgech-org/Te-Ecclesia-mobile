package com.teEcclesia.identity.presentation.screen.usersSearch

import com.teEcclesia.identity.domain.model.ProfileResponse
import com.teEcclesia.shared.domain.model.UserRole
import com.teEcclesia.lookups.domain.model.LookupResponse

data class UsersSearchUiState(
    val searchQuery: String = "",
    val selectedRole: UserRole? = null,
    val selectedStage: LookupResponse? = null,
    val selectedYear: LookupResponse? = null,
    val stages: List<LookupResponse> = emptyList(),
    val years: List<LookupResponse> = emptyList(),
    val roles: List<UserRole> = listOf(UserRole.MAKHDOOM, UserRole.KHADEM, UserRole.PARENT, UserRole.KAHEN),
    val users: List<ProfileResponse> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isFilterSheetVisible: Boolean = false,
    val isStageFilterLocked: Boolean = false,
    val isYearFilterLocked: Boolean = false,
    val hasMorePages: Boolean = true,
    val totalUsersCount: Long = 0L
) {
    val hasActiveFilters: Boolean
        get() = selectedRole != null || selectedStage != null || selectedYear != null
}

val ProfileResponse.educationalStageName: String?
    get() = when (role) {
        UserRole.KHADEM -> khademProfile?.educationalStage?.name
        UserRole.MAKHDOOM -> makhdoomProfile?.educationalStage?.name
        else -> khademProfile?.educationalStage?.name ?: makhdoomProfile?.educationalStage?.name
    }

val ProfileResponse.educationalYearName: String?
    get() = when (role) {
        UserRole.KHADEM -> khademProfile?.educationalYear?.name
        UserRole.MAKHDOOM -> makhdoomProfile?.educationalYear?.name
        else -> khademProfile?.educationalYear?.name ?: makhdoomProfile?.educationalYear?.name
    }

