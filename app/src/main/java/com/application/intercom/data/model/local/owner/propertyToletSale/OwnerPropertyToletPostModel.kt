package com.application.intercom.data.model.local.owner.propertyToletSale


data class OwnerPropertyToletPostModel(
    var amentities: ArrayList<Amentity>,
    var buildingId: String,
    var description: String,
    var flatId: String,
    var flatStatus: String,
    var includeParking: Boolean,
    var photo: ArrayList<String>,
    var price: Int
) {
    data class Amentity(
        var image: String,
        var name: String
    )
}