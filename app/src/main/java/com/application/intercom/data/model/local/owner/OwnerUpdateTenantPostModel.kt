package com.application.intercom.data.model.local.owner

data class OwnerUpdateTenantPostModel(
    var _id: String,
    var agreement: String,
    /*var billingDate: String,*/
    var buildingId: String,
    var dateOfOccupancy: String,
    var email: String,
    var flatId: String,
    var fullName: String,
    var includeParking: Boolean,
    var mobileNumber: String,
    var parkingPrice: Int,
    var photo: String,
    var referenceNidBack: String,
    var referenceNidFront: String,
    var referenceNidNumber: String,
    /*var roomRent: Int,*/
    var tenantNidBack: String,
    var tenantNidFront: String,
    var tenantNidNumber: String,
    var password:String
)