package com.application.intercom.data.model.remote.manager.managerSide.bill

data class AddManagerBillRes(
    var data: ArrayList<Data>?,
    var message: String,
    var status: Int
) {
    /*data class Data(
        var __v: Int,
        var _id: String,
        var amount: Int,
        var billType: String,
        var buildingId: String,
        var categoryId: String,
        var createdAt: String,
        var date: String,
        var flatId: String,
        var is_active: Boolean,
        var is_delete: Boolean,
        var is_notify: Boolean,
        var manager: String,
        var status: String,
        var tenant: String,
        var updatedAt: String,
        var userBillStatus: String,
        var userType: String
    )*/
    data class Data(
        var __v: Int?,
        var _id: String?,
        var addedBy: String?,
        var amount: Int?,
        var billMonth: String?,
        var billType: String?,
        var billYear: String?,
        var buildingId: String?,
        var categoryId: String?,
        var createdAt: String?,
        var date: String?,
        var flatId: String?,
        var forIncomeReport: String?,
        var is_active: Boolean?,
        var is_category: Boolean?,
        var is_delete: Boolean?,
        var is_notify: Boolean?,
        var manager: String?,
        var owner: String?,
        var projectId: String?,
        var status: String?,
        var subAdminId: String?,
        var tenant: String?,
        var updatedAt: String?,
        var userBillStatus: String?,
        var userType: String?,
        var voucherNo: String?
    )
}