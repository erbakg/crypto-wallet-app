package com.erbol.testnetwallet.domain.usecase

import com.erbol.testnetwallet.domain.model.OtpSendResult
import com.erbol.testnetwallet.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * Use case for sending OTP to user's email.
 */
class SendOtpUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    /**
     * Execute the use case.
     * @param email User's email address
     * @return Result wrapping OtpSendResult or exception
     */
    suspend operator fun invoke(email: String): Result<OtpSendResult> {
        return runCatching {
            require(email.isNotBlank()) { "Email cannot be empty" }
            authRepository.sendOtp(email)
        }
    }
}
