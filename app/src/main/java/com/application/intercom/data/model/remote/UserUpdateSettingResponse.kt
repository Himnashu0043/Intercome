package com.application.intercom.data.model.remote

class UserUpdateSettingResponse(
    val status: Int,
    val message: String,
    val data: Data
) {
    data class Data(
        val _id: String,
        val role: String,
        val createdBy: String,
        val phoneNumber: String,
        val profilePic: String,
        val description: String,
        val location: Location,
        val status: String,
        val defaultLanguage: String,
        val totalOrders: Int,
        val totalSpent: Int,
        val totalCancelOrders: Int,
        val totalCancelAmount: Int,
        val totalReturnOrders: Int,
        val totalReturnAmount: Int,
        val notification_status: Boolean,
        val referralCount: Int,
        val reffer_code_use_status: Boolean,
        val reffer_code_generated: Boolean,
        val is_delete: Boolean,
        val preferences: ArrayList<Any>,
        val createdAt: String,
        val updatedAt: String,
        val __v: Int,
        val jwtToken: String,
        val password: String,
        val plainPassword: String,
        val lastActivityDate: String,
        val lastActivityTimeStamp: Int,
        val deviceToken: String
    )
}