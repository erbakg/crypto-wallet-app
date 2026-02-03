package com.erbol.testnetwallet.data.remote

import com.erbol.testnetwallet.common.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.web3j.crypto.Credentials
import org.web3j.crypto.Keys
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.http.HttpService
import org.web3j.tx.Transfer
import org.web3j.utils.Convert
import java.math.BigDecimal
import java.math.BigInteger
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Service for Web3/Ethereum operations using Web3j library.
 * Handles all blockchain interactions for Sepolia testnet.
 */
@Singleton
class Web3Service @Inject constructor() {

    private val web3j: Web3j by lazy {
        Web3j.build(HttpService(Constants.SEPOLIA_RPC_URL))
    }

    /**
     * Generate a new Ethereum keypair.
     * @return Pair of (address, privateKey)
     */
    suspend fun generateWallet(): Pair<String, String> = withContext(Dispatchers.IO) {
        val ecKeyPair = Keys.createEcKeyPair()
        val privateKey = ecKeyPair.privateKey.toString(16).padStart(64, '0')
        val address = "0x${Keys.getAddress(ecKeyPair)}"
        Pair(address, privateKey)
    }

    /**
     * Get ETH balance for an address.
     * @param address Ethereum address
     * @return Balance in ETH
     */
    suspend fun getBalance(address: String): BigDecimal = withContext(Dispatchers.IO) {
        val balanceWei = web3j
            .ethGetBalance(address, DefaultBlockParameterName.LATEST)
            .send()
            .balance
        Convert.fromWei(BigDecimal(balanceWei), Convert.Unit.ETHER)
    }

    /**
     * Send ETH transaction.
     * @param privateKey Sender's private key
     * @param toAddress Recipient address
     * @param amountEth Amount in ETH
     * @return Transaction hash
     */
    suspend fun sendTransaction(
        privateKey: String,
        toAddress: String,
        amountEth: BigDecimal
    ): String = withContext(Dispatchers.IO) {
        val credentials = Credentials.create(privateKey)

        val transactionReceipt = Transfer.sendFunds(
            web3j,
            credentials,
            toAddress,
            amountEth,
            Convert.Unit.ETHER
        ).send()

        transactionReceipt.transactionHash
    }

    /**
     * Get current gas price.
     * @return Gas price in Wei
     */
    suspend fun getGasPrice(): BigInteger = withContext(Dispatchers.IO) {
        web3j.ethGasPrice().send().gasPrice
    }

    /**
     * Validate Ethereum address format.
     * @param address Address to validate
     * @return true if valid format
     */
    fun isValidAddress(address: String): Boolean {
        return address.matches(Regex(Constants.ETH_ADDRESS_PATTERN))
    }
}
