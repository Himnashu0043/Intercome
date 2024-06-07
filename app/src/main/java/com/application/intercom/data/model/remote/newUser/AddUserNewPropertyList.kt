package com.application.intercom.data.model.remote.newUser

data class AddUserNewPropertyList(
    var `data`: Data,
    var message: String,
    var status: Int
) {
    data class Data(
        var __v: Int,
        var _id: String,
        var address: String,
        var amentities: List<Amentity>,
        var approvedStatus: String,
        var bathroom: Int,
        var bedroom: Int,
        var createdAt: String,
        var description: String,
        var district: String,
        var flatStatus: String,
        var floorLevel: Int,
        var is_assign: Boolean,
        var is_delete: Boolean,
        var latitude: Double,
        var location: Location,
        var longitude: Double,
        var owner: String,
        var photos: List<String>,
        var policeStation: String,
        var postOffice: String,
        var price: Int,
        var propertyType: String,
        var sqft: Int,
        var status: String,
        var subPropertyType: String,
        var title: String,
        var totalFloor: Int,
        var updatedAt: String
    ) {
        data class Amentity(
            var _id: String,
            var image: String,
            var name: String
        )

        data class Location(
            var coordinates: List<Double>,
            var type: String
        )
    }
}