package com.application.intercom.data.model.remote.manager.managerSide.noticeBoard

data class ManagerNoticeBoardListRes(
    var data: ArrayList<Data>,
    var message: String,
    var status: Int
):java.io.Serializable {
    data class Data(
        var __v: Int,
        var _id: String,
        var buildingId: String,
        var content: String,
        var createdAt: String,
        var date: String,
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
    ):java.io.Serializable
}