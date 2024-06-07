package com.application.intercom.data.model.remote.owner.gatePass

data class OwnerAddGatePassList(
    var `data`: Data,
    var message: String,
    var status: Int
) {
    data class Data(
        var __v: Int,
        var _id: String,
        var buildingId: String,
        var contactName: String,
        var createdAt: String,
        var createdBy: String,
        var description: String,
        var exitTime: String,
        var flatId: String,
        var is_delete: Boolean,
        var passNo: String,
        var phoneNumber: String,
        var photo: List<Any>,
        var status: String,
        var toDate: String,
        var updatedAt: String
    )
}