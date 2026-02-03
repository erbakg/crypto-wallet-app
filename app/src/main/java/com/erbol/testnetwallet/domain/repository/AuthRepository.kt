package com.erbol.testnetwallet.domain.repository

import com.erbol.testnetwallet.domain.model.AuthResult
import com.erbol.testnetwallet.domain.model.OtpSendResult
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for authentication operations.
 * Defined in domain layer, implemented in data layer.
 */
interface AuthRepository {

    /**
     * Send OTP to the provided email address.
     * @param email User's email address
     * @return OtpSendResult containing verification ID
     * @throws Exception on failure
     */
    suspend fun sendOtp(email: String): OtpSendResult

    /**
     * Verify OTP code and authenticate user.
     * @param code OTP code entered by user
     * @return AuthResult indicating success and wallet address
     * @throws Exception on failure
     */
    suspend fun verifyOtp(code: String): AuthResult

    /**
     * Resend OTP to the email.
     * @param email User's email address
     * @return OtpSendResult containing new verification ID
     * @throws Exception on failure
     */
    suspend fun resendOtp(email: String): OtpSendResult

    /**
     * Check if user is currently authenticated.
     * @return Flow emitting authentication status
     */
    fun isAuthenticated(): Flow<Boolean>

    /**
     * Logout current user and clear session.
     */
    suspend fun logout()
}
