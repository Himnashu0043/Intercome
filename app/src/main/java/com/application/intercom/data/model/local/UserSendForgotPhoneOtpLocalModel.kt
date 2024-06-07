package com.application.intercom.data.model.local

data class  UserSendForgotPhoneOtpLocalModel(
    val mobile: String,
    val countryCode: String,
    val role: String,
    val screen: String
)