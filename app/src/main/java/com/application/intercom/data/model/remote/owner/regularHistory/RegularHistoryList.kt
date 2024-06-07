package com.application.intercom.data.model.remote.owner.regularHistory

data class RegularHistoryList(
    var data: Data,
    var message: String,
    var status: Int
) {
    data class Data(
        var result: ArrayList<Result>
    ):java.io.Serializable {
        data class Result(
            var __v: Int,
            var _id: String,
            var addedBy: String,
            var address: String,
            var buildingId: String,
            var createdAt: String,
            var createdBy: String,
            var document: ArrayList<String>,
            var flatId: String,
            var flatInfo: ArrayList<FlatInfo>,
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
            var visitorType: String,
            var currentStatus: String? = null,
            var entryTime:String?,
            var exitTime:String?


        ):java.io.Serializable {
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
            ):java.io.Serializable
        }
    }
}