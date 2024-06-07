package com.application.intercom.data.model.remote.manager.managerSide.newflow

data class DeleteUnPaidManagerRes(
    var `data`: Data,
    var message: String,
    var status: Int
) {
    data class Data(
        var deletedCount: Int
    )
}