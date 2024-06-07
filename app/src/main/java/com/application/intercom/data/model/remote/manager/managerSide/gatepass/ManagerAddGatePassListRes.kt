package com.application.intercom.data.model.remote.manager.managerSide.gatepass

data class ManagerAddGatePassListRes(
    var `data`: Data,
    var message: String,
    var status: Int
) {
    data class Data(
        var __v: Int,
        var _id: String,
        var activity: String,
        var buildingId: String,
        var createdAt: String,
        var createdBy: String,
        var description: String,
        var entryTime: String,
        var flatId: String,
        var is_delete: Boolean,
        var passNo: String,
        var phoneNumber: String,
        var photo: List<String>,
        var status: String,
        var updatedAt: String
    )
}