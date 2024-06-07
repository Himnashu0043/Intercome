package com.application.intercom.data.model.remote.gateKeeper.gateKeeperHome

data class FlatListOfBuildingGateKeeperListRes(
    var `data`: Data,
    var message: String,
    var status: Int
) {
    data class Data(
        var result: List<Result>
    ) {
        data class Result(
            var _id: String,
            var bathroom: Int,
            var bedroom: Int,
            var buildingId: String,
            var createdAt: String,
            var flatStatus: String,
            var name: String,
            var owner: String,
            var ownerInfo: List<OwnerInfo>,
            var sqft: Int,
            var tenantInfo: List<Any>
        ) {
            data class OwnerInfo(
                var _id: String,
                var phoneNumber: String
            )
        }
    }
}