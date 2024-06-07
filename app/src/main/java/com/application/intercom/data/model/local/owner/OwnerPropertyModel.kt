package com.application.intercom.data.model.local.owner

data class OwnerPropertyModel(
    var buildingName: String,
    var address: String,
    var bedroom: String,
    var bathroom: String,
    var sqft: String,
    var description: String,
    var photos: String
) : java.io.Serializable
