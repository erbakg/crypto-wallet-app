package com.erbol.testnetwallet.common

import com.erbol.testnetwallet.BuildConfig

/**
 * Application-wide constants.
 */
object Constants {
    // Sepolia Testnet Configuration
    const val SEPOLIA_CHAIN_ID = 11155111L
    const val SEPOLIA_CHAIN_NAME = "Sepolia"
    const val SEPOLIA_EXPLORER_URL = "https://sepolia.etherscan.io"

    // RPC URL - uses Infura if API key is provided, otherwise falls back to public RPC
    val SEPOLIA_RPC_URL: String
        get() = if (BuildConfig.INFURA_API_KEY.isNotEmpty()) {
            "https://sepolia.infura.io/v3/${BuildConfig.INFURA_API_KEY}"
        } else {
            "https://rpc.sepolia.org"
        }

    // Gas Configuration
    const val DEFAULT_GAS_LIMIT = 21000L
    const val DEFAULT_GAS_PRICE_GWEI = 20L

    // Validation Patterns
    const val ETH_ADDRESS_PATTERN = "^0x[a-fA-F0-9]{40}$"
    const val EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"

    // OTP Configuration
    const val OTP_LENGTH = 6
    const val OTP_RESEND_COOLDOWN_SECONDS = 60

    // Balance Display
    const val BALANCE_DECIMAL_PLACES = 8
}
