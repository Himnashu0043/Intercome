package com.application.intercom.data.model.local.localModel

data class UserCreateRoomPostModel(
    var message: String,
    var sender: String,
    var reciever: String,
    var roomId: String,
    var chatType: String,
    var msgType: String
)
