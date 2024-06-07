package com.application.intercom.data.model.local.gateKeeper

data class AddGatePassPostModel(
    var activity: String,
    var contactName: String,
    var description: String,
    var entryTime: String,
    var flatId: String,
    var profilePic:String,
    var phoneNumber: String,
    var photo: List<String>
)