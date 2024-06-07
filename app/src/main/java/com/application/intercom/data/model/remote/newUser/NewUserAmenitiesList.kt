package com.application.intercom.data.model.remote.newUser

data class NewUserAmenitiesList(
    var data: ArrayList<Data>,
    var message: String,
    var status: Int
) {
    data class Data(
        var __v: Int,
        var _id: String,
        var createdAt: String,
        var image: String,
        var is_active: Boolean,
        var name: String,
        var updatedAt: String,
        var isSelect: Boolean = false
    )
}