package com.teEcclesia.identity.presentation.screen.requests

import com.teEcclesia.identity.domain.model.UserRole

interface RegistrationRequestsInteractionListener {
    fun onSearchQueryChanged(query: String)
    fun onRoleFilterSelected(role: UserRole?)
    fun onSortByChanged(sortBy: String, sortOrder: String)
    fun onToggleSortingSheet(isVisible: Boolean)
    fun onLoadMore()
    fun onRefresh()
    fun onApproveUser(userId: String)
    fun onRejectUser(userId: String, reason: String)
    fun onRequestClicked(userId: String)
    fun onClickBack()
}

