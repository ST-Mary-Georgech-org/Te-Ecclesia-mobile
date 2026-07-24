package com.teEcclesia.identity.presentation.screen.requests.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.teEcclesia.designsystem.components.chips.SuggestionChip
import com.teEcclesia.designsystem.components.icon.Icon
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.modifier.clickableNoRipple
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.utils.Preview
import com.teEcclesia.identity.domain.model.Gender
import com.teEcclesia.identity.domain.model.ProfileResponse
import com.teEcclesia.identity.domain.model.ShamamsaStudyStatus
import com.teEcclesia.identity.domain.model.UserRole
import com.teEcclesia.identity.domain.model.UserStatus
import com.teEcclesia.identity.presentation.screen.requests.toHistory
import com.teEcclesia.identity.presentation.screen.requests.toText
import com.teEcclesia.lookups.domain.model.LookupResponse
import kotlinx.datetime.LocalDateTime
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.ic_arrow_right
import teecclesia.designsystem.generated.resources.ic_profile_image_placeholder

@Composable
fun RegistrationRequestCard(
    imageUrl: String?,
    fullName: String,
    requestDateTime: LocalDateTime,
    role: UserRole,
    stage: LookupResponse?,
    year: LookupResponse?,
    shamamsaStudyStatus: ShamamsaStudyStatus?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Theme.colorScheme.inverseOnSurface)
            .clickableNoRipple { onClick() }
            .border(1.dp, Theme.colorScheme.outline, RoundedCornerShape(16.dp))
            .padding(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (imageUrl == null) {
                Icon(
                    painter = painterResource(Res.drawable.ic_profile_image_placeholder),
                    contentDescription = null,
                    tint = Theme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Theme.colorScheme.secondaryContainer)
                        .border(1.dp, Theme.colorScheme.outline, CircleShape)
                        .padding(8.dp)
                )
            } else {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Theme.colorScheme.outline)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = fullName,
                    style = Theme.typography.bodyMedium,
                    color = Theme.colorScheme.onSecondaryContainer
                )

                Text(
                    text = requestDateTime.toString().replace("T", "  "),
                    style = Theme.typography.bodySmall,
                    color = Theme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.padding(top = 2.dp)
                )

                CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        SuggestionChip(
                            onClick = {},
                            label = {
                                Text(
                                    text = stringResource(role.toText()),
                                    style = Theme.typography.labelLarge,
                                    color = Theme.colorScheme.onSurfaceVariant
                                )
                            }
                        )
                        stage?.let {
                            SuggestionChip(
                                onClick = {},
                                label = {
                                    Text(
                                        text = stage.name,
                                        style = Theme.typography.labelLarge,
                                        color = Theme.colorScheme.onSurfaceVariant
                                    )
                                }
                            )
                        }

                        year?.let {
                            SuggestionChip(
                                onClick = {},
                                label = {
                                    Text(
                                        text = year.name,
                                        style = Theme.typography.labelLarge,
                                        color = Theme.colorScheme.onSurfaceVariant
                                    )
                                }
                            )
                        }

                        shamamsaStudyStatus?.let {
                            if (shamamsaStudyStatus != ShamamsaStudyStatus.NO) {
                                SuggestionChip(
                                    onClick = {},
                                    label = {
                                        Text(
                                            text = stringResource(shamamsaStudyStatus.toHistory()),
                                            style = Theme.typography.labelLarge,
                                            color = Theme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
        Icon(
            painter = painterResource(Res.drawable.ic_arrow_right),
            contentDescription = null,
            tint = Theme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(24.dp)
        )
    }
}

@PreviewLightDark
@Composable
private fun RegistrationRequestCardPreview() = Theme {
    val profile = ProfileResponse(
        id = "1",
        firstName = "Joseph",
        secondName = "Sameh",
        thirdName = "Fouad",
        lastName = "Nasr",
        displayName = "John Doe",
        fullName = "John Doe",
        nationalId = "29901010101234",
        phone = "01234567890",
        homePhone = "",
        email = "john@example.com",
        isEmailVerified = true,
        isPhoneVerified = true,
        imageUrl = null,
        job = "Engineer",
        buildingNo = "1",
        street = "Street",
        streetBranch = "",
        area = "Area",
        floor = "1",
        apartment = "1",
        specialMark = "",
        gender = Gender.MALE,
        status = UserStatus.PENDING_APPROVAL,
        statusReason = null,
        role = UserRole.MAKHDOOM,
        confessionPriest = null,
        externalConfessionPriestName = "",
        externalConfessionChurch = "",
        externalConfessionPhone = "",
        khademProfile = null,
        kahenProfile = null,
        parentProfile = null,
        ordinationProfile = null,
        makhdoomProfile = null
    )

    Preview(darkTheme = Theme.isDarkTheme) {
        RegistrationRequestCard(
            imageUrl = profile.imageUrl,
            fullName = profile.fullName,
            requestDateTime = LocalDateTime(2024, 6, 1, 12, 0),
            role = profile.role,
            stage = LookupResponse(
                id = 0L,
                name = "Primary",
                subItems = emptyList()
            ),
            year = LookupResponse(
                id = 0L,
                name = "2024",
                subItems = emptyList()
            ),
            shamamsaStudyStatus = ShamamsaStudyStatus.YES,
            onClick = {}
        )
    }
}