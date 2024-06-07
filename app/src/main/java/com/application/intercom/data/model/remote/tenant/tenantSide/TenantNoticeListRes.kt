package com.application.intercom.data.model.remote.tenant.tenantSide

data class TenantNoticeListRes(
    var data: ArrayList<Data>,
    var message: String,
    var status: Int
) {
    data class Data(
        var __v: Int,
        var _id: String,
        var buildingId: String,
        var content: String,
        var createdAt: String,
        var date: Any,
        var images: ArrayList<String>,
        var isScheduled: Boolean,
        var is_active: Boolean,
        var is_view: Boolean,
        var notice_number: String,
        var projectId: String,
        var title: String,
        var updatedAt: String,
        var user: String,
        var views: Int
    )
}