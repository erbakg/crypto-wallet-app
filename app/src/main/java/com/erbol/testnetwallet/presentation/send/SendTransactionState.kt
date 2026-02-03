package com.erbol.testnetwallet.presentation.send

import com.erbol.testnetwallet.domain.model.TransactionResult

/**
 * Form state for Send Transaction screen inputs.
 * Separated from UiState to follow the pattern where UiState handles async operations.
 */
data class SendTransactionFormState(
    val recipientAddress: String = "",
    val amountEth: String = "",
    val addressError: String? = null,
    val amountError: String? = null
) {
    val isAddressValid: Boolean
        get() = recipientAddress.matches(Regex("^0x[a-fA-F0-9]{40}$"))

    val isAmountValid: Boolean
        get() {
            val amount = amountEth.toBigDecimalOrNull() ?: return false
            return amount > java.math.BigDecimal.ZERO
        }

    val canSend: Boolean
        get() = isAddressValid && isAmountValid &&
                addressError == null && amountError == null
}

/**
 * Extension function for TransactionResult formatting.
 */
fun TransactionResult.truncatedHash(): String =
    if (transactionHash.length > 16) {
        "${transactionHash.take(10)}...${transactionHash.takeLast(6)}"
    } else {
        transactionHash
    }
