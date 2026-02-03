package com.erbol.testnetwallet.presentation.login

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.erbol.testnetwallet.ui.theme.TestnetWalletTheme

@Preview(showBackground = true)
@Composable
private fun EmailInputContentPreview() {
    TestnetWalletTheme {
        EmailInputContent(
            formState = LoginFormState(email = "test@example.com"),
            isLoading = false,
            error = null,
            onEmailChanged = {},
            onSendOtpClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EmailInputContentLoadingPreview() {
    TestnetWalletTheme {
        EmailInputContent(
            formState = LoginFormState(email = "test@example.com"),
            isLoading = true,
            error = null,
            onEmailChanged = {},
            onSendOtpClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OtpVerificationContentPreview() {
    TestnetWalletTheme {
        OtpVerificationContent(
            formState = LoginFormState(
                email = "test@example.com",
                isOtpSent = true,
                otpCode = "123"
            ),
            isLoading = false,
            error = null,
            onOtpChanged = {},
            onVerifyClick = {},
            onResendClick = {},
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OtpVerificationContentErrorPreview() {
    TestnetWalletTheme {
        OtpVerificationContent(
            formState = LoginFormState(
                email = "test@example.com",
                isOtpSent = true,
                otpCode = "123456"
            ),
            isLoading = false,
            error = "Invalid OTP code",
            onOtpChanged = {},
            onVerifyClick = {},
            onResendClick = {},
            onBackClick = {}
        )
    }
}
