package com.erbol.testnetwallet.presentation.login

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.erbol.testnetwallet.ui.theme.TestnetWalletTheme

@Preview(showBackground = true)
@Composable
private fun LoginContentPreview() {
    TestnetWalletTheme {
        LoginContent(
            onLoginClick = {}
        )
    }
}
