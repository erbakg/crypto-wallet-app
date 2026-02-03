package com.erbol.testnetwallet.presentation.login.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erbol.testnetwallet.common.Constants
import com.erbol.testnetwallet.ui.theme.BorderLight
import com.erbol.testnetwallet.ui.theme.PrimaryBlue
import com.erbol.testnetwallet.ui.theme.SurfaceWhite
import com.erbol.testnetwallet.ui.theme.TestnetWalletTheme
import com.erbol.testnetwallet.ui.theme.TextHint
import com.erbol.testnetwallet.ui.theme.TextPrimary

@Composable
fun OtpInputRow(
    otpCode: String,
    onOtpChanged: (String) -> Unit,
    onDone: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
    ) {
        repeat(Constants.OTP_LENGTH) { index ->
            OtpDigitBox(
                digit = otpCode.getOrNull(index)?.toString() ?: "",
                isFocused = otpCode.length == index
            )
        }
    }

    // Hidden input field for keyboard
    BasicTextField(
        value = otpCode,
        onValueChange = { newValue ->
            if (newValue.length <= Constants.OTP_LENGTH && newValue.all { it.isDigit() }) {
                onOtpChanged(newValue)
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = { onDone() }),
        modifier = Modifier
            .size(1.dp)
            .focusRequester(focusRequester)
    )
}

@Composable
private fun OtpDigitBox(
    digit: String,
    isFocused: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(48.dp)
            .border(
                width = if (isFocused) 2.dp else 1.dp,
                color = if (isFocused) PrimaryBlue else BorderLight,
                shape = RoundedCornerShape(12.dp)
            )
            .background(SurfaceWhite, RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = digit.ifEmpty { if (isFocused) "" else "-" },
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = if (digit.isEmpty()) TextHint else TextPrimary,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OtpInputRowPreview() {
    TestnetWalletTheme {
        OtpInputRow(
            otpCode = "123",
            onOtpChanged = {},
            onDone = {}
        )
    }
}
