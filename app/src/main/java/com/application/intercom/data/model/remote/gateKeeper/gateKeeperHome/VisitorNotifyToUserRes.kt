package com.application.intercom.data.model.remote.gateKeeper.gateKeeperHome

data class VisitorNotifyToUserRes(
    var `data`: Data,
    var message: String,
    var status: Int
) {
    data class Data(
        var __v: Int,
        var _id: String,
        var createdAt: String,
        var isSeen: Boolean,
        var notiMessage: String,
        var notiTitle: String,
        var owner: String,
        var status: String,
        var type: String,
        var updatedAt: String,
        var userType: List<Any>,
        var visitorId: String
    )
}