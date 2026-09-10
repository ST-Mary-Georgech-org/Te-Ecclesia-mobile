package com.teEcclesia.identity.presentation.screen.usersSearch

import com.teEcclesia.identity.domain.model.ProfileResponse
import com.teEcclesia.shared.domain.model.UserRole
import com.teEcclesia.lookups.domain.model.LookupResponse

interface UsersSearchInteractionListener {
    fun onSearchQueryChanged(query: String)
    fun onRoleFilterSelected(role: UserRole?)
    fun onStageFilterSelected(stage: LookupResponse?)
    fun onYearFilterSelected(year: LookupResponse?)
    fun onToggleFilterSheet(visible: Boolean)
    fun onResetFilters()
    fun onUserClicked(user: ProfileResponse)
    fun onLoadMore()
    fun onRetryLoadStages()
    fun onRefresh()
    fun onClickBack()
}
