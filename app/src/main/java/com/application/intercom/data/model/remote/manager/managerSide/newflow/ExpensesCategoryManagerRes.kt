package com.application.intercom.data.model.remote.manager.managerSide.newflow

data class ExpensesCategoryManagerRes(
    var data: ArrayList<Data>,
    var message: String,
    var status: Int
) {
    data class Data(
        var __v: Int,
        var _id: String,
        var adminId: String,
        var createdAt: String,
        var image: String,
        var is_delete: Boolean,
        var name: String,
        var status: String,
        var updatedAt: String
    )
}