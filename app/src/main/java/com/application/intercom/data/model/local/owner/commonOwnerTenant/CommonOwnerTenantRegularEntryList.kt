package com.application.intercom.data.model.local.owner.commonOwnerTenant

data class CommonOwnerTenantRegularEntryList(
    var data: Data,
    var message: String,
    var status: Int
) {
    data class Data(
        var result: ArrayList<Result>
    ) {
        data class Result(
            var __v: Int,
            var _id: String,
            var addedBy: String,
            var address: String,
            var buildingId: String,
            var createdAt: String,
            var createdBy: String,
            var document: List<String>,
            var flatId: String,
            var flatInfo: List<FlatInfo>,
            var fromDate: String,
            var fromTime: String,
            var is_delete: Boolean,
            var mobileNumber: String,
            var note: String,
            var photo: String,
            var regularVisitorStatus: String,
            var status: String,
            var tenant: String,
            var toDate: String,
            var toTime: String,
            var updatedAt: String,
            var visitCategoryId: String,
            var visitCategoryName: String,
            var visitorName: String,
            var visitorStatus: String,
            var visitorType: String
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
                var projectId: String,
                var sqft: Int,
                var status: String,
                var tenant: String,
                var tenantId: String,
                var updatedAt: String
            )
        }
    }
}