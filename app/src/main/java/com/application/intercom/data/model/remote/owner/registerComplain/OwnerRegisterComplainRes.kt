package com.application.intercom.data.model.remote.owner.registerComplain

data class OwnerRegisterComplainRes(
    var data: Data,
    var message: String,
    var status: Int
) {
    data class Data(
        var __v: Int,
        var _id: String,
        var compId: String,
        var complainName: String,
        var complaint_by: String,
        var createdAt: String,
        var description: String,
        var managerAction: String,
        var photo: List<String>,
        var serviceCategory: String,
        var status: String,
        var updatedAt: String
    )
}