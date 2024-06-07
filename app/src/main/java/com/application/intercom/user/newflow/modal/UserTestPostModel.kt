package com.application.intercom.user.newflow.modal

data class UserTestPostModel(
    var _id:String?=null,
    var address: String,
    var amentities: ArrayList<Amentity>? = null,
    var bathroom: Int? = null,
    var bedroom: Int? = null,
    var description: String? = null,
    var district: String? = null,
    var division: String? = null,
    var flatStatus: String? = null,
    var floorLevel: Int? = null,
    var lat: Double? = null,
    var long: Double? = null,
    var photos: ArrayList<String>? = null,
    var policeStation: String? = null,
    var postOffice: String? = null,
    var price: Int? = null,
    var propertyType: String? = null,
    var sqft: Int? = null,
    var subPropertyType: String? = null,
    var title: String? = null,
    var totalFloor: Int? = null
):java.io.Serializable {
    data class Amentity(
        var image: String,
        var name: String
    ):java.io.Serializable
}