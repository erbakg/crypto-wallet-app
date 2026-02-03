package com.erbol.testnetwallet.domain.usecase

import com.erbol.testnetwallet.common.Constants
import com.erbol.testnetwallet.domain.model.AuthResult
import com.erbol.testnetwallet.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * Use case for verifying OTP and authenticating user.
 */
class VerifyOtpUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    /**
     * Execute the use case.
     * @param code OTP code entered by user
     * @return Result wrapping AuthResult or exception
     */
    suspend operator fun invoke(code: String): Result<AuthResult> {
        return runCatching {
            require(code.length == Constants.OTP_LENGTH) {
                "OTP must be ${Constants.OTP_LENGTH} digits"
            }
            require(code.all { it.isDigit() }) { "OTP must contain only digits" }
            authRepository.verifyOtp(code)
        }
    }
}
