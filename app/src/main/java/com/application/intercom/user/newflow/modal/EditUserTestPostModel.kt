package com.application.intercom.user.newflow.modal

data class EditUserTestPostModel(
    var address: String,
    var amentities: ArrayList<Amentity>,
    var bathroom: Int,
    var bedroom: Int,
    var description: String,
    var district: String,
    var division: String,
    var flatStatus: String,
    var floorLevel: Int,
    var id: String,
    var lat: Double,
    var long: Double,
    var photos: List<String>,
    var policeStation: String,
    var postOffice: String,
    var price: Int,
    var propertyType: String,
    var sqft: Int,
    var subPropertyType: String,
    var title: String,
    var totalFloor: Int
):java.io.Serializable {
    data class Amentity(
        var image: String,
        var name: String
    ):java.io.Serializable
}