package com.application.intercom.data.model.remote.UserPlanList

data class UserPlanListRes(
    var `data`: List<Data>,
    var message: String,
    var status: Int
) {
    data class Data(
        var __v: Int,
        var _id: String,
        var contacts: String,
        var createdAt: String,
        var description: String,
        var `for`: String,
        var image: String,
        var is_delete: Boolean,
        var is_trial: Boolean,
        var modules: List<Any>,
        var price: Int,
        var status: String,
        var title: String? = null,
        var type: String,
        var updatedAt: String,
        var user_plan_number: String,
        var validFor: String,
        var isSelected: Boolean
    )
}