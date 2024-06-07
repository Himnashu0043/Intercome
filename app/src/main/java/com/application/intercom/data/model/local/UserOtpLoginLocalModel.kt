package com.application.intercom.data.model.local

data class UserOtpLoginLocalModel(
    val mobile: String,
    val countryCode:String,
    val otp: Int,
    val role: String,
    val defaultLanguage: String,
    val lat:Double,
    val long:Double
)