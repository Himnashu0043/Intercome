package com.application.intercom.data.model.remote.manager.managerSide.notifyuser

data class ManagerNotifyRes(
    var `data`: Data,
    var message: String,
    var status: Int
) {
    data class Data(
        var __v: Int,
        var _id: String,
        var addedBy: String,
        var amount: Int,
        var billType: String,
        var buildingId: String,
        var categoryId: String,
        var createdAt: String,
        var date: String,
        var flatId: String,
        var is_active: Boolean,
        var is_delete: Boolean,
        var is_notify: Boolean,
        var manager: String,
        var notifyDate: String,
        var status: String,
        var subAdminId: String,
        var tenant: String,
        var updatedAt: String,
        var userBillStatus: String,
        var userType: String
    )
}