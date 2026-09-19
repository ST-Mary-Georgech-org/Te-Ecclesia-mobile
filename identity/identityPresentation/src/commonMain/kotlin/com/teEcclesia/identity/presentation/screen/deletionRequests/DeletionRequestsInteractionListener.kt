package com.teEcclesia.identity.presentation.screen.deletionRequests

import com.teEcclesia.identity.domain.model.AccountDeletionRequest

interface DeletionRequestsInteractionListener {
    fun onRefresh()
    fun onLoadMore()
    fun onRequestClicked(request: AccountDeletionRequest)
    fun onClickBack()
}
