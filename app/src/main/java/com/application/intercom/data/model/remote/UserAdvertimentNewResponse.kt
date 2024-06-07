package com.application.intercom.data.model.remote

data class UserAdvertimentNewResponse(
    var data: Data,
    var message: String,
    var status: Int
) {
    data class Data(
        var bottomUser: ArrayList<BottomUser>,
        var middleUser: ArrayList<MiddleUser>,
        var topUser: ArrayList<TopUser>
    ) {
        data class BottomUser(
            var __v: Int,
            var _id: String,
            var adminId: String,
            var advertisementData: ArrayList<AdvertisementData>,
            var buildingId: List<Any>,
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

        data class MiddleUser(
            var __v: Int,
            var _id: String,
            var adminId: String,
            var advertisementData: ArrayList<AdvertisementData>,
            var buildingId: List<Any>,
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

        data class TopUser(
            var __v: Int,
            var _id: String,
            var adminId: String,
            var advertisementData: ArrayList<AdvertisementData>,
            var buildingId: List<Any>,
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
}