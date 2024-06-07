package com.application.intercom.data.model.remote.manager.managerSide.newflow

data class UnPaidExpensesManagerRes(
    var `data`: Data,
    var message: String,
    var status: Int
) : java.io.Serializable {
    data class Data(
        var result: ArrayList<Result>,
        var total: Int?
    ) : java.io.Serializable {
        data class Result(
            var __v: Int?,
            var _id: String?,
            var addedBy: String?,
            var billStatus: String?,
            var buildingId: String?,
            var categoryId: String?,
            var createdAt: String?,
            var createdBy: String?,
            var date: String?,
            var expenseAmount: Int?,
            var expenseDetail: String?,
            var expenseName: String?,
            var is_active: Boolean?,
            var is_delete: Boolean?,
            var refernceId: String?,
            var updatedAt: String?,
            var uploadBill: ArrayList<String>?
        ) : java.io.Serializable
    }
}