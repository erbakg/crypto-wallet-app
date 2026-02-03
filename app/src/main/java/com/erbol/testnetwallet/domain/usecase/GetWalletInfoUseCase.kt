package com.erbol.testnetwallet.domain.usecase

import com.erbol.testnetwallet.domain.model.WalletInfo
import com.erbol.testnetwallet.domain.repository.WalletRepository
import javax.inject.Inject

/**
 * Use case for getting wallet information.
 */
class GetWalletInfoUseCase @Inject constructor(
    private val walletRepository: WalletRepository
) {
    /**
     * Execute the use case.
     * @return Result wrapping WalletInfo or exception
     */
    suspend operator fun invoke(): Result<WalletInfo> {
        return runCatching {
            walletRepository.getWalletInfo()
        }
    }
}
