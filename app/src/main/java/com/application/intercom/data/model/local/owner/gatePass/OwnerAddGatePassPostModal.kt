package com.application.intercom.data.model.local.owner.gatePass

data class OwnerAddGatePassPostModal(
    var contactName: String,
    var description: String,
    var exitDate: String,
    var exitTime: String,
    var flatId: String,
    var phoneNumber: String,
    var photo: List<Any>
)