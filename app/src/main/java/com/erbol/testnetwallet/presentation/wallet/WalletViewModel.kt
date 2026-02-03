package com.erbol.testnetwallet.presentation.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erbol.testnetwallet.common.UiState
import com.erbol.testnetwallet.domain.model.WalletInfo
import com.erbol.testnetwallet.domain.usecase.GetWalletInfoUseCase
import com.erbol.testnetwallet.domain.usecase.LogoutUseCase
import com.erbol.testnetwallet.domain.usecase.RefreshBalanceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WalletViewModel @Inject constructor(
    private val getWalletInfoUseCase: GetWalletInfoUseCase,
    private val refreshBalanceUseCase: RefreshBalanceUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<WalletInfo>>(UiState.Idle)
    val uiState: StateFlow<UiState<WalletInfo>> = _uiState.asStateFlow()

    private val _walletUiState = MutableStateFlow(WalletUiState())
    val walletUiState: StateFlow<WalletUiState> = _walletUiState.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _uiState.value = UiState.Error(throwable.message ?: "Unknown error")
        _walletUiState.update { it.copy(isRefreshing = false) }
    }

    init {
        loadWalletInfo()
    }

    fun loadWalletInfo() {
        viewModelScope.launch(exceptionHandler) {
            _uiState.value = UiState.Loading

            getWalletInfoUseCase()
                .onSuccess { walletInfo ->
                    _uiState.value = UiState.Success(walletInfo)
                }
                .onFailure { e ->
                    _uiState.value = UiState.Error(e.message ?: "Failed to load wallet")
                }
        }
    }

    fun refreshBalance() {
        val currentWalletInfo = (_uiState.value as? UiState.Success)?.data ?: return

        viewModelScope.launch(exceptionHandler) {
            _walletUiState.update { it.copy(isRefreshing = true) }

            refreshBalanceUseCase()
                .onSuccess { balance ->
                    _uiState.value = UiState.Success(
                        currentWalletInfo.copy(balanceEth = balance)
                    )
                    _walletUiState.update { it.copy(isRefreshing = false) }
                }
                .onFailure { e ->
                    _uiState.value = UiState.Error(e.message ?: "Failed to refresh balance")
                    _walletUiState.update { it.copy(isRefreshing = false) }
                }
        }
    }

    fun onAddressCopied() {
        _walletUiState.update { it.copy(isAddressCopied = true) }
    }

    fun onAddressCopyHandled() {
        _walletUiState.update { it.copy(isAddressCopied = false) }
    }

    fun logout() {
        viewModelScope.launch(exceptionHandler) {
            logoutUseCase()
                .onSuccess {
                    _walletUiState.update { it.copy(isLoggedOut = true) }
                }
                .onFailure { e ->
                    _uiState.value = UiState.Error(e.message ?: "Logout failed")
                }
        }
    }

    fun clearError() {
        if (_uiState.value is UiState.Error) {
            val previousData = (_uiState.value as? UiState.Success)?.data
            _uiState.value = if (previousData != null) {
                UiState.Success(previousData)
            } else {
                UiState.Idle
            }
        }
    }
}
