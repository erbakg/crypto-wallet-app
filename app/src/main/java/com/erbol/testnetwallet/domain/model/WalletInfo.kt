package com.erbol.testnetwallet.domain.model

import java.math.BigDecimal

/**
 * Domain model representing wallet information.
 * No Android dependencies - pure Kotlin.
 */
data class WalletInfo(
    val address: String,
    val network: String,
    val chainId: Long,
    val balanceEth: BigDecimal
)
