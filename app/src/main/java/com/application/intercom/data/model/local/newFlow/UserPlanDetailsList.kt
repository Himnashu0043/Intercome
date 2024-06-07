package com.application.intercom.data.model.local.newFlow

data class UserPlanDetailsList(
    var `data`: List<Data>,
    var message: String,
    var status: Int
) {
    data class Data(
        var __v: Int,
        var _id: String,
        var adminId: String,
        var createdAt: String,
        var description: String,
        var is_delete: Boolean,
        var title: String,
        var updatedAt: String
    )
}