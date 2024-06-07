package com.application.intercom.data.model.local.owner.gatePass

data class OwnerEditGatePassPostModel(
    var contactName: String,
    var description: String,
    var exitDate: String,
    var exitTime: String,
    var flatId: String,
    var gatePassId: String,
    var phoneNumber: String,
    var photo: List<String>
)