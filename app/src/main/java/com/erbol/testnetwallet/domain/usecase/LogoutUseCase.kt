package com.erbol.testnetwallet.domain.usecase

import com.erbol.testnetwallet.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * Use case for logging out user.
 */
class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    /**
     * Execute the use case.
     * @return Result indicating success or failure
     */
    suspend operator fun invoke(): Result<Unit> {
        return runCatching {
            authRepository.logout()
        }
    }
}
