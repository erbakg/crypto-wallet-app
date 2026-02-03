package com.erbol.testnetwallet.domain.usecase

import com.erbol.testnetwallet.common.toBigDecimalOrNull
import com.erbol.testnetwallet.domain.model.TransactionResult
import com.erbol.testnetwallet.domain.repository.WalletRepository
import java.math.BigDecimal
import javax.inject.Inject

/**
 * Use case for sending ETH transaction.
 */
class SendTransactionUseCase @Inject constructor(
    private val walletRepository: WalletRepository
) {
    /**
     * Execute the use case.
     * @param toAddress Recipient address
     * @param amountEth Amount in ETH as string
     * @return Result wrapping TransactionResult or exception
     */
    suspend operator fun invoke(toAddress: String, amountEth: String): Result<TransactionResult> {
        return runCatching {
            // Validate address
            require(walletRepository.isValidAddress(toAddress)) {
                "Invalid recipient address format"
            }

            // Parse and validate amount
            val amount = amountEth.toBigDecimalOrNull()
                ?: throw IllegalArgumentException("Invalid amount format")

            require(amount > BigDecimal.ZERO) {
                "Amount must be greater than 0"
            }

            walletRepository.sendTransaction(toAddress, amount)
        }
    }
}
