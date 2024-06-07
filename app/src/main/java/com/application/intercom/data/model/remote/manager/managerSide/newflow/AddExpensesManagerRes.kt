package com.application.intercom.data.model.remote.manager.managerSide.newflow

data class AddExpensesManagerRes(
    var data: Data,
    var message: String,
    var status: Int
) {
    data class Data(
        var __v: Int,
        var _id: String?,
        var addStatus: Boolean,
        var ballancedAmount: String,
        var buildingId: String,
        var createdAt: String,
        var createdBy: String,
        var date: String,
        var earningAmount: String,
        var expenseAmount: String?,
        var expenseDetail: String,
        var is_delete: Boolean,
        var status: String,
        var type: String,
        var updatedAt: String,
        var uploadBill: ArrayList<String>
    )
}