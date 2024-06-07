package com.application.intercom.data.model.remote.owner.community

data class OwnerLikeCommunityRes(
    var `data`: Data,
    var message: String,
    var status: Int
) {
    data class Data(
        var __v: Int,
        var _id: String,
        var createdAt: String,
        var liked_by: String,
        var post: String,
        var status: String,
        var updatedAt: String
    )
}