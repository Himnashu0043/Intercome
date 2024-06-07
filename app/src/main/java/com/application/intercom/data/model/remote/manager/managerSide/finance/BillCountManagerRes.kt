package com.application.intercom.data.model.remote.manager.managerSide.finance

data class BillCountManagerRes(
    var data: Data?,
    var message: String?,
    var status: Int?
) {
    data class Data(
        var resultExpenseCount: Int?,
        var resultExpensePaidCount: Int?,
        var resultExpensePaidSum: Int?,
        var resultExpenseSum: Int?,
        var resultPaidCount: Int?,
        var resultPaidSum: Int?,
        var resultUnApprovedCount: Int?,
        var resultUnApprovedSum: Int?,
        var resultUnPaidCount: Int?,
        var resultUnPaidSum: Int?,
        var resultServiceCount:Int?,
        var resultServiceSum:Int?,
        var resultRentCount:Int?,
        var resultRentSum:Int
    )
}