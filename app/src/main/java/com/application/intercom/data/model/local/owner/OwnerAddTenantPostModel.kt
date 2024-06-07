package com.application.intercom.data.model.local.owner

data class OwnerAddTenantPostModel(
    var buildingId: String,
    var flatId: String,
    var fullName: String,
    var email: String,
    var mobileNumber: String,
    var tenantNidNumber: String,
    var tenantNidFront: String,
    var tenantNidBack: String,
    var referenceNidNumber: String,
    var referenceNidFront: String,
    var referenceNidBack: String,
    var includeParking: Boolean,
   /* var billingDate: String,*/
    /*var roomRent: Int,*/
    var parkingPrice: Int,
    var photo: String,
    var dateOfOccupancy: String,
    var agreement: String,
    var password:String

)