# Crypto Wallet - Sepolia Testnet

A production-quality Android cryptocurrency wallet application for the Ethereum Sepolia testnet, built with Kotlin, Jetpack Compose, and **Dynamic SDK** for Web3 authentication.

## Architecture

The application follows **Clean Architecture** with clear separation between layers:

```
┌─────────────────────────────────────────────────────────────────┐
│                      PRESENTATION LAYER                         │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────────┐ │
│  │ LoginScreen │  │WalletScreen │  │ SendTransactionScreen   │ │
│  └──────┬──────┘  └──────┬──────┘  └────────────┬────────────┘ │
│         │                │                      │               │
│  ┌──────▼──────┐  ┌──────▼──────┐  ┌───────────▼────────────┐  │
│  │LoginViewModel│  │WalletViewModel│  │SendTransactionViewModel│ │
│  │              │  │              │  │                        │  │
│  │ UiState<T>   │  │ UiState<T>   │  │ UiState<T>             │  │
│  └──────┬──────┘  └──────┬──────┘  └───────────┬────────────┘  │
└─────────┼────────────────┼─────────────────────┼───────────────┘
          │                │                     │
          ▼                ▼                     ▼
┌─────────────────────────────────────────────────────────────────┐
│                        DOMAIN LAYER                             │
│  ┌────────────┐ ┌────────────┐ ┌──────────────┐ ┌────────────┐ │
│  │SendOtpUseCase│ │VerifyOtp │ │GetWalletInfo │ │SendTransaction│
│  └──────┬─────┘ └─────┬──────┘ └──────┬───────┘ └──────┬─────┘ │
│         │             │               │                │        │
│  ┌──────▼─────────────▼───────────────▼────────────────▼─────┐ │
│  │              Repository Interfaces                        │  │
│  │         AuthRepository    WalletRepository                │  │
│  └───────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                         DATA LAYER                              │
│  ┌────────────────────────────────────────────────────────────┐ │
│  │              Repository Implementations                    │  │
│  │        AuthRepositoryImpl    WalletRepositoryImpl          │  │
│  └─────────────────────┬──────────────────────────────────────┘ │
│                        │                                        │
│  ┌─────────────────────▼──────────────────────────────────────┐ │
│  │                  Dynamic SDK                               │  │
│  │     ┌──────────────┐          ┌──────────────┐             │ │
│  │     │  Auth Module │          │ Wallet/EVM   │             │ │
│  │     │  (Email OTP) │          │  Module      │             │ │
│  │     └──────────────┘          └──────────────┘             │ │
│  └────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────┘
```

### Layer Responsibilities

| Layer            | Responsibility                                                  |
| ---------------- | --------------------------------------------------------------- |
| **Presentation** | UI components (Compose), ViewModels, UI state management        |
| **Domain**       | Business logic, use cases, repository interfaces, domain models |
| **Data**         | Repository implementations using Dynamic SDK                    |

## Tech Stack

| Category          | Technology                    |
| ----------------- | ----------------------------- |
| **Language**      | Kotlin 2.0                    |
| **UI Framework**  | Jetpack Compose               |
| **Design System** | Material Design 3             |
| **Architecture**  | MVVM + Clean Architecture     |
| **DI**            | Hilt (Dagger)                 |
| **Async**         | Kotlin Coroutines + StateFlow |
| **Navigation**    | Jetpack Navigation Compose    |
| **Web3 SDK**      | Dynamic SDK (AAR)             |
| **Build System**  | Gradle Kotlin DSL             |

## Dynamic SDK Integration

This app uses the **Dynamic Android SDK** for:

- Email OTP authentication
- Wallet creation and management
- EVM transaction signing and sending
- Balance queries

The SDK is included as an AAR file (`app/libs/dynamic-sdk-android.aar`).

### SDK Features Used

```kotlin
// Authentication
sdk.ui.showAuth()                    // Show auth modal
sdk.auth.email.sendOTP(email)        // Send OTP
sdk.auth.email.verifyOTP(code)       // Verify OTP
sdk.auth.logout()                    // Logout

// Wallet
sdk.wallets.userWallets              // Get wallets
sdk.wallets.getBalance(wallet)       // Get balance

// Transactions
sdk.evm.createPublicClient(chainId)  // Create EVM client
sdk.evm.sendTransaction(tx, wallet)  // Send transaction
```

## Project Structure

```
app/src/main/java/com/erbol/testnetwallet/
├── common/                      # Shared utilities
│   ├── Constants.kt             # App-wide constants
│   └── UiState.kt               # Generic UI state sealed interface
├── data/
│   └── repository/
│       ├── AuthRepositoryImpl.kt    # Uses Dynamic SDK auth
│       └── WalletRepositoryImpl.kt  # Uses Dynamic SDK wallets
├── di/
│   ├── AppModule.kt             # Hilt providers
│   └── RepositoryModule.kt      # Repository bindings
├── domain/
│   ├── model/
│   │   ├── AuthResult.kt
│   │   ├── TransactionResult.kt
│   │   └── WalletInfo.kt
│   ├── repository/
│   │   ├── AuthRepository.kt
│   │   └── WalletRepository.kt
│   └── usecase/
│       ├── SendOtpUseCase.kt
│       ├── VerifyOtpUseCase.kt
│       ├── GetWalletInfoUseCase.kt
│       ├── SendTransactionUseCase.kt
│       ├── RefreshBalanceUseCase.kt
│       └── LogoutUseCase.kt
├── presentation/
│   ├── navigation/
│   │   └── NavGraph.kt
│   ├── login/
│   │   ├── LoginScreen.kt
│   │   └── LoginViewModel.kt
│   ├── wallet/
│   │   ├── WalletScreen.kt
│   │   ├── WalletViewModel.kt
│   │   └── WalletState.kt
│   └── send/
│       ├── SendTransactionScreen.kt
│       ├── SendTransactionViewModel.kt
│       └── SendTransactionState.kt
├── ui/theme/
├── TestnetWalletApp.kt
└── MainActivity.kt
```

## Setup Instructions

### Prerequisites

- Android Studio Ladybug (2024.2.1) or newer
- JDK 17
- Android SDK 35 (compileSdk)
- Android device/emulator with API 28+ (minSdk)

### Configuration

1. **Clone the repository**

   ```bash
   git clone https://github.com/erbakg/crypto-wallet-app.git
   cd crypto-wallet-app
   ```

2. **Configure Dynamic Environment ID**

   Create or edit `local.properties` in the project root:

   ```properties
   # Dynamic SDK Environment ID (required)
   DYNAMIC_ENVIRONMENT_ID=your_dynamic_environment_id
   ```

   Get your Environment ID from [Dynamic Dashboard](https://app.dynamic.xyz).

3. **Build the project**

   ```bash
   ./gradlew assembleDebug
   ```

4. **Install on device**
   ```bash
   ./gradlew installDebug
   ```

### Getting Test ETH (Sepolia)

To test transactions, you need SepoliaETH from a faucet:

- [Google Cloud Faucet](https://cloud.google.com/application/web3/faucet/ethereum/sepolia)
- [Alchemy Sepolia Faucet](https://www.alchemy.com/faucets/ethereum-sepolia)
- [Infura Sepolia Faucet](https://www.infura.io/faucet/sepolia)

## Screens

### 1. Login Screen

- Single "Login with Dynamic" button
- Opens Dynamic SDK auth modal (WebView)
- Supports email OTP, social login, passkeys

### 2. Wallet Details Screen

- Wallet address with copy functionality
- Network info: Sepolia (Chain ID: 11155111)
- ETH balance with pull-to-refresh
- Send Transaction button
- Logout button

### 3. Send Transaction Screen

- Recipient address input with validation
- Amount input (ETH)
- Send button with loading state
- Success card with transaction hash
- Link to Sepolia Etherscan

## Screenshots

_Add screenshots here after running the app_

| Login                           | Wallet                            | Send Transaction              |
| ------------------------------- | --------------------------------- | ----------------------------- |
| ![Login](screenshots/login.png) | ![Wallet](screenshots/wallet.png) | ![Send](screenshots/send.png) |

## State Management

All ViewModels use a consistent UiState pattern:

```kotlin
sealed interface UiState<out T> {
    data object Idle : UiState<Nothing>
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String) : UiState<Nothing>
}
```

## Code Quality

- [x] Clean Architecture (data/domain/presentation)
- [x] MVVM with StateFlow
- [x] Hilt Dependency Injection
- [x] No business logic in Composables
- [x] No hardcoded strings (strings.xml)
- [x] No public MutableStateFlow
- [x] CoroutineExceptionHandler in ViewModels
- [x] Proper error handling

## Build Configuration

| Property    | Value      |
| ----------- | ---------- |
| compileSdk  | 35         |
| minSdk      | 28         |
| targetSdk   | 34         |
| Kotlin      | 2.0.21     |
| Compose BOM | 2024.12.01 |

## License

MIT License

## Author

Erbol - 2024
