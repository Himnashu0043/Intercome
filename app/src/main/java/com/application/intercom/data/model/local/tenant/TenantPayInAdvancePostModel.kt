package com.application.intercom.data.model.local.tenant

data class TenantPayInAdvancePostModel(
    var monthCount: Int,
    var referenceNo: String,
    var uploadDocument: String
)