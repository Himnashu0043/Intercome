package com.application.intercom.data.model.local.owner.createPost

data class OwnerCreatePostModel(
    var building: String,
    var buildingId: String,
    var description: String,
    var `file`: List<String>,
    var flat: String,
    var flatId: String,
    var projectId: String
)