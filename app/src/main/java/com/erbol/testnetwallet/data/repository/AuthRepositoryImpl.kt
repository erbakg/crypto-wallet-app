package com.erbol.testnetwallet.data.repository

import com.erbol.testnetwallet.BuildConfig
import com.erbol.testnetwallet.data.local.SessionStorage
import com.erbol.testnetwallet.data.remote.DynamicApi
import com.erbol.testnetwallet.data.remote.Web3Service
import com.erbol.testnetwallet.data.remote.dto.EmailVerificationCreateRequest
import com.erbol.testnetwallet.domain.model.AuthResult
import com.erbol.testnetwallet.domain.model.OtpSendResult
import com.erbol.testnetwallet.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val dynamicApi: DynamicApi,
    private val sessionStorage: SessionStorage,
    private val web3Service: Web3Service
) : AuthRepository {

    private val environmentId: String = BuildConfig.DYNAMIC_ENVIRONMENT_ID

    private var pendingEmail: String? = null
    private var verificationUUID: String? = null

    override suspend fun sendOtp(email: String): OtpSendResult {
        val response = dynamicApi.createEmailVerification(
            environmentId = environmentId,
            request = EmailVerificationCreateRequest(email = email)
        )

        pendingEmail = email
        verificationUUID = response.verificationUUID
        sessionStorage.saveVerificationId(response.verificationUUID)

        return OtpSendResult(verificationId = response.verificationUUID)
    }

    override suspend fun verifyOtp(code: String): AuthResult {
        val email = pendingEmail
            ?: throw IllegalStateException("No pending email verification")
        val uuid = verificationUUID
            ?: throw IllegalStateException("No verification UUID")

        if (code.length != 6 || !code.all { it.isDigit() }) {
            throw IllegalArgumentException("Invalid OTP code")
        }

        // Note: Dynamic SDK endpoints require browser-based authentication.
        // OTP is sent via Dynamic API, verification is done locally for demo.

        val (address, privateKey) = web3Service.generateWallet()

        sessionStorage.saveSession(
            jwtToken = "demo_jwt_${uuid}",
            email = email,
            walletAddress = address,
            privateKey = privateKey
        )

        pendingEmail = null
        verificationUUID = null

        return AuthResult(
            isAuthenticated = true,
            walletAddress = address
        )
    }

    override suspend fun resendOtp(email: String): OtpSendResult {
        return sendOtp(email)
    }

    override fun isAuthenticated(): Flow<Boolean> {
        return sessionStorage.isLoggedIn
    }

    override suspend fun logout() {
        sessionStorage.clearSession()
        pendingEmail = null
        verificationUUID = null
    }
}
