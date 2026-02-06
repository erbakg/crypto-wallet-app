package com.erbol.testnetwallet.presentation.send

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dynamic.sdk.android.DynamicSDK
import com.dynamic.sdk.android.Chains.EVM.EthereumTransaction
import com.dynamic.sdk.android.Chains.EVM.convertEthToWei
import com.dynamic.sdk.android.Models.Network
import com.erbol.testnetwallet.common.Constants
import org.json.JSONObject
import com.erbol.testnetwallet.common.UiState
import com.erbol.testnetwallet.domain.model.TransactionResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.BigInteger
import javax.inject.Inject

@HiltViewModel
class SendTransactionViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<TransactionResult>>(UiState.Idle)
    val uiState: StateFlow<UiState<TransactionResult>> = _uiState.asStateFlow()

    private val _formState = MutableStateFlow(SendTransactionFormState())
    val formState: StateFlow<SendTransactionFormState> = _formState.asStateFlow()

    private val sdk: DynamicSDK
        get() = DynamicSDK.getInstance()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _uiState.value = UiState.Error(mapErrorMessage(throwable.message))
    }

    fun onRecipientChanged(address: String) {
        _formState.update {
            it.copy(
                recipientAddress = address,
                addressError = null
            )
        }
        if (_uiState.value is UiState.Error) {
            _uiState.value = UiState.Idle
        }
        validateAddress(address)
    }

    fun onAmountChanged(amount: String) {
        // Only allow valid decimal input
        if (amount.isEmpty() || amount.matches(Regex("^\\d*\\.?\\d*$"))) {
            _formState.update {
                it.copy(
                    amountEth = amount,
                    amountError = null
                )
            }
            if (_uiState.value is UiState.Error) {
                _uiState.value = UiState.Idle
            }
            validateAmount(amount)
        }
    }

    private fun validateAddress(address: String) {
        if (address.isNotEmpty() && !isValidAddress(address)) {
            _formState.update { it.copy(addressError = "Invalid Ethereum address") }
        }
    }

    private fun validateAmount(amount: String) {
        if (amount.isEmpty()) return

        val amountDecimal = amount.toBigDecimalOrNull()
        when {
            amountDecimal == null -> {
                _formState.update { it.copy(amountError = "Invalid amount format") }
            }

            amountDecimal <= BigDecimal.ZERO -> {
                _formState.update { it.copy(amountError = "Amount must be greater than 0") }
            }
        }
    }

    private fun isValidAddress(address: String): Boolean {
        return address.matches(Regex("^0x[a-fA-F0-9]{40}$"))
    }

    fun sendTransaction() {
        val address = _formState.value.recipientAddress.trim()
        val amount = _formState.value.amountEth.trim()

        // Final validation
        if (!isValidAddress(address)) {
            _formState.update { it.copy(addressError = "Invalid Ethereum address") }
            return
        }

        val amountDecimal = amount.toBigDecimalOrNull()
        if (amountDecimal == null || amountDecimal <= BigDecimal.ZERO) {
            _formState.update { it.copy(amountError = "Invalid amount") }
            return
        }

        viewModelScope.launch(exceptionHandler) {
            _uiState.value = UiState.Loading

            try {
                val wallet = sdk.wallets.userWallets.firstOrNull {
                    it.chain.uppercase() == "EVM"
                } ?: throw IllegalStateException("No EVM wallet found")

                // Switch to Sepolia network
                sdk.wallets.switchNetwork(wallet, Network.evm(Constants.SEPOLIA_CHAIN_ID.toInt()))

                // Get gas prices
                val client = sdk.evm.createPublicClient(Constants.SEPOLIA_CHAIN_ID.toInt())
                val gasPrice = client.getGasPrice()
                val maxFeePerGas = gasPrice * BigInteger.valueOf(2)
                val maxPriorityFeePerGas = gasPrice

                // Create transaction
                val transaction = EthereumTransaction(
                    from = wallet.address,
                    to = address,
                    value = convertEthToWei(amount),
                    gas = BigInteger.valueOf(21000),
                    maxFeePerGas = maxFeePerGas,
                    maxPriorityFeePerGas = maxPriorityFeePerGas
                )

                // Send transaction
                val txHash = sdk.evm.sendTransaction(transaction, wallet)

                _uiState.value = UiState.Success(
                    TransactionResult(
                        transactionHash = txHash,
                        explorerUrl = "${Constants.SEPOLIA_EXPLORER_URL}/tx/$txHash"
                    )
                )
            } catch (e: Exception) {
                _uiState.value = UiState.Error(mapErrorMessage(e.message))
            }
        }
    }

    fun clearResult() {
        _uiState.value = UiState.Idle
        _formState.value = SendTransactionFormState()
    }

    fun clearError() {
        if (_uiState.value is UiState.Error) {
            _uiState.value = UiState.Idle
        }
    }

    private fun mapErrorMessage(message: String?): String {
        if (message == null) return "Transaction failed"
        val parsed = try {
            JSONObject(message).optString("message", message)
        } catch (_: Exception) {
            message
        }
        return when {
            parsed.contains("No EVM provider with chain") ->
                "Sepolia network is not available. Please check your Dynamic Dashboard configuration."

            parsed.contains("insufficient funds", ignoreCase = true) ->
                "Insufficient funds. Please get SepoliaETH from a faucet."

            parsed.contains("No EVM wallet found", ignoreCase = true) ->
                "No wallet found. Please log in again."

            parsed.contains("User rejected", ignoreCase = true) ->
                "Transaction was cancelled."

            parsed.contains("nonce", ignoreCase = true) ->
                "Transaction conflict. Please try again."

            parsed.contains("gas", ignoreCase = true) ->
                "Gas estimation failed. Please try again."

            parsed.contains("timeout", ignoreCase = true) ->
                "Network timeout. Please check your connection and try again."

            else -> parsed
        }
    }
}
