package com.application.intercom.data.model.remote

import com.application.intercom.db.entity.ServicesCategoryTable

class ServicesListResponse(
    val status: Int,
    val message: String,
    val data: Data
) {
    data class Data(
        val docs: ArrayList<ServicesCategoryTable>,
        val total: Int,
        val limit: Int,
        val page: Int,
        val pages: Int
        )
//    {
//        data class ServicesCategoryTable(
//            val _id: String,
//            val category_number: String,
//            val category_name: String,
//            val type: String,
//            val image: String,
//            val status: String,
//            var is_delete: Boolean,
//            val createdAt: String,
//            val updatedAt: String,
//            val __v: Int,
//            var is_recent: Boolean
//            )
//    }
}