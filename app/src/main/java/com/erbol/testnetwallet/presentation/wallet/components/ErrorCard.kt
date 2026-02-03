package com.erbol.testnetwallet.presentation.wallet.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erbol.testnetwallet.ui.theme.ErrorRed
import com.erbol.testnetwallet.ui.theme.ErrorRedLight
import com.erbol.testnetwallet.ui.theme.TestnetWalletTheme

@Composable
fun ErrorCard(
    message: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = ErrorRedLight)
    ) {
        Text(
            text = message,
            color = ErrorRed,
            fontSize = 14.sp,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorCardPreview() {
    TestnetWalletTheme {
        ErrorCard(message = "Failed to load wallet information")
    }
}
