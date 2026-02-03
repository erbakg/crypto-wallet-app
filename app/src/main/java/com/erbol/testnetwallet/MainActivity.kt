package com.erbol.testnetwallet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.dynamic.sdk.android.DynamicSDK
import com.dynamic.sdk.android.UI.DynamicUI
import com.dynamic.sdk.android.core.ClientProps
import com.dynamic.sdk.android.core.LoggerLevel
import com.erbol.testnetwallet.presentation.navigation.NavGraph
import com.erbol.testnetwallet.presentation.navigation.Screen
import com.erbol.testnetwallet.ui.theme.BackgroundLight
import com.erbol.testnetwallet.ui.theme.TestnetWalletTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize Dynamic SDK
        initializeDynamicSDK()

        setContent {
            TestnetWalletTheme {
                val navController = rememberNavController()
                var startDestination by remember { mutableStateOf<String?>(null) }

                // Listen to Dynamic SDK auth state
                LaunchedEffect(Unit) {
                    try {
                        val sdk = DynamicSDK.getInstance()
                        sdk.auth.authenticatedUserChanges.collect { user ->
                            val isAuthenticated = user != null
                            if (startDestination == null) {
                                startDestination = if (isAuthenticated) {
                                    Screen.Wallet.route
                                } else {
                                    Screen.Login.route
                                }
                            }
                        }
                    } catch (e: Exception) {
                        // SDK not initialized yet, start with login
                        startDestination = Screen.Login.route
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = BackgroundLight
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        // Main app content
                        startDestination?.let { destination ->
                            NavGraph(
                                navController = navController,
                                startDestination = destination
                            )
                        }

                        // Dynamic SDK WebView overlay (shows when auth/profile is opened)
                        DynamicUI()
                    }
                }
            }
        }
    }

    private fun initializeDynamicSDK() {
        val environmentId = BuildConfig.DYNAMIC_ENVIRONMENT_ID
        if (environmentId.isBlank()) {
            return
        }

        val props = ClientProps(
            environmentId = environmentId,
            appLogoUrl = "https://demo.dynamic.xyz/favicon-32x32.png",
            appName = "Crypto Wallet",
            redirectUrl = "cryptowallet://",
            appOrigin = "https://cryptowallet.app",
            logLevel = LoggerLevel.DEBUG
        )
        DynamicSDK.initialize(props, applicationContext, this)
    }
}
