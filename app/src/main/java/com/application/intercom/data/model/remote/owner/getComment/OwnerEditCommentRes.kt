package com.application.intercom.data.model.remote.owner.getComment

data class OwnerEditCommentRes(
    var `data`: Data,
    var message: String,
    var status: Int
) {
    data class Data(
        var __v: Int,
        var _id: String,
        var comment: String,
        var comment_by: String,
        var createdAt: String,
        var likesCount: Int,
        var parent_comment: Any,
        var post: String,
        var updatedAt: String
    )
}