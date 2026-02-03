package com.erbol.testnetwallet.presentation.wallet.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erbol.testnetwallet.R
import com.erbol.testnetwallet.ui.theme.BackgroundLight
import com.erbol.testnetwallet.ui.theme.BalanceRed
import com.erbol.testnetwallet.ui.theme.EvmTagBackground
import com.erbol.testnetwallet.ui.theme.EvmTagText
import com.erbol.testnetwallet.ui.theme.PrimaryBlue
import com.erbol.testnetwallet.ui.theme.SurfaceWhite
import com.erbol.testnetwallet.ui.theme.TestnetWalletTheme
import com.erbol.testnetwallet.ui.theme.TextPrimary
import com.erbol.testnetwallet.ui.theme.TextSecondary

@Composable
fun WalletInfoCard(
    address: String,
    networkDisplay: String,
    formattedBalance: String,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            EvmTag()

            Spacer(modifier = Modifier.height(20.dp))

            AddressSection(
                address = address,
                isLoading = isLoading
            )

            Spacer(modifier = Modifier.height(24.dp))

            NetworkSection(networkDisplay = networkDisplay)

            Spacer(modifier = Modifier.height(24.dp))

            BalanceSection(
                formattedBalance = formattedBalance,
                isLoading = isLoading
            )
        }
    }
}

@Composable
private fun EvmTag() {
    Box(
        modifier = Modifier
            .background(EvmTagBackground, RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = stringResource(R.string.evm_tag),
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = EvmTagText
        )
    }
}

@Composable
private fun AddressSection(
    address: String,
    isLoading: Boolean
) {
    Text(
        text = stringResource(R.string.address_label),
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        color = TextPrimary
    )

    Spacer(modifier = Modifier.height(8.dp))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(BackgroundLight, RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp
            )
        } else {
            Text(
                text = address.ifEmpty { stringResource(R.string.loading) },
                fontSize = 14.sp,
                color = TextPrimary,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun NetworkSection(networkDisplay: String) {
    Text(
        text = stringResource(R.string.current_network_label),
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        color = TextPrimary
    )

    Spacer(modifier = Modifier.height(4.dp))

    Text(
        text = networkDisplay,
        fontSize = 14.sp,
        color = TextSecondary
    )
}

@Composable
private fun BalanceSection(
    formattedBalance: String,
    isLoading: Boolean
) {
    Text(
        text = stringResource(R.string.balance_label),
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        color = TextPrimary
    )

    Spacer(modifier = Modifier.height(4.dp))

    Row(verticalAlignment = Alignment.Bottom) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp
            )
        } else {
            Text(
                text = formattedBalance,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = BalanceRed
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = stringResource(R.string.eth_symbol),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = PrimaryBlue,
                modifier = Modifier.padding(bottom = 2.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WalletInfoCardPreview() {
    TestnetWalletTheme {
        WalletInfoCard(
            address = "0x6E6F148c27fB49c9760371A9723d08C4d5Af8b4",
            networkDisplay = "Sepolia - 11155111",
            formattedBalance = "1.18769409",
            isLoading = false
        )
    }
}
