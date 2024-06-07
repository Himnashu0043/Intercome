package com.application.intercom.data.model.local

data class UserVerifyOtpCommonLocalModel(
    val mobile: String,
    val email: String,
    val otp: Int
)