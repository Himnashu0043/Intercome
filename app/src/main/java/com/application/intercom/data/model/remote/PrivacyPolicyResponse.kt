package com.application.intercom.data.model.remote

data class PrivacyPolicyResponse(
    val status: Int,
    val message: String,
    val data: Data
) {
    data class Data(
        val _id: String,
        val title: String,
        val data: String,
        val updatedAt: String
    )

}
