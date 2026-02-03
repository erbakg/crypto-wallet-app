package com.erbol.testnetwallet.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "crypto_wallet_prefs")

/**
 * Local storage for session data using DataStore.
 * Handles secure storage of wallet credentials and session info.
 */
@Singleton
class SessionStorage @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private companion object {
        val KEY_JWT_TOKEN = stringPreferencesKey("jwt_token")
        val KEY_USER_EMAIL = stringPreferencesKey("user_email")
        val KEY_WALLET_ADDRESS = stringPreferencesKey("wallet_address")
        val KEY_PRIVATE_KEY = stringPreferencesKey("private_key")
        val KEY_VERIFICATION_ID = stringPreferencesKey("verification_id")
    }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { prefs ->
        !prefs[KEY_WALLET_ADDRESS].isNullOrEmpty() && !prefs[KEY_PRIVATE_KEY].isNullOrEmpty()
    }

    suspend fun saveSession(
        jwtToken: String,
        email: String,
        walletAddress: String,
        privateKey: String
    ) {
        context.dataStore.edit { prefs ->
            prefs[KEY_JWT_TOKEN] = jwtToken
            prefs[KEY_USER_EMAIL] = email
            prefs[KEY_WALLET_ADDRESS] = walletAddress
            prefs[KEY_PRIVATE_KEY] = privateKey
        }
    }

    suspend fun saveVerificationId(verificationId: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_VERIFICATION_ID] = verificationId
        }
    }

    suspend fun getVerificationId(): String? {
        return context.dataStore.data.first()[KEY_VERIFICATION_ID]
    }

    suspend fun getWalletAddress(): String? {
        return context.dataStore.data.first()[KEY_WALLET_ADDRESS]
    }

    suspend fun getPrivateKey(): String? {
        return context.dataStore.data.first()[KEY_PRIVATE_KEY]
    }

    suspend fun getEmail(): String? {
        return context.dataStore.data.first()[KEY_USER_EMAIL]
    }

    suspend fun clearSession() {
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }
}
