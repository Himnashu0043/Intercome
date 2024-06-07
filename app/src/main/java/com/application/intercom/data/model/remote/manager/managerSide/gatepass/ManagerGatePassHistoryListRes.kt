package com.application.intercom.data.model.remote.manager.managerSide.gatepass

data class ManagerGatePassHistoryListRes(
    var data: Data,
    var message: String,
    var status: Int
) : java.io.Serializable {
    data class Data(
        var result: ArrayList<Result>
    ) : java.io.Serializable {
        data class Result(
            var __v: Int,
            var _id: String,
            var activity: String,
            var buildingId: String,
            var createdAt: String,
            var createdBy: String,
            var description: String,
            var entryTime: String,
            var flatId: String,
            var flatInfo: ArrayList<FlatInfo>,
            var is_delete: Boolean,
            var passNo: String,
            var phoneNumber: String,
            var photo: List<String>,
            var status: String,
            var updatedAt: String
        ) : java.io.Serializable {
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
            ) : java.io.Serializable
        }
    }
}