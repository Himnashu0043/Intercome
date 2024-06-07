package com.application.intercom.data.model.remote.getTutorial

data class GetTutorialList(
    var `data`: List<Data>,
    var message: String,
    var status: Int
) {
    data class Data(
        var __v: Int,
        var _id: String,
        var createdAt: String,
        var `file`: String,
        var is_active: Boolean,
        var is_delete: Boolean,
        var status: String,
        var tutorial_number: String,
        var updatedAt: String,
        var userType: String
    )
}