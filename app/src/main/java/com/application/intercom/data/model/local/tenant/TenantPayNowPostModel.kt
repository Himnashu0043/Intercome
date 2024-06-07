package com.application.intercom.data.model.local.tenant

data class TenantPayNowPostModel(
    var billId: String,
    var referenceNo: String,
    var uploadDocument: String,
    var payType: String

)