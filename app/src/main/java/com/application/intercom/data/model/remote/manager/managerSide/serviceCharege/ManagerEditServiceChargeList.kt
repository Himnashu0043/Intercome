package com.application.intercom.data.model.remote.manager.managerSide.serviceCharege

data class ManagerEditServiceChargeList(
    var `data`: Data,
    var message: String,
    var status: Int
) {
    data class Data(
        var __v: Int,
        var _id: String,
        var amount: Int,
        var buildingId: String,
        var createdAt: String,
        var date: String,
        var flatId: String,
        var is_active: Boolean,
        var is_delete: Boolean,
        var is_notify: Boolean,
        var manager: String,
        var serviceChargeType: String,
        var status: String,
        var updatedAt: String,
        var userBillStatus: String
    )
}