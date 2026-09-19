package com.teEcclesia.identity.presentation.screen.reviewDeletionRequest

import com.teEcclesia.designsystem.components.button.AppButtonState
import com.teEcclesia.designsystem.navigation.BaseViewModel
import com.teEcclesia.designsystem.utils.UiText
import com.teEcclesia.identity.api.ReviewDeletionRequestRoute
import com.teEcclesia.identity.domain.repository.ProfileRepository
import com.teEcclesia.identity.presentation.screen.deletionRequests.DeletionRequestsViewModel.Companion.KEY_DELETION_REQUEST_HANDLED
import com.teEcclesia.shared.domain.model.UserRole
import com.teEcclesia.designsystem.util.extentions.format
import com.teEcclesia.shared.domain.utils.toLocalDateTimeOrDefault
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.account_restored_successfully
import teecclesia.designsystem.generated.resources.deletion_approved_successfully
import teecclesia.designsystem.generated.resources.failed_to_approve_deletion
import teecclesia.designsystem.generated.resources.failed_to_restore_account

class ReviewDeletionRequestViewModel(
    route: ReviewDeletionRequestRoute,
    private val profileRepository: ProfileRepository
) : BaseViewModel<ReviewDeletionRequestUiState>(
    ReviewDeletionRequestUiState(
        requestId = route.requestId,
        userId = route.userId,
        userName = route.userName,
        userCode = route.userCode,
        userImageUrl = route.userImageUrl,
        userRole = UserRole.fromStringOrDefault(route.userRole),
        reason = route.reason,
        requestedAt = route.requestedAt.toLocalDateTimeOrDefault().format()
    )
), ReviewDeletionRequestInteractionListener {

    override fun onApproveDeletion() {
        val requestId = state.value.requestId
        if (requestId.isBlank()) return

        tryToCall(
            onStart = { updateState { copy(approveButtonState = AppButtonState.Loading) } },
            block = { profileRepository.approveDeletion(requestId) },
            onSuccess = {
                updateState { copy(approveButtonState = AppButtonState.Enabled) }
                showSnackBar(
                    title = UiText.StringRes(Res.string.deletion_approved_successfully),
                    isSuccess = true
                )
                popBackStack(KEY_DELETION_REQUEST_HANDLED to true)
            },
            onError = {
                updateState { copy(approveButtonState = AppButtonState.Enabled) }
                showSnackBar(
                    title = UiText.StringRes(Res.string.failed_to_approve_deletion),
                    isSuccess = false
                )
            }
        )
    }

    override fun onRejectDeletion() {
        val requestId = state.value.requestId
        if (requestId.isBlank()) return

        tryToCall(
            onStart = { updateState { copy(rejectButtonState = AppButtonState.Loading) } },
            block = { profileRepository.rejectDeletion(requestId) },
            onSuccess = {
                updateState { copy(rejectButtonState = AppButtonState.Enabled) }
                showSnackBar(
                    title = UiText.StringRes(Res.string.account_restored_successfully),
                    isSuccess = true
                )
                popBackStack(KEY_DELETION_REQUEST_HANDLED to true)
            },
            onError = {
                updateState { copy(rejectButtonState = AppButtonState.Enabled) }
                showSnackBar(
                    title = UiText.StringRes(Res.string.failed_to_restore_account),
                    isSuccess = false
                )
            }
        )
    }

    override fun onClickBack() {
        popBackStack()
    }
}
