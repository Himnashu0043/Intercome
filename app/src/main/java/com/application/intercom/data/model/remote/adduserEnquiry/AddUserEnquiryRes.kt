package com.application.intercom.data.model.remote.adduserEnquiry

data class AddUserEnquiryRes(
    var `data`: Data,
    var message: String,
    var status: Int
) {
    data class Data(
        var __v: Int,
        var _id: String,
        var createdAt: String,
        var details: String,
        var email: String,
        var is_delete: Boolean,
        var mobileNumber: String,
        var name: String,
        var status: String,
        var updatedAt: String
    )
}