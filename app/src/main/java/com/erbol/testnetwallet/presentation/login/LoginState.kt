package com.erbol.testnetwallet.presentation.login

/**
 * Form state for Login screen inputs.
 * Separated from UiState to follow the pattern where UiState handles async operations.
 */
data class LoginFormState(
    val email: String = "",
    val otpCode: String = "",
    val isOtpSent: Boolean = false,
    val canResendOtp: Boolean = true,
    val resendCooldownSeconds: Int = 0
) {
    val isEmailValid: Boolean
        get() = email.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))

    val isOtpValid: Boolean
        get() = otpCode.length == 6 && otpCode.all { it.isDigit() }
}

/**
 * Combined state for Login screen.
 * Contains form inputs and async operation state.
 */
data class LoginScreenState(
    val form: LoginFormState = LoginFormState(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isAuthenticated: Boolean = false
) {
    val canSendOtp: Boolean
        get() = form.isEmailValid && !isLoading

    val canVerifyOtp: Boolean
        get() = form.isOtpValid && !isLoading
}
