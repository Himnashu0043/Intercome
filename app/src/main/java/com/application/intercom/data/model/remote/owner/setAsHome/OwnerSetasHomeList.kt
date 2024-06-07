package com.application.intercom.data.model.remote.owner.setAsHome

data class OwnerSetasHomeList(
    var data: Data,
    var message: String,
    var status: Int
) {
    data class Data(
        var __v: Int,
        var _id: String,
        var bathroom: Int,
        var bedroom: Int,
        var buildingId: String,
        var createdAt: String,
        var document: String,
        var flatStatus: String,
        var is_assign: Boolean,
        var is_delete: Boolean,
        var is_home: Boolean,
        var name: String,
        var owner: String,
        var projectId: String,
        var sqft: Int,
        var status: String,
        var updatedAt: String
    )
}