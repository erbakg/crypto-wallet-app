package com.erbol.testnetwallet.domain.usecase

import com.erbol.testnetwallet.domain.repository.WalletRepository
import java.math.BigDecimal
import javax.inject.Inject

/**
 * Use case for refreshing wallet balance.
 */
class RefreshBalanceUseCase @Inject constructor(
    private val walletRepository: WalletRepository
) {
    /**
     * Execute the use case.
     * @return Result wrapping balance in ETH or exception
     */
    suspend operator fun invoke(): Result<BigDecimal> {
        return runCatching {
            walletRepository.getBalance()
        }
    }
}
