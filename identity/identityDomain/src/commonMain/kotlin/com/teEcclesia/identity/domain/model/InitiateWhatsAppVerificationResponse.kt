package com.teEcclesia.identity.domain.model

data class InitiateWhatsAppVerificationResponse(
    val deepLink: String,
    val token: String
)
