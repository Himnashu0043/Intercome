package com.application.intercom.data.model.remote

class SendPhoneOtpResponse(
    val status: Int,
    val message: String,
    val data: Data,
) {

    data class Data(
        val otp: String
    )
}