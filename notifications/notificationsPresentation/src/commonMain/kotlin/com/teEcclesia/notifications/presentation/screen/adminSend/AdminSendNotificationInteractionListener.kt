package com.teEcclesia.notifications.presentation.screen.adminSend

import com.teEcclesia.identity.domain.model.attendance.AttendeeUserPreview
import com.teEcclesia.lookups.domain.model.LookupResponse
import com.teEcclesia.shared.domain.model.UserRole

interface AdminSendNotificationInteractionListener {
    fun onClickBack()
    fun onTargetModeSelected(mode: RecipientTargetMode)
    fun onTitleChanged(title: String)
    fun onBodyChanged(body: String)

    fun onUserSearchQueryChanged(query: String)
    fun onSelectSuggestedUser(user: AttendeeUserPreview)
    fun onRemoveSelectedUser(user: AttendeeUserPreview)
    fun onDismissSuggestionsDropdown()

    fun onToggleRoleDropdown()
    fun onSelectRole(role: UserRole?)
    fun onDismissRoleDropdown()

    fun onToggleStageSheet(isVisible: Boolean)
    fun onSelectStage(stage: LookupResponse?)
    fun onLoadNextStages()
    fun onRetryLoadStages()

    fun onTogglePayloadExpanded()
    fun onAddPayloadItem()
    fun onPayloadKeyChanged(id: String, key: String)
    fun onPayloadValueChanged(id: String, value: String)
    fun onRemovePayloadItem(id: String)

    fun onClickSend()
    fun onDismissSuccessBanner()
}
