package com.application.intercom.data.model.remote.gateKeeper.gateKeeperHome

data class SingleEntryHistoryList(
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
            var document: List<Any>,
            var entryTime: String,
            var flatId: String,
            var flatInfo: ArrayList<FlatInfo>,
            var is_delete: Boolean,
            var mobileNumber: String,
            var note: String,
            var owner: String,
            var photo: String,
            var regularVisitorStatus: String,
            var status: String,
            var updatedAt: String,
            var visitCategoryId: String,
            var visitCategoryName: String?,
            var visitorName: String,
            var visitorStatus: String,
            var visitorType: String?,
            var exitTime: String,
            var notifyDate: String?,


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
                var updatedAt: String
            )
        }
    }
}