package com.application.intercom.data.model.local.owner.registerComplain

data class OwnerRegisterComplainPostModel(
    var complainName: String,
    var description: String,
    var photo: ArrayList<String>,
    var buildingId:String
    /*var serviceCategory: String*/
)