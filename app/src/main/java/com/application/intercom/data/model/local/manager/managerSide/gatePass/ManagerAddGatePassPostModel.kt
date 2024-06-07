package com.application.intercom.data.model.local.manager.gatePass

data class ManagerAddGatePassPostModel(
    var activity: String,
    var description: String,
    var entryTime: String,
    var flatId: String,
    var phoneNumber: String,
    var photo: List<String>
)