package com.teEcclesia.identity.data.dataSource.remote.dto.auth.request

import com.teEcclesia.identity.data.mapper.normalizeEgyptPhone
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.teEcclesia.identity.domain.model.RegisterRequest
import com.teEcclesia.identity.domain.model.UserRole

@Serializable
data class RegisterRequestDto(
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
    @SerialName("nationalId")
    val nationalId: String,
    @SerialName("phone")
    val phone: String,
    @SerialName("homePhone")
    val homePhone: String?,
    @SerialName("email")
    val email: String?,
    @SerialName("password")
    val password: String,
    @SerialName("imageUrl")
    val imageUrl: String?,
    @SerialName("job")
    val job: String?,
    @SerialName("buildingNo")
    val buildingNo: String,
    @SerialName("street")
    val street: String,
    @SerialName("streetBranch")
    val streetBranch: String?,
    @SerialName("area")
    val area: String,
    @SerialName("floor")
    val floor: String,
    @SerialName("apartment")
    val apartment: String?,
    @SerialName("specialMark")
    val specialMark: String,
    @SerialName("role")
    val role: UserRole?,
    @SerialName("confessionPriestId")
    val confessionPriestId: String?,
    @SerialName("externalConfessionPriestName")
    val externalConfessionPriestName: String?,
    @SerialName("externalConfessionChurch")
    val externalConfessionChurch: String?,
    @SerialName("externalConfessionPhone")
    val externalConfessionPhone: String?,
    @SerialName("ordinationProfile")
    val ordinationProfile: OrdinationProfileRequestDto?,
    @SerialName("makhdoomProfile")
    val makhdoomProfile: MakhdoomProfileRequestDto?,
    @SerialName("parentProfile")
    val parentProfile: ParentProfileRequestDto?
)

fun RegisterRequest.toDto() = RegisterRequestDto(
    firstName = firstName,
    secondName = secondName,
    thirdName = thirdName,
    lastName = lastName,
    displayName = displayName,
    nationalId = nationalId,
    phone = phone.normalizeEgyptPhone(),
    homePhone = homePhone,
    email = email,
    password = password,
    imageUrl = imageUrl,
    job = job,
    buildingNo = buildingNo,
    street = street,
    streetBranch = streetBranch,
    area = area,
    floor = floor,
    apartment = apartment,
    specialMark = specialMark,
    role = role,
    confessionPriestId = confessionPriestId,
    externalConfessionPriestName = externalConfessionPriestName,
    externalConfessionChurch = externalConfessionChurch,
    externalConfessionPhone = externalConfessionPhone,
    ordinationProfile = ordinationProfile?.toDto(),
    makhdoomProfile = makhdoomProfile?.toDto(),
    parentProfile = parentProfile?.toDto()
)
