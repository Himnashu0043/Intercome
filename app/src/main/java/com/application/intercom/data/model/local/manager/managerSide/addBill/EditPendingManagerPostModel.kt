package com.application.intercom.data.model.local.manager.managerSide.addBill

data class EditPendingManagerPostModel(
    /* var amount: Int,
     var billId: String,
     var categoryId: String,
     var date: String,
     var description:String,
     var flatId: String*/
    var billId: String?,
    var amount: Int?,
    var billMonth: String?,
    var billYear: String?,
    var categoryId: String?,
    var date: String?,
    var flatId: String?,
    var dueDate: String?
)