package com.application.intercom.data.model.local.localModel

class MapDataSendModel(
    var buildingName: String,
    var distance: Double,
    var division: String,
    var district: String,
    var bedroom: Int?=null,
    var bathroom: Int,
    var sqft: Int,
    var price: Int,
    var photo: String,
    var _id:String,
    var buildingId:String,
    var description: String?=null
):java.io.Serializable