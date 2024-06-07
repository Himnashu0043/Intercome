package com.application.intercom.data.model.remote

data class VerifyOtpCommonResponse(
    val status: Int,
    val message: String,
    val data: Data
) {

    data class Data(
        val _id: String,
        val phoneNumber: String,
        val otp: Int,
        val createdA: String,
        val updatedAt: String,
        val __v: Int

    )
}