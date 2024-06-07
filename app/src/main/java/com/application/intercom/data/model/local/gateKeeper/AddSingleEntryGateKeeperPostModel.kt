package com.application.intercom.data.model.local.gateKeeper

data class AddSingleEntryGateKeeperPostModel(
    var address: String,
    var buildingId: String,
    var flatId: String,
    var mobileNumber: String,
    var note: String,
    var photo: String,
    var visitCategoryId: String,
    var visitCategoryName: String,
    var visitorName: String
)