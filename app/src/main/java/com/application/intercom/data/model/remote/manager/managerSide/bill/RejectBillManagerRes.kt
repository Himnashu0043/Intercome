package com.application.intercom.data.model.remote.manager.managerSide.bill

data class RejectBillManagerRes(
    var `data`: Data?,
    var message: String?,
    var status: Int?
) {
    data class Data(
        var __v: Int?,
        var _id: String?,
        var addedBy: String?,
        var amount: Int?,
        var associationType: String?,
        var billMonth: String?,
        var billType: String?,
        var billYear: String?,
        var buildingId: String?,
        var createdAt: String?,
        var date: String?,
        var dueDate: String?,
        var flatId: String?,
        var forIncomeReport: String?,
        var is_active: Boolean?,
        var is_bill_type_new: String?,
        var is_category: Boolean?,
        var is_delete: Boolean?,
        var is_notify: Boolean?,
        var is_rent: Boolean?,
        var is_service: Boolean?,
        var manager: String?,
        var notifyDate: String?,
        var notifyEndDate: String?,
        var notifyStartDate: String?,
        var owner: String?,
        var paidDate: String?,
        var payType: String?,
        var projectId: String?,
        var recieptLink: String?,
        var referenceNo: String?,
        var rejectReason: String?,
        var status: String?,
        var subAdminId: String?,
        var tenant: String?,
        var updatedAt: String?,
        var uploadDocument: String?,
        var userBillStatus: String?,
        var userType: String?,
        var voucherNo: String?
    )
}