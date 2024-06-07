package com.application.intercom.data.model.remote.tenant.tenantSide.tenantBill

data class TenanaPendingListRes(
    var `data`: Data,
    var message: String,
    var status: Int
) {
    data class Data(
        var result: List<Result>
    ) {
        data class Result(
            var __v: Int,
            var _id: String,
            var amount: Int,
            var billType: String,
            var buildingId: String,
            var createdAt: String,
            var date: String,
            var flatId: String,
            var flatInfo: List<FlatInfo>,
            var is_active: Boolean,
            var is_delete: Boolean,
            var is_notify: Boolean,
            var owner: String,
            var referenceNo: String,
            var serviceCategory: List<Any>,
            var status: String,
            var tenant: String,
            var updatedAt: String,
            var uploadDocument: String,
            var userBillStatus: String
        ) {
            data class FlatInfo(
                var __v: Int,
                var _id: String,
                var bathroom: Int,
                var bedroom: Int,
                var buildingId: String,
                var createdAt: String,
                var document: String,
                var flatStatus: String,
                var is_assign: Boolean,
                var is_delete: Boolean,
                var name: String,
                var owner: String,
                var sqft: Int,
                var status: String,
                var tenant: String,
                var tenantId: String,
                var updatedAt: String
            )
        }
    }
}