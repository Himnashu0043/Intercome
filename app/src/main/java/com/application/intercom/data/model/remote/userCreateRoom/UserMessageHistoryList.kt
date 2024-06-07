package com.application.intercom.data.model.remote.userCreateRoom


data class SocketMessageResponse(
    var message: UserMessageHistoryList.Data2
)
data class UserMessageHistoryList(
    var Data: ArrayList<Data2>,
    var response_code: Int
) {
    data class Data2(
        var chatType: String,
        var message: String,
        var profile_pic: String,
        var reciever: String,
        var roomId: String,
        var seen_status: Boolean,
        var sender: String,
        var createdAt: String,
        var msgType:String?=null
    )
}

/*{
    "message": "hiiiii",
    "chatType": "PROPERTY",
    "roomId": "645decfb6e3848ecf5b6458f648063a309e67e30ac2d5be8",
    "link": [

    ],
    "sender": "645decfb6e3848ecf5b6458f",
    "reciever": "648063a309e67e30ac2d5be8",
    "profile_pic": "",
    "msgType": "message",
    "seen_status": false,
    "_id": "64ae7a259f2b75aa219feea1",
    "createdAt": "2023-07-12T10:02:13.261Z",
    "updatedAt": "2023-07-12T10:02:13.261Z",
    "__v": 0
  }*/