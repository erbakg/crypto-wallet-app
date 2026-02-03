package com.erbol.testnetwallet.presentation.send.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erbol.testnetwallet.R
import com.erbol.testnetwallet.ui.theme.PrimaryBlue
import com.erbol.testnetwallet.ui.theme.SuccessBorder
import com.erbol.testnetwallet.ui.theme.SuccessGreen
import com.erbol.testnetwallet.ui.theme.SuccessGreenLight
import com.erbol.testnetwallet.ui.theme.TestnetWalletTheme
import com.erbol.testnetwallet.ui.theme.TextPrimary
import com.erbol.testnetwallet.ui.theme.TextSecondary

@Composable
fun TransactionSuccessCard(
    truncatedHash: String,
    onCopyHash: () -> Unit,
    onViewOnEtherscan: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = SuccessBorder,
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SuccessGreenLight)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            SuccessHeader()

            Spacer(modifier = Modifier.height(16.dp))

            TransactionHashSection(
                truncatedHash = truncatedHash,
                onCopyHash = onCopyHash
            )

            Spacer(modifier = Modifier.height(12.dp))

            EtherscanLink(onClick = onViewOnEtherscan)
        }
    }
}

@Composable
private fun SuccessHeader() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = SuccessGreen,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = stringResource(R.string.transaction_success),
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = SuccessGreen
        )
    }
}

@Composable
private fun TransactionHashSection(
    truncatedHash: String,
    onCopyHash: () -> Unit
) {
    Text(
        text = stringResource(R.string.transaction_hash_label),
        fontSize = 14.sp,
        color = TextSecondary
    )

    Spacer(modifier = Modifier.height(8.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = truncatedHash,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = TextPrimary,
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = onCopyHash) {
            Icon(
                imageVector = Icons.Default.ContentCopy,
                contentDescription = stringResource(R.string.copy),
                tint = TextSecondary
            )
        }
    }
}

@Composable
private fun EtherscanLink(onClick: () -> Unit) {
    Text(
        text = stringResource(R.string.view_on_etherscan),
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = PrimaryBlue,
        textDecoration = TextDecoration.Underline,
        modifier = Modifier.clickable { onClick() }
    )
}

@Preview(showBackground = true)
@Composable
private fun TransactionSuccessCardPreview() {
    TestnetWalletTheme {
        TransactionSuccessCard(
            truncatedHash = "0xabc123...def456",
            onCopyHash = {},
            onViewOnEtherscan = {}
        )
    }
}
