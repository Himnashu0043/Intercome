package com.application.intercom.data.model.remote.newUser.MyList

data class RejectNewPhaseList(
    var data: ArrayList<Data>,
    var message: String,
    var status: Int
):java.io.Serializable {
    data class Data(
        var __v: Int,
        var _id: String,
        var addedBy: String,
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
        var photo: List<String>,
        var policeStation: String,
        var postOffice: String,
        var price: Int,
        var propertyType: String,
        var rejectReason: String,
        var sqft: Int,
        var status: String,
        var subPropertyType: String,
        var title: String,
        var totalFloor: Int,
        var updatedAt: String
    ):java.io.Serializable {
        data class Amentity(
            var _id: String,
            var image: String,
            var name: String
        ):java.io.Serializable

        data class Location(
            var coordinates: List<Double>,
            var type: String
        ):java.io.Serializable
    }
}