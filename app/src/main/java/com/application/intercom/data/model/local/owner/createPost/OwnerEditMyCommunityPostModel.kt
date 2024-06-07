package com.application.intercom.data.model.local.owner.createPost

data class OwnerEditMyCommunityPostModel(
    var building: String,
    var buildingId: String,
    var description: String,
    var `file`: List<String>,
    var flat: String,
    var flatId: String,
    var postId: String,
    var projectId: String
)