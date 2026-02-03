package com.erbol.testnetwallet.presentation.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.erbol.testnetwallet.R
import com.erbol.testnetwallet.common.UiState
import com.erbol.testnetwallet.presentation.login.components.AppLogo
import com.erbol.testnetwallet.presentation.login.components.PrimaryButton
import com.erbol.testnetwallet.ui.theme.BackgroundLight
import com.erbol.testnetwallet.ui.theme.TextPrimary
import com.erbol.testnetwallet.ui.theme.TextSecondary

@Composable
fun LoginScreen(
    onNavigateToWallet: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val isAuthenticated = uiState is UiState.Success

    LaunchedEffect(isAuthenticated) {
        if (isAuthenticated) {
            onNavigateToWallet()
        }
    }

    LoginContent(
        onLoginClick = viewModel::showAuthModal
    )
}

@Composable
internal fun LoginContent(
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(120.dp))

            AppLogo()

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.app_title),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.login_subtitle),
                fontSize = 16.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(48.dp))

            PrimaryButton(
                text = stringResource(R.string.login_button),
                onClick = onLoginClick,
                enabled = true,
                isLoading = false
            )
        }
    }
}
