package com.erbol.testnetwallet.presentation.wallet

import com.erbol.testnetwallet.domain.model.WalletInfo
import java.math.BigDecimal

/**
 * UI-specific state for Wallet screen interactions.
 * Separated from async data loading state (UiState<WalletInfo>).
 */
data class WalletUiState(
    val isRefreshing: Boolean = false,
    val isAddressCopied: Boolean = false,
    val isLoggedOut: Boolean = false
)

/**
 * Extension functions for WalletInfo formatting.
 */
fun WalletInfo.formattedBalance(): String =
    balanceEth
        .setScale(8, java.math.RoundingMode.DOWN)
        .stripTrailingZeros()
        .toPlainString()

fun WalletInfo.truncatedAddress(): String =
    if (address.length > 10) {
        "${address.take(6)}...${address.takeLast(4)}"
    } else {
        address
    }

fun WalletInfo.networkDisplay(): String = "$network - $chainId"
