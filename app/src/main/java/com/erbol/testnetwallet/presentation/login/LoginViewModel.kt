package com.erbol.testnetwallet.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dynamic.sdk.android.DynamicSDK
import com.erbol.testnetwallet.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Data class for successful login result.
 */
data class LoginResult(
    val walletAddress: String
)

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<LoginResult>>(UiState.Idle)
    val uiState: StateFlow<UiState<LoginResult>> = _uiState.asStateFlow()

    private val sdk: DynamicSDK
        get() = DynamicSDK.getInstance()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _uiState.value = UiState.Error(throwable.message ?: "Unknown error")
    }

    init {
        observeWallets()
    }

    private fun observeWallets() {
        viewModelScope.launch(exceptionHandler) {
            sdk.wallets.userWalletsChanges.collect { wallets ->
                val evmWallet = wallets.firstOrNull { it.chain.uppercase() == "EVM" }
                if (evmWallet != null) {
                    // Hide the auth WebView immediately
                    sdk.webViewController.visibility.setOverrideVisible(false)

                    _uiState.value = UiState.Success(
                        LoginResult(walletAddress = evmWallet.address)
                    )
                }
            }
        }
    }

    /**
     * Shows Dynamic SDK auth modal (WebView overlay).
     */
    fun showAuthModal() {
        sdk.ui.showAuth()
    }

    fun clearError() {
        if (_uiState.value is UiState.Error) {
            _uiState.value = UiState.Idle
        }
    }
}
