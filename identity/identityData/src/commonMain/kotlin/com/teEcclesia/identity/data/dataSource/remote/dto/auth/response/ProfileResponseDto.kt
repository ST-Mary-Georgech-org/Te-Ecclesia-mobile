package com.teEcclesia.identity.data.dataSource.remote.dto.auth.response

import com.teEcclesia.shared.domain.utils.toLocalDateTimeOrDefault
import com.teEcclesia.identity.domain.model.ProfileResponse
import com.teEcclesia.identity.domain.model.Gender
import com.teEcclesia.identity.domain.model.UserStatus
import com.teEcclesia.shared.domain.model.UserRole
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

import com.teEcclesia.identity.data.dto.PriestDto
import com.teEcclesia.identity.data.dto.UserSummaryDto
import com.teEcclesia.identity.data.dto.toDomain
import com.teEcclesia.identity.domain.model.CachedProfile
import com.teEcclesia.shared.domain.utils.getNow

@Serializable
data class ProfileResponseDto(
    @SerialName("id")
    val id: String,
    @SerialName("code")
    val code: String? = null,
    @SerialName("firstName")
    val firstName: String,
    @SerialName("secondName")
    val secondName: String,
    @SerialName("thirdName")
    val thirdName: String,
    @SerialName("lastName")
    val lastName: String,
    @SerialName("displayName")
    val displayName: String,
    @SerialName("fullName")
    val fullName: String,
    @SerialName("nationalId")
    val nationalId: String? = null,
    @SerialName("phone")
    val phone: String,
    @SerialName("homePhone")
    val homePhone: String? = null,
    @SerialName("email")
    val email: String? = null,
    @SerialName("emailVerified")
    val isEmailVerified: Boolean = false,
    @SerialName("phoneVerified")
    val isPhoneVerified: Boolean = false,
    @SerialName("imageUrl")
    val imageUrl: String? = null,
    @SerialName("job")
    val job: String? = null,
    @SerialName("buildingNo")
    val buildingNo: String? = null,
    @SerialName("street")
    val street: String? = null,
    @SerialName("streetBranch")
    val streetBranch: String? = null,
    @SerialName("area")
    val area: String? = null,
    @SerialName("floor")
    val floor: String? = null,
    @SerialName("apartment")
    val apartment: String? = null,
    @SerialName("specialMark")
    val specialMark: String? = null,
    @SerialName("gender")
    val gender: Gender,
    @SerialName("status")
    val status: UserStatus,
    @SerialName("statusReason")
    val statusReason: String? = null,
    @SerialName("role")
    val role: UserRole,
    @SerialName("confessionPriest")
    val confessionPriest: PriestDto? = null,
    @SerialName("externalConfessionPriestName")
    val externalConfessionPriestName: String? = null,
    @SerialName("externalConfessionChurch")
    val externalConfessionChurch: String? = null,
    @SerialName("externalConfessionPhone")
    val externalConfessionPhone: String? = null,
    @SerialName("khademProfile")
    val khademProfile: KhademProfileResponseDto? = null,
    @SerialName("kahenProfile")
    val kahenProfile: KahenProfileResponseDto? = null,
    @SerialName("parentProfile")
    val parentProfile: ParentProfileResponseDto? = null,
    @SerialName("ordinationProfile")
    val ordinationProfile: OrdinationProfileResponseDto? = null,
    @SerialName("makhdoomProfile")
    val makhdoomProfile: MakhdoomProfileResponseDto? = null,
    @SerialName("createdAt")
    val createdAt: String? = null,
    @SerialName("actionTakenAt")
    val actionTakenAt: String? = null,
    @SerialName("actionTakenBy")
    val actionTakenBy: UserSummaryDto? = null,
    @SerialName("deaconsSchoolRecord")
    val deaconsSchoolRecord: DeaconsSchoolRecordDto? = null
)

fun ProfileResponseDto.toDomain() = ProfileResponse(
    id = id,
    code = code.orEmpty(),
    firstName = firstName,
    secondName = secondName,
    thirdName = thirdName,
    lastName = lastName,
    displayName = displayName,
    fullName = fullName,
    nationalId = nationalId.orEmpty(),
    phone = phone,
    homePhone = homePhone.orEmpty(),
    email = email.orEmpty(),
    isEmailVerified = isEmailVerified,
    isPhoneVerified = isPhoneVerified,
    imageUrl = imageUrl,
    job = job.orEmpty(),
    buildingNo = buildingNo.orEmpty(),
    street = street.orEmpty(),
    streetBranch = streetBranch.orEmpty(),
    area = area.orEmpty(),
    floor = floor.orEmpty(),
    apartment = apartment.orEmpty(),
    specialMark = specialMark.orEmpty(),
    gender = gender,
    status = status,
    statusReason = statusReason,
    role = role,
    confessionPriest = confessionPriest?.toDomain(),
    externalConfessionPriestName = externalConfessionPriestName.orEmpty(),
    externalConfessionChurch = externalConfessionChurch.orEmpty(),
    externalConfessionPhone = externalConfessionPhone.orEmpty(),
    khademProfile = khademProfile?.toDomain(),
    kahenProfile = kahenProfile?.toDomain(),
    parentProfile = parentProfile?.toDomain(),
    ordinationProfile = ordinationProfile?.toDomain(),
    makhdoomProfile = makhdoomProfile?.toDomain(),
    createdAt = createdAt?.toLocalDateTimeOrDefault() ?: getNow(),
    actionTakenAt = actionTakenAt,
    actionTakenBy = actionTakenBy?.toDomain(),
    deaconsSchoolRecord = deaconsSchoolRecord?.toDomain()
)

fun ProfileResponseDto.toCachedProfile() = CachedProfile(
    role = role,
    fullName = fullName,
    displayName = displayName,
    code = code.orEmpty(),
    imageUrl = imageUrl,
    whatsAppLink = when (role) {
        UserRole.MAKHDOOM -> makhdoomProfile?.educationalYear?.whatsAppLink
        UserRole.KHADEM -> null
        UserRole.ADMIN -> null
        UserRole.PARENT -> parentProfile?.whatsAppLink
        UserRole.GUEST -> null
        UserRole.KAHEN -> null
    }
)