package com.application.intercom.data.model.local.manager.managerSide.gateKeeper

data class ManagerEditGateKeeperPostModel(
    var _id: String,
    var document: String,
    var documentBack: String,
    var documentFront: String,
    var fatherName: String,
    var fullName: String,
    var mobileNumber: String,
    var motherName: String,
    var nidBack: String,
    var nidFront: String,
    var nidNumber: String,
    var password: String,
    var photo: String,
    var referenceMobile: String,
    var referenceName: String,
    var referenceNidBack: String,
    var referenceNidFront: String,
    var referenceNidNumber: String,
    var shiftEndTime: String,
    var shiftStartTime: String
)