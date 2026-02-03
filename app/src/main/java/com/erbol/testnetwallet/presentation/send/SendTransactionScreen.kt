package com.erbol.testnetwallet.presentation.send

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.erbol.testnetwallet.R
import com.erbol.testnetwallet.common.UiState
import com.erbol.testnetwallet.domain.model.TransactionResult
import com.erbol.testnetwallet.presentation.login.components.PrimaryButton
import com.erbol.testnetwallet.presentation.send.components.AddressInputField
import com.erbol.testnetwallet.presentation.send.components.AmountInputField
import com.erbol.testnetwallet.presentation.send.components.TransactionSuccessCard
import com.erbol.testnetwallet.presentation.wallet.components.ErrorCard
import com.erbol.testnetwallet.ui.theme.BackgroundLight

@Composable
fun SendTransactionScreen(
    onNavigateBack: () -> Unit,
    viewModel: SendTransactionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val formState by viewModel.formState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    SendTransactionContent(
        uiState = uiState,
        formState = formState,
        onNavigateBack = onNavigateBack,
        onRecipientChanged = viewModel::onRecipientChanged,
        onAmountChanged = viewModel::onAmountChanged,
        onSendClick = viewModel::sendTransaction,
        onCopyHash = { hash ->
            copyToClipboard(context, hash)
            Toast.makeText(
                context,
                context.getString(R.string.hash_copied),
                Toast.LENGTH_SHORT
            ).show()
        },
        onViewOnEtherscan = { url ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SendTransactionContent(
    uiState: UiState<TransactionResult>,
    formState: SendTransactionFormState,
    onNavigateBack: () -> Unit,
    onRecipientChanged: (String) -> Unit,
    onAmountChanged: (String) -> Unit,
    onSendClick: () -> Unit,
    onCopyHash: (String) -> Unit,
    onViewOnEtherscan: (String) -> Unit
) {
    val isLoading = uiState is UiState.Loading
    val error = (uiState as? UiState.Error)?.message
    val transactionResult = (uiState as? UiState.Success)?.data
    val isSuccess = transactionResult != null
    val canSend = formState.canSend && !isLoading

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.send_transaction_title),
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundLight)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                AddressInputField(
                    address = formState.recipientAddress,
                    onAddressChanged = onRecipientChanged,
                    error = formState.addressError
                )

                Spacer(modifier = Modifier.height(20.dp))

                AmountInputField(
                    amount = formState.amountEth,
                    onAmountChanged = onAmountChanged,
                    error = formState.amountError
                )

                Spacer(modifier = Modifier.height(24.dp))

                PrimaryButton(
                    text = stringResource(R.string.send_transaction),
                    onClick = onSendClick,
                    enabled = canSend,
                    isLoading = isLoading
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Error Message
                AnimatedVisibility(
                    visible = error != null && !isSuccess,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    ErrorCard(message = error ?: "")
                }

                // Success Card
                AnimatedVisibility(
                    visible = isSuccess,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    transactionResult?.let { result ->
                        TransactionSuccessCard(
                            truncatedHash = result.truncatedHash(),
                            onCopyHash = { onCopyHash(result.transactionHash) },
                            onViewOnEtherscan = { onViewOnEtherscan(result.explorerUrl) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

private fun copyToClipboard(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Transaction Hash", text)
    clipboard.setPrimaryClip(clip)
}
