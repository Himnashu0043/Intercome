package com.application.intercom.data.model.remote.manager.managerSide.finance.balanceSheet

data class AddNoteBalanceSheetRes(
    var `data`: Data?,
    var message: String?,
    var status: Int?
) {
    data class Data(
        var __v: Int?,
        var _id: String?,
        var addedBy: String?,
        var buildingId: String?,
        var createdAt: String?,
        var endDate: String?,
        var is_active: Boolean?,
        var is_delete: Boolean?,
        var month: String?,
        var note: String?,
        var startDate: String?,
        var updatedAt: String?
    )
}