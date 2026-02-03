package com.erbol.testnetwallet.domain.model

/**
 * Domain model representing transaction result.
 * No Android dependencies - pure Kotlin.
 */
data class TransactionResult(
    val transactionHash: String,
    val explorerUrl: String
)
