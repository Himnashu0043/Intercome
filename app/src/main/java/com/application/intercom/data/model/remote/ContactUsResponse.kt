package com.application.intercom.data.model.remote

data class ContactUsResponse(
    val status: Int,
    val message: String,
    val data: Data
) {
    data class Data(
        val _id: String,
        val title: String,
        val data: String,
        val contactNo: String,
        val email: String,
        val address: String,
        val updatedAt: String
    )

}
