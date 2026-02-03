package com.erbol.testnetwallet.data.remote.dto

import com.google.gson.annotations.SerializedName

data class EmailVerificationCreateRequest(
    @SerializedName("email")
    val email: String
)

data class EmailVerificationCreateResponse(
    @SerializedName("verificationUUID")
    val verificationUUID: String
)
