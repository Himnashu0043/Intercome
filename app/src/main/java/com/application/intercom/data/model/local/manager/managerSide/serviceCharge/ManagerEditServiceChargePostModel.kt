package com.application.intercom.data.model.local.manager.managerSide.serviceCharge

data class ManagerEditServiceChargePostModel(
    var amount: Int,
    var chargeId: String,
    var date: String,
    var serviceChargeType: String
)