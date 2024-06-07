package com.application.intercom.data.model.local.owner.propertyToletSale

data class OwnerPropertySalePostModel(
    var buildingId: String,
    var description: String,
    var flatId: String,
    var parkingId: String,
    var parkingPrice: Int,
    var parkingStatus: String,
    var photo: ArrayList<String>
)