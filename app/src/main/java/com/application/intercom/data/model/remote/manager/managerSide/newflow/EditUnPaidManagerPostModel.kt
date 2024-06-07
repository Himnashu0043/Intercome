package com.application.intercom.data.model.remote.manager.managerSide.newflow

data class EditUnPaidManagerPostModel(
    var billId: String,
    var buildingId: String,
    var categoryId: String?=null,
    var date: String,
    var expenseAmount: String,
    var expenseDetail: String,
    var expenseName: String,
    var uploadBill: String
)