package com.erbol.testnetwallet.presentation.login.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erbol.testnetwallet.R
import com.erbol.testnetwallet.ui.theme.PrimaryBlue
import com.erbol.testnetwallet.ui.theme.TestnetWalletTheme
import com.erbol.testnetwallet.ui.theme.TextSecondary

@Composable
fun ResendCodeSection(
    canResend: Boolean,
    cooldownSeconds: Int,
    onResendClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.didnt_receive_code),
            fontSize = 14.sp,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(4.dp))

        TextButton(
            onClick = onResendClick,
            enabled = canResend
        ) {
            Text(
                text = if (canResend) {
                    stringResource(R.string.resend_code)
                } else {
                    stringResource(R.string.resend_code_cooldown, cooldownSeconds)
                },
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (canResend) PrimaryBlue else TextSecondary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ResendCodeSectionPreview() {
    TestnetWalletTheme {
        ResendCodeSection(
            canResend = true,
            cooldownSeconds = 0,
            onResendClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ResendCodeSectionCooldownPreview() {
    TestnetWalletTheme {
        ResendCodeSection(
            canResend = false,
            cooldownSeconds = 45,
            onResendClick = {}
        )
    }
}
