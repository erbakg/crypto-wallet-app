package com.erbol.testnetwallet.presentation.login.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.erbol.testnetwallet.ui.theme.ErrorRed
import com.erbol.testnetwallet.ui.theme.TestnetWalletTheme

@Composable
fun ErrorText(
    message: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = message,
        color = ErrorRed,
        fontSize = 14.sp,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun ErrorTextPreview() {
    TestnetWalletTheme {
        ErrorText(message = "Invalid email address")
    }
}
