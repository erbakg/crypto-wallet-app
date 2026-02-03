package com.erbol.testnetwallet.domain.repository

import com.erbol.testnetwallet.domain.model.TransactionResult
import com.erbol.testnetwallet.domain.model.WalletInfo
import java.math.BigDecimal

/**
 * Repository interface for wallet operations.
 * Defined in domain layer, implemented in data layer.
 */
interface WalletRepository {

    /**
     * Get current wallet information including address, network, and balance.
     * @return WalletInfo containing wallet details
     * @throws Exception if no wallet found or network error
     */
    suspend fun getWalletInfo(): WalletInfo

    /**
     * Refresh and get current ETH balance.
     * @return Current balance in ETH
     * @throws Exception on network error
     */
    suspend fun getBalance(): BigDecimal

    /**
     * Get the wallet address.
     * @return Wallet address or null if not available
     */
    suspend fun getWalletAddress(): String?

    /**
     * Send ETH transaction.
     * @param toAddress Recipient address
     * @param amountEth Amount in ETH
     * @return TransactionResult with hash and explorer URL
     * @throws Exception on failure
     */
    suspend fun sendTransaction(toAddress: String, amountEth: BigDecimal): TransactionResult

    /**
     * Validate if address format is correct.
     * @param address Ethereum address to validate
     * @return true if valid format
     */
    fun isValidAddress(address: String): Boolean

    /**
     * Generate transaction explorer URL.
     * @param txHash Transaction hash
     * @return Full explorer URL
     */
    fun getExplorerUrl(txHash: String): String
}
