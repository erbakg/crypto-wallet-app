# Crypto Wallet - Sepolia Testnet

A production-quality Android cryptocurrency wallet application for the Ethereum Sepolia testnet, built with Kotlin and Jetpack Compose following Clean Architecture principles.

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
│  │ + FormState  │  │ + UiState    │  │ + FormState            │  │
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
│  ┌───────────────────────────────────────────────────────────┐ │
│  │              Domain Models                                │  │
│  │    WalletInfo  │  TransactionResult  │  AuthResult        │  │
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
│  │     ┌──────────────┐          ┌──────────────┐             │ │
│  │     │SessionStorage│          │  Web3Service │             │ │
│  │     │  (DataStore) │          │   (Web3j)    │             │ │
│  │     └──────────────┘          └──────────────┘             │ │
│  └────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────┘
```

### Layer Responsibilities

| Layer            | Responsibility                                                  |
| ---------------- | --------------------------------------------------------------- |
| **Presentation** | UI components (Compose), ViewModels, UI state management        |
| **Domain**       | Business logic, use cases, repository interfaces, domain models |
| **Data**         | Repository implementations, data sources, API services          |

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
| **Blockchain**    | Web3j                         |
| **Network**       | Retrofit + OkHttp             |
| **Storage**       | DataStore Preferences         |
| **Build System**  | Gradle Kotlin DSL             |

## Project Structure

```
app/src/main/java/com/erbol/testnetwallet/
├── common/                      # Shared utilities
│   ├── Constants.kt             # App-wide constants
│   ├── Extensions.kt            # Kotlin extensions
│   └── UiState.kt               # Generic UI state sealed interface
├── data/
│   ├── local/
│   │   └── SessionStorage.kt    # DataStore for session
│   ├── remote/
│   │   ├── DynamicApiService.kt # Dynamic SDK REST API
│   │   └── Web3Service.kt       # Web3j blockchain service
│   └── repository/
│       ├── AuthRepositoryImpl.kt
│       └── WalletRepositoryImpl.kt
├── di/
│   ├── AppModule.kt             # Hilt providers
│   └── RepositoryModule.kt      # Repository bindings
├── domain/
│   ├── model/
│   │   ├── AuthResult.kt
│   │   ├── TransactionResult.kt
│   │   └── WalletInfo.kt
│   ├── repository/
│   │   ├── AuthRepository.kt    # Interface
│   │   └── WalletRepository.kt  # Interface
│   └── usecase/
│       ├── SendOtpUseCase.kt
│       ├── VerifyOtpUseCase.kt
│       ├── GetWalletInfoUseCase.kt
│       ├── SendTransactionUseCase.kt
│       ├── RefreshBalanceUseCase.kt
│       └── LogoutUseCase.kt
├── presentation/
│   ├── navigation/
│   │   └── NavGraph.kt          # Type-safe navigation
│   ├── login/
│   │   ├── LoginScreen.kt
│   │   ├── LoginScreenPreviews.kt
│   │   ├── LoginViewModel.kt
│   │   ├── LoginState.kt        # LoginFormState
│   │   └── components/
│   ├── wallet/
│   │   ├── WalletScreen.kt
│   │   ├── WalletScreenPreviews.kt
│   │   ├── WalletViewModel.kt
│   │   ├── WalletState.kt       # WalletUiState
│   │   └── components/
│   └── send/
│       ├── SendTransactionScreen.kt
│       ├── SendTransactionScreenPreviews.kt
│       ├── SendTransactionViewModel.kt
│       ├── SendTransactionState.kt  # SendTransactionFormState
│       └── components/
├── ui/theme/                    # Material 3 theming
├── CryptoWalletApp.kt           # @HiltAndroidApp
└── MainActivity.kt              # Single Activity
```

## State Management Pattern

All ViewModels follow a consistent UiState pattern:

```kotlin
sealed interface UiState<out T> {
    data object Idle : UiState<Nothing>
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String) : UiState<Nothing>
}
```

Each ViewModel exposes:

- `uiState: StateFlow<UiState<T>>` - Async operation state (Loading/Success/Error)
- `formState: StateFlow<FormState>` - Form inputs and validation (where applicable)

Example:

```kotlin
@HiltViewModel
class LoginViewModel @Inject constructor(...) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<AuthResult>>(UiState.Idle)
    val uiState: StateFlow<UiState<AuthResult>> = _uiState.asStateFlow()

    private val _formState = MutableStateFlow(LoginFormState())
    val formState: StateFlow<LoginFormState> = _formState.asStateFlow()
}
```

## Setup Instructions

### Prerequisites

- Android Studio Ladybug (2024.2.1) or newer
- JDK 17
- Android SDK 35 (compileSdk)
- Android device/emulator with API 24+ (minSdk)

### Configuration

1. **Clone the repository**

   ```bash
   git clone <repository-url>
   cd TestnetWallet
   ```

2. **Configure API Keys**

   Create or edit `local.properties` in the project root:

   ```properties
   # Dynamic SDK Environment ID (for OTP authentication)
   DYNAMIC_ENVIRONMENT_ID=your_dynamic_environment_id

   # Infura API Key (for Sepolia RPC connection)
   INFURA_API_KEY=your_infura_api_key
   ```

   Get your keys:
   - **Dynamic Environment ID**: [Dynamic Dashboard](https://app.dynamicauth.com/)
   - **Infura API Key**: [Infura Dashboard](https://app.infura.io/)

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
- [QuickNode Faucet](https://faucet.quicknode.com/ethereum/sepolia)

## Screens

### 1. Login Screen

- Email input with validation
- "Send OTP" button (sends via Dynamic API)
- Error handling for invalid email

### 2. OTP Verification Screen

- 6-digit code input boxes with auto-focus
- Keyboard auto-opens
- Resend code with 60-second cooldown
- Back navigation to email input

### 3. Wallet Details Screen

- Full wallet address with copy functionality
- Network info: Sepolia - 11155111
- ETH balance with pull-to-refresh
- Copy Address action card
- Send Transaction button
- Logout button (red, with confirmation)

### 4. Send Transaction Screen

- Recipient address input with 0x validation
- Amount input (ETH) with numeric validation
- Send button with loading state
- Success card with:
  - Truncated transaction hash (copyable)
  - "View on Etherscan" link
- Stays on same screen after transaction

## Screenshots

_Add screenshots here after running the app_

| Login                           | OTP Verification            | Wallet                            | Send Transaction              |
| ------------------------------- | --------------------------- | --------------------------------- | ----------------------------- |
| ![Login](screenshots/login.png) | ![OTP](screenshots/otp.png) | ![Wallet](screenshots/wallet.png) | ![Send](screenshots/send.png) |

To add screenshots:

1. Run the app on emulator/device
2. Take screenshots of each screen
3. Create `screenshots/` folder in project root
4. Save images as `login.png`, `otp.png`, `wallet.png`, `send.png`

## Assumptions & Trade-offs

### Architecture Decisions

1. **Hybrid Authentication Flow**:
   - OTP sending uses Dynamic SDK REST API (`/emailVerifications/create`)
   - OTP verification is simulated locally (Dynamic SDK requires browser session for verify endpoint)
   - In production, full Dynamic SDK integration would use their native SDK when available

2. **Local Wallet Generation**:
   - Embedded wallets generated locally using Web3j
   - Private keys stored securely in encrypted DataStore
   - Mnemonic-based wallet creation for recovery capability

3. **Single Network**:
   - Hardcoded for Sepolia testnet (chainId: 11155111)
   - Production would require network switching capability

4. **No Transaction History**:
   - Current implementation focuses on sending transactions
   - Transaction history would require Etherscan API integration

### Security Considerations

- Private keys stored in encrypted DataStore (never in plain text)
- API keys loaded from `local.properties` (not committed to git)
- Input validation on all user entries (email, address, amount)
- Repository interfaces enable easy mocking for tests
- No sensitive data exposed in logs

### Code Quality Checklist

- [x] No `!!` operator - All nullability handled with `?.`, `?:`, `let`
- [x] No business logic in Composables - Screens only observe state
- [x] No hardcoded strings - All UI text in `strings.xml`
- [x] No public MutableStateFlow - Only immutable StateFlow exposed
- [x] Exhaustive `when` statements - All sealed class branches handled
- [x] CoroutineExceptionHandler - All exceptions caught in ViewModels
- [x] UiState pattern - Consistent across all ViewModels
- [x] Clean Architecture - Proper layer separation
- [x] SOLID principles - Single responsibility, dependency inversion

## Build Variants

| Variant   | Description                                    |
| --------- | ---------------------------------------------- |
| `debug`   | Development build with logging enabled         |
| `release` | Production build with ProGuard/R8 minification |

## Dependencies

Key dependencies (see `libs.versions.toml` for full list):

```toml
[versions]
kotlin = "2.0.21"
compose-bom = "2024.12.01"
hilt = "2.51.1"
web3j = "4.12.3"
retrofit = "2.11.0"
datastore = "1.1.1"

[libraries]
# Compose
androidx-compose-bom = { group = "androidx.compose", name = "compose-bom", version.ref = "compose-bom" }
androidx-compose-material3 = { group = "androidx.compose.material3", name = "material3" }

# Hilt
hilt-android = { group = "com.google.dagger", name = "hilt-android", version.ref = "hilt" }
hilt-navigation-compose = { group = "androidx.hilt", name = "hilt-navigation-compose", version.ref = "hilt-navigation" }

# Web3
web3j-core = { group = "org.web3j", name = "core", version.ref = "web3j" }
```

## Future Improvements

- [ ] Dynamic SDK native integration (when available for Android)
- [ ] QR code scanner for recipient addresses
- [ ] Transaction history via Etherscan API
- [ ] Multiple wallet support
- [ ] Network switching (mainnet, other testnets)
- [ ] Biometric authentication for transactions
- [ ] Dark mode support
- [ ] Unit tests for ViewModels and UseCases
- [ ] UI tests with Compose Testing
- [ ] CI/CD pipeline (GitHub Actions)

## Troubleshooting

### Build Issues

**Problem**: `Could not find org.web3j:core`
**Solution**: Ensure you have Maven Central in your repositories

**Problem**: `API key not found`
**Solution**: Check that `local.properties` exists and contains valid keys

### Runtime Issues

**Problem**: OTP not received
**Solution**: Check Dynamic Environment ID is correct and email is verified in Dynamic dashboard

**Problem**: Transaction fails
**Solution**: Ensure wallet has sufficient Sepolia ETH for gas fees

## License

MIT License
