package com.application.intercom.data.model.remote.owner.addRegularEntry

data class AddRegularEntryOwnerRes(
    var `data`: Data,
    var message: String,
    var status: Int
) {
    data class Data(
        var __v: Int,
        var _id: String,
        var addedBy: String,
        var address: String,
        var buildingId: String,
        var createdAt: String,
        var createdBy: String,
        var document: List<String>,
        var flatId: String,
        var fromDate: String,
        var fromTime: String,
        var is_delete: Boolean,
        var mobileNumber: String,
        var note: String,
        var photo: String,
        var regularVisitorStatus: String,
        var status: String,
        var tenant: String,
        var toDate: String,
        var toTime: String,
        var updatedAt: String,
        var visitCategoryId: String,
        var visitCategoryName: String,
        var visitorName: String,
        var visitorStatus: String,
        var visitorType: String
    )
}