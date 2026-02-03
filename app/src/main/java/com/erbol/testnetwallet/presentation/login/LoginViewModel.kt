package com.erbol.testnetwallet.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erbol.testnetwallet.common.Constants
import com.erbol.testnetwallet.common.UiState
import com.erbol.testnetwallet.domain.model.AuthResult
import com.erbol.testnetwallet.domain.repository.AuthRepository
import com.erbol.testnetwallet.domain.usecase.SendOtpUseCase
import com.erbol.testnetwallet.domain.usecase.VerifyOtpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val sendOtpUseCase: SendOtpUseCase,
    private val verifyOtpUseCase: VerifyOtpUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<AuthResult>>(UiState.Idle)
    val uiState: StateFlow<UiState<AuthResult>> = _uiState.asStateFlow()

    private val _formState = MutableStateFlow(LoginFormState())
    val formState: StateFlow<LoginFormState> = _formState.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _uiState.value = UiState.Error(throwable.message ?: "Unknown error")
    }

    init {
        checkAuthState()
    }

    private fun checkAuthState() {
        viewModelScope.launch(exceptionHandler) {
            authRepository.isAuthenticated().collect { isAuthenticated ->
                if (isAuthenticated) {
                    _uiState.value = UiState.Success(AuthResult(isAuthenticated = true, walletAddress = ""))
                }
            }
        }
    }

    fun onEmailChanged(email: String) {
        _formState.update { it.copy(email = email) }
        if (_uiState.value is UiState.Error) {
            _uiState.value = UiState.Idle
        }
    }

    fun onOtpChanged(otp: String) {
        if (otp.length <= Constants.OTP_LENGTH && otp.all { it.isDigit() }) {
            _formState.update { it.copy(otpCode = otp) }
            if (_uiState.value is UiState.Error) {
                _uiState.value = UiState.Idle
            }
        }
    }

    fun sendOtp() {
        val email = _formState.value.email.trim()
        if (email.isBlank()) {
            _uiState.value = UiState.Error("Please enter your email")
            return
        }

        viewModelScope.launch(exceptionHandler) {
            _uiState.value = UiState.Loading

            sendOtpUseCase(email)
                .onSuccess {
                    _uiState.value = UiState.Idle
                    _formState.update {
                        it.copy(isOtpSent = true, canResendOtp = false)
                    }
                    startResendCooldown()
                }
                .onFailure { e ->
                    _uiState.value = UiState.Error(e.message ?: "Failed to send OTP")
                }
        }
    }

    fun verifyOtp() {
        val otp = _formState.value.otpCode
        if (otp.length != Constants.OTP_LENGTH) {
            _uiState.value = UiState.Error("Please enter the ${Constants.OTP_LENGTH}-digit code")
            return
        }

        viewModelScope.launch(exceptionHandler) {
            _uiState.value = UiState.Loading

            verifyOtpUseCase(otp)
                .onSuccess { result ->
                    _uiState.value = UiState.Success(result)
                }
                .onFailure { e ->
                    _uiState.value = UiState.Error(e.message ?: "Verification failed")
                }
        }
    }

    fun resendOtp() {
        if (!_formState.value.canResendOtp) return
        sendOtp()
    }

    fun goBackToEmail() {
        _formState.update {
            it.copy(
                isOtpSent = false,
                otpCode = "",
                canResendOtp = true,
                resendCooldownSeconds = 0
            )
        }
        _uiState.value = UiState.Idle
    }

    fun clearError() {
        if (_uiState.value is UiState.Error) {
            _uiState.value = UiState.Idle
        }
    }

    private fun startResendCooldown() {
        viewModelScope.launch {
            for (i in Constants.OTP_RESEND_COOLDOWN_SECONDS downTo 1) {
                _formState.update { it.copy(resendCooldownSeconds = i) }
                delay(1000L)
            }
            _formState.update {
                it.copy(canResendOtp = true, resendCooldownSeconds = 0)
            }
        }
    }
}
