package com.erbol.testnetwallet.presentation.wallet

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.erbol.testnetwallet.R
import com.erbol.testnetwallet.common.UiState
import com.erbol.testnetwallet.domain.model.WalletInfo
import com.erbol.testnetwallet.presentation.wallet.components.ActionCard
import com.erbol.testnetwallet.presentation.wallet.components.ErrorCard
import com.erbol.testnetwallet.presentation.wallet.components.WalletInfoCard
import com.erbol.testnetwallet.ui.theme.BackgroundLight
import com.erbol.testnetwallet.ui.theme.ErrorRed
import com.erbol.testnetwallet.ui.theme.PrimaryBlue

@Composable
fun WalletScreen(
    onNavigateToSendTransaction: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: WalletViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val walletUiState by viewModel.walletUiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(walletUiState.isLoggedOut) {
        if (walletUiState.isLoggedOut) {
            onNavigateToLogin()
        }
    }

    val walletInfo = (uiState as? UiState.Success)?.data

    LaunchedEffect(walletUiState.isAddressCopied) {
        if (walletUiState.isAddressCopied && walletInfo != null) {
            copyToClipboard(context, walletInfo.address)
            Toast.makeText(
                context,
                context.getString(R.string.address_copied),
                Toast.LENGTH_SHORT
            ).show()
            viewModel.onAddressCopyHandled()
        }
    }

    WalletContent(
        uiState = uiState,
        walletUiState = walletUiState,
        onRefresh = viewModel::refreshBalance,
        onCopyAddress = viewModel::onAddressCopied,
        onSendTransaction = onNavigateToSendTransaction,
        onLogout = viewModel::logout
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun WalletContent(
    uiState: UiState<WalletInfo>,
    walletUiState: WalletUiState,
    onRefresh: () -> Unit,
    onCopyAddress: () -> Unit,
    onSendTransaction: () -> Unit,
    onLogout: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.wallet_details_title),
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundLight)
            )

            when (uiState) {
                is UiState.Idle, is UiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = PrimaryBlue)
                    }
                }

                is UiState.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        ErrorCard(message = uiState.message)
                    }
                }

                is UiState.Success -> {
                    val walletInfo = uiState.data
                    PullToRefreshBox(
                        isRefreshing = walletUiState.isRefreshing,
                        onRefresh = onRefresh,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(horizontal = 16.dp)
                        ) {
                            Spacer(modifier = Modifier.height(8.dp))

                            WalletInfoCard(
                                address = walletInfo.address,
                                networkDisplay = walletInfo.networkDisplay(),
                                formattedBalance = walletInfo.formattedBalance(),
                                isLoading = walletUiState.isRefreshing
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            ActionCard(
                                icon = Icons.Default.ContentCopy,
                                text = stringResource(R.string.copy_address),
                                onClick = onCopyAddress
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            SendTransactionButton(onClick = onSendTransaction)

                            Spacer(modifier = Modifier.height(12.dp))

                            ActionCard(
                                icon = Icons.AutoMirrored.Filled.ExitToApp,
                                text = stringResource(R.string.logout),
                                onClick = onLogout,
                                textColor = ErrorRed,
                                iconTint = ErrorRed
                            )

                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SendTransactionButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.Send,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(R.string.send_transaction),
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
    }
}

private fun copyToClipboard(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Wallet Address", text)
    clipboard.setPrimaryClip(clip)
}
