package com.erbol.testnetwallet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.erbol.testnetwallet.data.local.SessionStorage
import com.erbol.testnetwallet.presentation.navigation.NavGraph
import com.erbol.testnetwallet.presentation.navigation.Screen
import com.erbol.testnetwallet.ui.theme.BackgroundLight
import com.erbol.testnetwallet.ui.theme.TestnetWalletTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var sessionStorage: SessionStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TestnetWalletTheme {
                val navController = rememberNavController()
                var startDestination by remember { mutableStateOf<String?>(null) }

                // Determine start destination based on auth state
                LaunchedEffect(Unit) {
                    val isLoggedIn = sessionStorage.isLoggedIn.first()
                    startDestination = if (isLoggedIn) {
                        Screen.Wallet.route
                    } else {
                        Screen.Login.route
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = BackgroundLight
                ) {
                    startDestination?.let { destination ->
                        NavGraph(
                            navController = navController,
                            startDestination = destination
                        )
                    }
                }
            }
        }
    }
}
