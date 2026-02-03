package com.erbol.testnetwallet.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.erbol.testnetwallet.presentation.login.LoginScreen
import com.erbol.testnetwallet.presentation.send.SendTransactionScreen
import com.erbol.testnetwallet.presentation.wallet.WalletScreen

/**
 * Type-safe navigation routes.
 */
sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Wallet : Screen("wallet")
    data object SendTransaction : Screen("send_transaction")
}

/**
 * Navigation graph for the application.
 */
@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Login.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = Screen.Login.route) {
            LoginScreen(
                onNavigateToWallet = {
                    navController.navigate(Screen.Wallet.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Screen.Wallet.route) {
            WalletScreen(
                onNavigateToSendTransaction = {
                    navController.navigate(Screen.SendTransaction.route)
                },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Wallet.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Screen.SendTransaction.route) {
            SendTransactionScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
