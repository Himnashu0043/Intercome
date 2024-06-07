package com.application.intercom.data.model.local.manager.managerSide.serviceCharge

data class ManagerAddServiceChargePostModel(
    /*var amount: Int,
    var date: String,
    var flats: List<String>,
    var serviceChargeType: String*/
    var flats: ArrayList<Flat>?
) {
    data class Flat(
        var amount: Int?,
        var date: String?,
        var dueDate: String?,
        var flatId: String?,
        var month: String?,
        var year: String?
    )
}