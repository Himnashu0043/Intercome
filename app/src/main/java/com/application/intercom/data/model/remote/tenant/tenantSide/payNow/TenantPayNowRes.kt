package com.application.intercom.data.model.remote.tenant.tenantSide.payNow

data class TenantPayNowRes(
    var `data`: Data,
    var message: String,
    var status: Int
) {
    data class Data(
        var __v: Int,
        var _id: String,
        var amount: Int,
        var billType: String,
        var buildingId: String,
        var createdAt: String,
        var date: String,
        var flatId: String,
        var is_active: Boolean,
        var is_delete: Boolean,
        var is_notify: Boolean,
        var owner: String,
        var referenceNo: String,
        var status: String,
        var tenant: String,
        var updatedAt: String,
        var uploadDocument: String,
        var userBillStatus: String
    )
}