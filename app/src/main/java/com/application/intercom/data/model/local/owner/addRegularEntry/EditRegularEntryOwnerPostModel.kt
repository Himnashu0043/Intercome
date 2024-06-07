package com.application.intercom.data.model.local.owner.addRegularEntry

data class EditRegularEntryOwnerPostModel(
    var address: String,
    var buildingId: String,
    var document: List<String>,
    var flatId: String,
    var fromDate: String,
    var fromTime: String,
    var mobileNumber: String,
    var note: String,
    var photo: String,
    var toDate: String,
    var toTime: String,
    var visitCategoryId: String,
    var visitCategoryName: String,
    var visitorId: String,
    var visitorName: String
)