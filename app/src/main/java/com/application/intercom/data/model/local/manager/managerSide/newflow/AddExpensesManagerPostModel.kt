package com.application.intercom.data.model.local.manager.managerSide.newflow

data class AddExpensesManagerPostModel(
    var billStatus: String,
    var buildingId: String,
    var categoryId: String?=null,
    var date: String,
    var expenseAmount: String,
    var expenseDetail: String,
    var expenseName: String,
    var uploadBill:String,
    var projectId:String
)