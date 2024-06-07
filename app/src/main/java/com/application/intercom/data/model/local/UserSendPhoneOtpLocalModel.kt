package com.application.intercom.data.model.local

data class UserSendPhoneOtpLocalModel(
    val mobile: String,
    val countryCode: String,
    val role: String,
    val screen: String,
    val deviceToken: String,
    val deviceType: String,

)