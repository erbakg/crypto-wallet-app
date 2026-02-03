package com.erbol.testnetwallet.domain.model

/**
 * Domain model representing authentication result.
 * No Android dependencies - pure Kotlin.
 */
data class AuthResult(
    val isAuthenticated: Boolean,
    val walletAddress: String?
)

/**
 * Domain model representing OTP send result.
 */
data class OtpSendResult(
    val verificationId: String
)
