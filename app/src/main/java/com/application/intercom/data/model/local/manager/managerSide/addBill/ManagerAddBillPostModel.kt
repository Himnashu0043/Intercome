package com.application.intercom.data.model.local.manager.managerSide.addBill

data class ManagerAddBillPostModel(
    var flats: ArrayList<Flat>
   /* var amount: Int,
    var categoryId: String,
    var date: String,
    var flatId: String,
    var description: String*/
){
    data class Flat(
        var amount: Int?,
        var billMonth: String?,
        var billYear: String?,
        var categoryId: String?,
        var date: String?,
        var flatId: String?,
        var dueDate: String?
    )
}