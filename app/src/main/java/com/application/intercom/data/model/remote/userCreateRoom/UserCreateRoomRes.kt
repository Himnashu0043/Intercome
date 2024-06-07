package com.application.intercom.data.model.remote.userCreateRoom

data class UserCreateRoomRes(
    var Data: Data1,
    var message: String,
    var response_code: Int
) {
    data class Data1(
        var __v: Int,
        var _id: String,
        var chatType: String,
        var createdAt: String,
        var reciverId: String,
        var roomId: String,
        var senderId: String,
        var updatedAt: String
    )
}