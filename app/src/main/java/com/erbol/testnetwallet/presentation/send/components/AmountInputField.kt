package com.erbol.testnetwallet.presentation.send.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erbol.testnetwallet.R
import com.erbol.testnetwallet.ui.theme.BorderLight
import com.erbol.testnetwallet.ui.theme.ErrorRed
import com.erbol.testnetwallet.ui.theme.PrimaryBlue
import com.erbol.testnetwallet.ui.theme.SurfaceWhite
import com.erbol.testnetwallet.ui.theme.TestnetWalletTheme
import com.erbol.testnetwallet.ui.theme.TextHint
import com.erbol.testnetwallet.ui.theme.TextPrimary

@Composable
fun AmountInputField(
    amount: String,
    onAmountChanged: (String) -> Unit,
    error: String?,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.amount_label),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = amount,
            onValueChange = onAmountChanged,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = stringResource(R.string.amount_placeholder),
                    color = TextHint
                )
            },
            isError = error != null,
            supportingText = error?.let { errorText ->
                { Text(errorText, color = ErrorRed) }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryBlue,
                unfocusedBorderColor = BorderLight,
                focusedContainerColor = SurfaceWhite,
                unfocusedContainerColor = SurfaceWhite,
                errorBorderColor = ErrorRed,
                errorContainerColor = SurfaceWhite
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AmountInputFieldPreview() {
    TestnetWalletTheme {
        AmountInputField(
            amount = "0.001",
            onAmountChanged = {},
            error = null
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AmountInputFieldErrorPreview() {
    TestnetWalletTheme {
        AmountInputField(
            amount = "0",
            onAmountChanged = {},
            error = "Amount must be greater than 0"
        )
    }
}
