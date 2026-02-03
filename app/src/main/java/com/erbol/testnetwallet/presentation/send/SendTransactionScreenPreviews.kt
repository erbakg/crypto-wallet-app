package com.erbol.testnetwallet.presentation.send

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.erbol.testnetwallet.common.UiState
import com.erbol.testnetwallet.domain.model.TransactionResult
import com.erbol.testnetwallet.ui.theme.TestnetWalletTheme

@Preview(showBackground = true)
@Composable
private fun SendTransactionContentPreview() {
    TestnetWalletTheme {
        SendTransactionContent(
            uiState = UiState.Idle,
            formState = SendTransactionFormState(
                recipientAddress = "0x742d355cc634C0032925a3b844Bc9e755595f0bDd7",
                amountEth = "0.001"
            ),
            onNavigateBack = {},
            onRecipientChanged = {},
            onAmountChanged = {},
            onSendClick = {},
            onCopyHash = {},
            onViewOnEtherscan = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SendTransactionSuccessPreview() {
    TestnetWalletTheme {
        SendTransactionContent(
            uiState = UiState.Success(
                TransactionResult(
                    transactionHash = "0xabc123def456789012345678901234567890123456789012345678901234def456",
                    explorerUrl = "https://sepolia.etherscan.io/tx/0xabc123"
                )
            ),
            formState = SendTransactionFormState(),
            onNavigateBack = {},
            onRecipientChanged = {},
            onAmountChanged = {},
            onSendClick = {},
            onCopyHash = {},
            onViewOnEtherscan = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SendTransactionLoadingPreview() {
    TestnetWalletTheme {
        SendTransactionContent(
            uiState = UiState.Loading,
            formState = SendTransactionFormState(
                recipientAddress = "0x742d355cc634C0032925a3b844Bc9e755595f0bDd7",
                amountEth = "0.001"
            ),
            onNavigateBack = {},
            onRecipientChanged = {},
            onAmountChanged = {},
            onSendClick = {},
            onCopyHash = {},
            onViewOnEtherscan = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SendTransactionErrorPreview() {
    TestnetWalletTheme {
        SendTransactionContent(
            uiState = UiState.Error("Insufficient balance"),
            formState = SendTransactionFormState(
                recipientAddress = "0x742d355cc634C0032925a3b844Bc9e755595f0bDd7",
                amountEth = "0.001"
            ),
            onNavigateBack = {},
            onRecipientChanged = {},
            onAmountChanged = {},
            onSendClick = {},
            onCopyHash = {},
            onViewOnEtherscan = {}
        )
    }
}
