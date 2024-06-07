package com.application.intercom.data.model.remote.owner.community

data class OwnerEditMyCommunityRes(
    var `data`: Data,
    var message: String,
    var status: Int
) {
    data class Data(
        var __v: Int,
        var _id: String,
        var building: String,
        var buildingId: String,
        var commentsCount: Int,
        var createdAt: String,
        var description: String,
        var `file`: List<String>,
        var flat: String,
        var flatId: String,
        var is_active: Boolean,
        var is_delete: Boolean,
        var likesCount: Int,
        var projectId: String,
        var totalViews: Int,
        var updatedAt: String,
        var userId: String
    )
}