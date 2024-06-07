package com.application.intercom.data.model.remote.gateKeeper.gateKeeperHome

data class GetVisitorCategoryList(
    var data: ArrayList<Data>,
    var message: String,
    var status: Int
) {
    data class Data(
        var __v: Int,
        var _id: String,
        var     categoryName: String,
        var createdAt: String,
        var icon: String,
        var is_delete: Boolean,
        var status: String,
        var updatedAt: String
    )
}