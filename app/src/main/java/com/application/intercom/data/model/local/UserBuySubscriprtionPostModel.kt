package com.application.intercom.data.model.local

data class UserBuySubscriprtionPostModel(
    var contacts: Int,
    var cost: String,
    var duration: Int,
    var planId: String,
    var transactionId: String,
    var title:String
)