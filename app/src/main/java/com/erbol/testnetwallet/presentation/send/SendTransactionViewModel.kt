package com.erbol.testnetwallet.presentation.send

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erbol.testnetwallet.common.UiState
import com.erbol.testnetwallet.domain.model.TransactionResult
import com.erbol.testnetwallet.domain.repository.WalletRepository
import com.erbol.testnetwallet.domain.usecase.SendTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class SendTransactionViewModel @Inject constructor(
    private val sendTransactionUseCase: SendTransactionUseCase,
    private val walletRepository: WalletRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<TransactionResult>>(UiState.Idle)
    val uiState: StateFlow<UiState<TransactionResult>> = _uiState.asStateFlow()

    private val _formState = MutableStateFlow(SendTransactionFormState())
    val formState: StateFlow<SendTransactionFormState> = _formState.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _uiState.value = UiState.Error(throwable.message ?: "Unknown error")
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
        if (address.isNotEmpty() && !walletRepository.isValidAddress(address)) {
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

    fun sendTransaction() {
        val address = _formState.value.recipientAddress.trim()
        val amount = _formState.value.amountEth.trim()

        // Final validation
        if (!walletRepository.isValidAddress(address)) {
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

            sendTransactionUseCase(address, amount)
                .onSuccess { result ->
                    _uiState.value = UiState.Success(result)
                }
                .onFailure { e ->
                    _uiState.value = UiState.Error(e.message ?: "Transaction failed")
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
}
