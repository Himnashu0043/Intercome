package com.application.intercom.data.model.remote

data class UserAdvertismentResponse(
    val status: Int,
    val message: String,
    val data: ArrayList<Data>
) {
    data class Data(
        val _id: String,
        val advertisement_number: String,
        val date: String,
        val title: String,
        val image: String,
        val url: String,
        val validFor: ArrayList<String>,
        val status: String,
        val is_delete: Boolean,
        val createdAt: String,
        val updatedAt: String,
        val __v: Int

    )

}
