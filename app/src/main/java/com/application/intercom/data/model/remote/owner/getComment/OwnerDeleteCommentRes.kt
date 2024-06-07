package com.application.intercom.data.model.remote.owner.getComment

data class OwnerDeleteCommentRes(
    var data: Data,
    var message: String,
    var status: Int
) {
    data class Data(
        var deletedCount: Int
    )
}