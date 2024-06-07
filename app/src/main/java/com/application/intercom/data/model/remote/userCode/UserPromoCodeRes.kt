package com.application.intercom.data.model.remote.userCode

data class UserPromoCodeRes(
    var data: Data,
    var message: String,
    var status: Int
) {
    data class Data(
        var __v: Int,
        var _id: String,
        var coupon_number: String,
        var createdAt: String,
        var deleteStatus: Boolean,
        var discount: Int,
        var fromDate: String,
        var is_delete: Boolean,
        var perUserLimit: Int,
        var status: String,
        var title: String,
        var toDate: String,
        var type: String,
        var updatedAt: String,
        var userLimit: Int
    )
}