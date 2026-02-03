package com.erbol.testnetwallet.presentation.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.erbol.testnetwallet.R
import com.erbol.testnetwallet.common.UiState
import com.erbol.testnetwallet.domain.model.AuthResult
import com.erbol.testnetwallet.presentation.login.components.AppLogo
import com.erbol.testnetwallet.presentation.login.components.EmailIcon
import com.erbol.testnetwallet.presentation.login.components.EmailInputCard
import com.erbol.testnetwallet.presentation.login.components.ErrorText
import com.erbol.testnetwallet.presentation.login.components.OtpInputRow
import com.erbol.testnetwallet.presentation.login.components.PrimaryButton
import com.erbol.testnetwallet.presentation.login.components.ResendCodeSection
import com.erbol.testnetwallet.ui.theme.BackgroundLight
import com.erbol.testnetwallet.ui.theme.TextPrimary
import com.erbol.testnetwallet.ui.theme.TextSecondary

@Composable
fun LoginScreen(
    onNavigateToWallet: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val formState by viewModel.formState.collectAsStateWithLifecycle()

    val isAuthenticated = uiState is UiState.Success
    val isLoading = uiState is UiState.Loading
    val error = (uiState as? UiState.Error)?.message

    LaunchedEffect(isAuthenticated) {
        if (isAuthenticated) {
            onNavigateToWallet()
        }
    }

    if (formState.isOtpSent) {
        OtpVerificationContent(
            formState = formState,
            isLoading = isLoading,
            error = error,
            onOtpChanged = viewModel::onOtpChanged,
            onVerifyClick = viewModel::verifyOtp,
            onResendClick = viewModel::resendOtp,
            onBackClick = viewModel::goBackToEmail
        )
    } else {
        EmailInputContent(
            formState = formState,
            isLoading = isLoading,
            error = error,
            onEmailChanged = viewModel::onEmailChanged,
            onSendOtpClick = viewModel::sendOtp
        )
    }
}

@Composable
internal fun EmailInputContent(
    formState: LoginFormState,
    isLoading: Boolean,
    error: String?,
    onEmailChanged: (String) -> Unit,
    onSendOtpClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val canSendOtp = formState.isEmailValid && !isLoading

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

            EmailInputCard(
                email = formState.email,
                onEmailChanged = onEmailChanged,
                onDone = {
                    focusManager.clearFocus()
                    if (canSendOtp) onSendOtpClick()
                }
            )

            if (error != null) {
                Spacer(modifier = Modifier.height(8.dp))
                ErrorText(message = error)
            }

            Spacer(modifier = Modifier.height(24.dp))

            PrimaryButton(
                text = stringResource(R.string.send_otp_button),
                onClick = onSendOtpClick,
                enabled = canSendOtp,
                isLoading = isLoading
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun OtpVerificationContent(
    formState: LoginFormState,
    isLoading: Boolean,
    error: String?,
    onOtpChanged: (String) -> Unit,
    onVerifyClick: () -> Unit,
    onResendClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val canVerifyOtp = formState.isOtpValid && !isLoading

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.verify_email_title),
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
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
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(40.dp))

                EmailIcon()

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = stringResource(R.string.check_email_title),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = stringResource(R.string.otp_sent_message),
                    fontSize = 14.sp,
                    color = TextSecondary
                )

                Text(
                    text = formState.email,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(40.dp))

                OtpInputRow(
                    otpCode = formState.otpCode,
                    onOtpChanged = onOtpChanged,
                    onDone = { if (canVerifyOtp) onVerifyClick() }
                )

                if (error != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    ErrorText(message = error)
                }

                Spacer(modifier = Modifier.height(32.dp))

                PrimaryButton(
                    text = stringResource(R.string.verify_button),
                    onClick = onVerifyClick,
                    enabled = canVerifyOtp,
                    isLoading = isLoading
                )

                Spacer(modifier = Modifier.height(24.dp))

                ResendCodeSection(
                    canResend = formState.canResendOtp && !isLoading,
                    cooldownSeconds = formState.resendCooldownSeconds,
                    onResendClick = onResendClick
                )
            }
        }
    }
}
