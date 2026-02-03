package com.erbol.testnetwallet.presentation.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dynamic.sdk.android.DynamicSDK
import com.dynamic.sdk.android.Chains.EVM.EthereumTransaction
import com.dynamic.sdk.android.Chains.EVM.convertEthToWei
import com.dynamic.sdk.android.Models.BaseWallet
import com.erbol.testnetwallet.common.Constants
import com.erbol.testnetwallet.common.UiState
import com.erbol.testnetwallet.domain.model.WalletInfo
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
class WalletViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<WalletInfo>>(UiState.Idle)
    val uiState: StateFlow<UiState<WalletInfo>> = _uiState.asStateFlow()

    private val _walletUiState = MutableStateFlow(WalletUiState())
    val walletUiState: StateFlow<WalletUiState> = _walletUiState.asStateFlow()

    private val sdk: DynamicSDK
        get() = DynamicSDK.getInstance()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _uiState.value = UiState.Error(throwable.message ?: "Unknown error")
        _walletUiState.update { it.copy(isRefreshing = false) }
    }

    init {
        observeWallets()
    }

    private fun observeWallets() {
        viewModelScope.launch(exceptionHandler) {
            _uiState.value = UiState.Loading

            sdk.wallets.userWalletsChanges.collect { wallets ->
                val evmWallet = wallets.firstOrNull { it.chain.uppercase() == "EVM" }
                if (evmWallet != null) {
                    loadWalletInfo(evmWallet)
                }
            }
        }
    }

    private suspend fun loadWalletInfo(wallet: BaseWallet) {
        try {
            val balance = sdk.wallets.getBalance(wallet)

            _uiState.value = UiState.Success(
                WalletInfo(
                    address = wallet.address,
                    network = Constants.SEPOLIA_CHAIN_NAME,
                    chainId = Constants.SEPOLIA_CHAIN_ID,
                    balanceEth = balance.toBigDecimal()
                )
            )
        } catch (e: Exception) {
            // Balance fetch failed, show wallet with zero balance
            _uiState.value = UiState.Success(
                WalletInfo(
                    address = wallet.address,
                    network = Constants.SEPOLIA_CHAIN_NAME,
                    chainId = Constants.SEPOLIA_CHAIN_ID,
                    balanceEth = BigDecimal.ZERO
                )
            )
        }
    }

    fun refreshBalance() {
        val currentWalletInfo = (_uiState.value as? UiState.Success)?.data ?: return
        val evmWallet = sdk.wallets.userWallets.firstOrNull { it.chain.uppercase() == "EVM" } ?: return

        viewModelScope.launch(exceptionHandler) {
            _walletUiState.update { it.copy(isRefreshing = true) }

            try {
                val balance = sdk.wallets.getBalance(evmWallet)
                _uiState.value = UiState.Success(
                    currentWalletInfo.copy(balanceEth = balance.toBigDecimal())
                )
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Failed to refresh balance")
            }

            _walletUiState.update { it.copy(isRefreshing = false) }
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
            try {
                sdk.auth.logout()
                _walletUiState.update { it.copy(isLoggedOut = true) }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Logout failed")
            }
        }
    }

    fun clearError() {
        if (_uiState.value is UiState.Error) {
            _uiState.value = UiState.Idle
        }
    }
}
