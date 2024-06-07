package com.application.intercom.data.model.local.newFlow

data class AddUserNewPropertyPostModel(
    var address: String,
    var amentities: List<Amentity>,
    var bathroom: Int,
    var bedroom: Int,
    var description: String,
    var district: String,
    var division: String,
    var flatStatus: String,
    var floorLevel: Int,
    var lat: Double,
    var long: Double,
    var photos: ArrayList<String>,
    var policeStation: String,
    var postOffice: String,
    var price: Int,
    var propertyType: String,
    var sqft: Int,
    var subPropertyType: String,
    var title: String,
    var totalFloor: Int
) {
    data class Amentity(
        var image: String,
        var name: String
    )
}