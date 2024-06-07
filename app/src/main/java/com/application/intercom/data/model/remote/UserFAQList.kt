package com.application.intercom.data.model.remote

data class UserFAQList(
    var `data`: List<Data>,
    var message: String,
    var status: Int
) {
    data class Data(
        var __v: Int,
        var _id: String,
        var adminId: String,
        var answer: String,
        var createdAt: String,
        var is_active: Boolean,
        var is_delete: Boolean,
        var planType: String,
        var question: String,
        var updatedAt: String
    )
}