package com.application.intercom.data.model.remote.manager.managerSide.gatepass

data class ManagerFlatOfBuildingListRes(
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
            var updatedAt: String
        )
    }
}