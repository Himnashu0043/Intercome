package com.application.intercom.data.model.remote.owner.ownerHome

data class BillCountOwnerRes(
    var data: Data?,
    var message: String?,
    var status: Int?
) {
    data class Data(
        var resultPaidCount: Int?,
        var resultPaidSum: Int?,
        var resultRentCount: Int?,
        var resultRentSum: Int?,
        var resultUnApprovedCount: Int?,
        var resultUnApprovedSum: Int?,
        var resultUnPaidCount: Int?,
        var resultUnPaidSum: Int?,
        var resultPendingCount: Int?,
        var resultPendingSum: Int?

    )
}