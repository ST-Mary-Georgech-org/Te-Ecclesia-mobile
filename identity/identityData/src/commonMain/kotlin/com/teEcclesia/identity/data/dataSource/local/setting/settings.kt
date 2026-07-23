package com.teEcclesia.identity.data.dataSource.local.setting

import com.russhwolf.settings.Settings
import com.teEcclesia.identity.domain.util.AppTheme

internal var Settings.accessToken: String
    get() = getString(ACCESS_TOKEN, "")
    set(value) = putString(ACCESS_TOKEN, value)

internal var Settings.refreshToken: String
    get() = getString(REFRESH_TOKEN, "")
    set(value) = putString(REFRESH_TOKEN, value)

internal var Settings.appLanguage: String
    get() = getString(APP_LANGUAGE, "")
    set(value) = putString(APP_LANGUAGE, value)

internal var Settings.appTheme: String
    get() = getString(APP_THEME, AppTheme.SYSTEM.name)
    set(value) = putString(APP_THEME, value)

internal var Settings.userStatus: String
    get() = getString(USER_STATUS, "")
    set(value) = putString(USER_STATUS, value)

internal var Settings.userRole: String
    get() = getString(USER_ROLE, "")
    set(value) = putString(USER_ROLE, value)

internal var Settings.canApproveRequests: Boolean
    get() = getBoolean(CAN_APPROVE_REQUESTS, false)
    set(value) = putBoolean(CAN_APPROVE_REQUESTS, value)

const val ACCESS_TOKEN = "access_token"
const val REFRESH_TOKEN = "refresh_token"
const val USER_STATUS = "user_status"
const val APP_LANGUAGE = "app_language"
const val APP_THEME = "app_theme"
const val USER_ROLE = "user_role"
const val CAN_APPROVE_REQUESTS = "can_approve_requests"
