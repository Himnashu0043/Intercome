package com.application.intercom.data.model.local.BuySubscribe

data class BuySubscribePost(
    var contacts: Int,
    var cost: String,
    var duration: Int,
    var planId: String
)