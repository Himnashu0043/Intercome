package com.application.intercom.data.model.remote

data class NotificationList(
    var data: ArrayList<Data>,
    var message: String?,
    var status: Int?
) {
    data class Data(
        var __v: Int?,
        var _id: String?,
        var amount: Int?,
        var billMonth: String?,
        var billYear: String?,
        var categoryData: List<CategoryData?>?,
        var categoryId: String?,
        var createdAt: String?,
        var date: String?,
        var deleteStatus: Boolean?,
        var dueDate: String?,
        var isSeen: Boolean?,
        var visitorName:String?,
        var visitorId:String?,
        var visitCategoryName:String?,
        var flatName:String?,
        var flatNumber:String?,
        var complainName:String?,
        var photo:String?,
        var note:String?,
        var address:String?,
        var mobileNumber:String?,
        var approvedDate: String?,
        var notiMessage: String?,
        var notiTitle: String?,
        var notiTo: String?,
        var status: String?,
        var type: String?,
        var billType: String?,
        var updatedAt: String?,
        var userName: String?,
        var payType: String?

    ) {
        data class CategoryData(
            var __v: Int?,
            var _id: String?,
            var adminId: String?,
            var createdAt: String?,
            var image: String?,
            var is_delete: Boolean?,
            var name: String?,
            var status: String?,
            var updatedAt: String?
        )
    }
}