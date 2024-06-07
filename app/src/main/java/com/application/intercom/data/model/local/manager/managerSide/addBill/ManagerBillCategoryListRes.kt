package com.application.intercom.data.model.local.manager.managerSide.addBill

data class ManagerBillCategoryListRes(
    var `data`: List<Data>,
    var message: String,
    var status: Int
) {
    data class Data(
        var __v: Int,
        var _id: String,
        var createdAt: String,
        var image: String,
        var is_delete: Boolean,
        var name: String,
        var status: String,
        var updatedAt: String
    )
}