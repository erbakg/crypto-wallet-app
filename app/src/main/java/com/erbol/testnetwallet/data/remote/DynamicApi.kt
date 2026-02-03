package com.erbol.testnetwallet.data.remote

import com.erbol.testnetwallet.data.remote.dto.EmailVerificationCreateRequest
import com.erbol.testnetwallet.data.remote.dto.EmailVerificationCreateResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface DynamicApi {

    @POST("sdk/{environmentId}/emailVerifications/create")
    suspend fun createEmailVerification(
        @Path("environmentId") environmentId: String,
        @Body request: EmailVerificationCreateRequest
    ): EmailVerificationCreateResponse

    companion object {
        const val BASE_URL = "https://app.dynamicauth.com/api/v0/"
    }
}
