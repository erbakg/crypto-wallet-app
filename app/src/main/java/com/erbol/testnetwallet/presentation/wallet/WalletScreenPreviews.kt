package com.erbol.testnetwallet.presentation.wallet

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.erbol.testnetwallet.common.UiState
import com.erbol.testnetwallet.domain.model.WalletInfo
import com.erbol.testnetwallet.ui.theme.TestnetWalletTheme
import java.math.BigDecimal

@Preview(showBackground = true)
@Composable
private fun WalletContentPreview() {
    TestnetWalletTheme {
        WalletContent(
            uiState = UiState.Success(
                WalletInfo(
                    address = "0x6E6F148c27fB49c9760371A9723d08C4d5Af8b4",
                    network = "Sepolia",
                    chainId = 11155111,
                    balanceEth = BigDecimal("1.18769409")
                )
            ),
            walletUiState = WalletUiState(),
            onRefresh = {},
            onCopyAddress = {},
            onSendTransaction = {},
            onLogout = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun WalletContentLoadingPreview() {
    TestnetWalletTheme {
        WalletContent(
            uiState = UiState.Loading,
            walletUiState = WalletUiState(),
            onRefresh = {},
            onCopyAddress = {},
            onSendTransaction = {},
            onLogout = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun WalletContentErrorPreview() {
    TestnetWalletTheme {
        WalletContent(
            uiState = UiState.Error("Failed to load balance"),
            walletUiState = WalletUiState(),
            onRefresh = {},
            onCopyAddress = {},
            onSendTransaction = {},
            onLogout = {}
        )
    }
}
