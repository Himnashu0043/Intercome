package com.application.intercom.data.model.local.PropertyListPostModel

data class PropertyListUserPostModel(
    var latitude: Double,
    var longitude: Double,
    var minValue: Int? = null,
    var maxValue: Int? = null,
    var sort: String? = null,
    var parkingStatus: String? = null,
    var flatType: Int? = null,
    var sqftMinValue: Int? = null,
    var sqftMaxValue: Int? = null,
    var bedroom: Int? = null,
    var bathroom: Int? = null,
    var propertyType: String? = null,
    var flatStatus: String? = null,

)