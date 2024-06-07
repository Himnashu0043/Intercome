package com.application.intercom.data.model.remote.owner.ownerHome

data class OwnerAdvertisementRes(
    var data: ArrayList<Data>,
    var message: String,
    var status: Int
) {
    data class Data(
        var __v: Int,
        var _id: String,
        var adminId: String,
        var advertisementData: ArrayList<AdvertisementData>,
        var buildingId: ArrayList<String>,
        var createdAt: String,
        var is_delete: Boolean,
        var status: String,
        var updatedAt: String,
        var validFor: String
    ) {
        data class AdvertisementData(
            var _id: String,
            var date: String,
            var image: String,
            var title: String,
            var url: String
        )
    }
}