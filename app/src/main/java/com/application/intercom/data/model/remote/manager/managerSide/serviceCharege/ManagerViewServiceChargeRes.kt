package com.application.intercom.data.model.remote.manager.managerSide.serviceCharege

data class ManagerViewServiceChargeRes(
    var data: Data,
    var message: String,
    var status: Int
) : java.io.Serializable {
    data class Data(
        var __v: Int,
        var _id: String? = null,
        var amount: Int,
        var buildingId: String,
        var createdAt: String,
        var date: String,
        var is_active: Boolean,
        var is_delete: Boolean,
        var is_notify: Boolean,
        var manager: String,
        var serviceChargeType: String,
        var status: String,
        var updatedAt: String,
        var userBillStatus: String
    ) : java.io.Serializable
}