package com.erbol.testnetwallet.data.repository

import com.erbol.testnetwallet.common.Constants
import com.erbol.testnetwallet.data.local.SessionStorage
import com.erbol.testnetwallet.data.remote.Web3Service
import com.erbol.testnetwallet.domain.model.TransactionResult
import com.erbol.testnetwallet.domain.model.WalletInfo
import com.erbol.testnetwallet.domain.repository.WalletRepository
import java.math.BigDecimal
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of WalletRepository.
 * Handles all wallet-related operations using Web3j.
 */
@Singleton
class WalletRepositoryImpl @Inject constructor(
    private val sessionStorage: SessionStorage,
    private val web3Service: Web3Service
) : WalletRepository {

    override suspend fun getWalletInfo(): WalletInfo {
        val address = sessionStorage.getWalletAddress()
            ?: throw IllegalStateException("No wallet found. Please login first.")

        val balance = web3Service.getBalance(address)

        return WalletInfo(
            address = address,
            network = Constants.SEPOLIA_CHAIN_NAME,
            chainId = Constants.SEPOLIA_CHAIN_ID,
            balanceEth = balance
        )
    }

    override suspend fun getBalance(): BigDecimal {
        val address = sessionStorage.getWalletAddress()
            ?: throw IllegalStateException("No wallet found")

        return web3Service.getBalance(address)
    }

    override suspend fun getWalletAddress(): String? {
        return sessionStorage.getWalletAddress()
    }

    override suspend fun sendTransaction(
        toAddress: String,
        amountEth: BigDecimal
    ): TransactionResult {
        val privateKey = sessionStorage.getPrivateKey()
            ?: throw IllegalStateException("No wallet credentials found")

        // Validate address
        if (!isValidAddress(toAddress)) {
            throw IllegalArgumentException("Invalid recipient address")
        }

        // Validate amount
        if (amountEth <= BigDecimal.ZERO) {
            throw IllegalArgumentException("Amount must be greater than 0")
        }

        // Check balance
        val currentBalance = getBalance()
        if (currentBalance < amountEth) {
            throw IllegalArgumentException(
                "Insufficient balance. Available: $currentBalance ETH"
            )
        }

        // Send transaction
        val txHash = try {
            web3Service.sendTransaction(privateKey, toAddress, amountEth)
        } catch (e: Exception) {
            throw mapTransactionError(e)
        }

        return TransactionResult(
            transactionHash = txHash,
            explorerUrl = getExplorerUrl(txHash)
        )
    }

    override fun isValidAddress(address: String): Boolean {
        return web3Service.isValidAddress(address)
    }

    override fun getExplorerUrl(txHash: String): String {
        return "${Constants.SEPOLIA_EXPLORER_URL}/tx/$txHash"
    }

    private fun mapTransactionError(e: Exception): Exception {
        val message = e.message ?: "Transaction failed"
        return when {
            message.contains("insufficient funds", ignoreCase = true) ->
                IllegalStateException("Insufficient funds for transaction and gas fees")

            message.contains("nonce too low", ignoreCase = true) ->
                IllegalStateException("Transaction conflict. Please try again.")

            message.contains("gas", ignoreCase = true) ->
                IllegalStateException("Gas estimation failed. Please try again.")

            else -> IllegalStateException("Transaction failed: $message")
        }
    }
}
