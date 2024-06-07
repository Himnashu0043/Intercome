package com.application.intercom.data.model.remote.manager.managerSide.rent

data class AddRentManagerPostModel(
    var flats: ArrayList<Flat>?
) {
    data class Flat(
        var amount: Int?,
        var date: String?,
        var dueDate: String?,
        var flatId: String?,
        var billMonth: String?,
        var billYear: String?
    )
}
