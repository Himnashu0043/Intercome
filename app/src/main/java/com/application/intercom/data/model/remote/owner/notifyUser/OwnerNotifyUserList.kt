package com.application.intercom.data.model.remote.owner.notifyUser

data class OwnerNotifyUserList(
    var `data`: Data,
    var message: String,
    var status: Int
) {
    data class Data(
        var __v: Int,
        var _id: String,
        var amount: Int,
        var billType: String,
        var buildingId: String,
        var createdAt: String,
        var date: String,
        var flatId: String,
        var is_active: Boolean,
        var is_delete: Boolean,
        var is_notify: Boolean,
        var manager: String,
        var notifyDate: String,
        var notifyEndDate: String,
        var notifyStartDate: String,
        var owner: String,
        var paidDate: String,
        var payType: String,
        var referenceNo: String,
        var serviceChargeType: String,
        var status: String,
        var updatedAt: String,
        var uploadDocument: String,
        var userBillStatus: String,
        var userType: String
    )
}