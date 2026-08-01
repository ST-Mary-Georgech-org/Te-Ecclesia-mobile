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

internal var Settings.khademStageId: Long
    get() = getLong(KHADEM_STAGE_ID, -1L)
    set(value) = putLong(KHADEM_STAGE_ID, value)

internal var Settings.khademYearId: Long
    get() = getLong(KHADEM_YEAR_ID, -1L)
    set(value) = putLong(KHADEM_YEAR_ID, value)

internal var Settings.responsibleStageIds: String
    get() = getString(RESPONSIBLE_STAGE_IDS, "")
    set(value) = putString(RESPONSIBLE_STAGE_IDS, value)

internal var Settings.responsibleYearIds: String
    get() = getString(RESPONSIBLE_YEAR_IDS, "")
    set(value) = putString(RESPONSIBLE_YEAR_IDS, value)

internal var Settings.cachedProfileJson: String
    get() = getString(CACHED_PROFILE_JSON, "")
    set(value) = putString(CACHED_PROFILE_JSON, value)


const val ACCESS_TOKEN = "access_token"
const val REFRESH_TOKEN = "refresh_token"
const val USER_STATUS = "user_status"
const val APP_LANGUAGE = "app_language"
const val APP_THEME = "app_theme"
const val USER_ROLE = "user_role"
const val CAN_APPROVE_REQUESTS = "can_approve_requests"
const val KHADEM_STAGE_ID = "khadem_stage_id"
const val KHADEM_YEAR_ID = "khadem_year_id"
const val RESPONSIBLE_STAGE_IDS = "responsible_stage_ids"
const val RESPONSIBLE_YEAR_IDS = "responsible_year_ids"
const val CACHED_PROFILE_JSON = "cached_profile_json"