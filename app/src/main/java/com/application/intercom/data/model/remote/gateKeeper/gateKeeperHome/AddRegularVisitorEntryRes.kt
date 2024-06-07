package com.application.intercom.data.model.remote.gateKeeper.gateKeeperHome

data class AddRegularVisitorEntryRes(
    var data: Data,
    var message: String,
    var status: Int
) {
    data class Data(
        var __v: Int,
        var _id: String,
        var createdAt: String,
        var currentStatus: String,
        var flatId: String,
        var inTime: String,
        var is_delete: Boolean,
        var status: String,
        var updatedAt: String,
        var visitorId: String
    )
}