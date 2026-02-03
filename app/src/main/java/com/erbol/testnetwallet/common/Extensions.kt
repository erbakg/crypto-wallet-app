package com.erbol.testnetwallet.common

import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode

/**
 * Extension functions used across the application.
 * No Android dependencies - pure Kotlin.
 */

/**
 * Converts Wei to ETH
 */
fun BigInteger.weiToEth(): BigDecimal {
    return BigDecimal(this).divide(BigDecimal.TEN.pow(18), 18, RoundingMode.DOWN)
}

/**
 * Converts ETH to Wei
 */
fun BigDecimal.ethToWei(): BigInteger {
    return this.multiply(BigDecimal.TEN.pow(18)).toBigInteger()
}

/**
 * Formats ETH balance for display
 */
fun BigDecimal.formatEthBalance(decimals: Int = Constants.BALANCE_DECIMAL_PLACES): String {
    return this.setScale(decimals, RoundingMode.DOWN)
        .stripTrailingZeros()
        .toPlainString()
}

/**
 * Truncates Ethereum address for display (0x1234...5678)
 */
fun String.truncateAddress(prefixLength: Int = 6, suffixLength: Int = 4): String {
    return if (this.length > prefixLength + suffixLength + 3) {
        "${take(prefixLength)}...${takeLast(suffixLength)}"
    } else {
        this
    }
}

/**
 * Validates Ethereum address format
 */
fun String.isValidEthAddress(): Boolean {
    return matches(Regex(Constants.ETH_ADDRESS_PATTERN))
}

/**
 * Validates email format
 */
fun String.isValidEmail(): Boolean {
    return matches(Regex(Constants.EMAIL_PATTERN))
}

/**
 * Parses string to BigDecimal safely
 */
fun String.toBigDecimalOrNull(): BigDecimal? {
    return try {
        if (isBlank()) null else BigDecimal(this)
    } catch (e: NumberFormatException) {
        null
    }
}
