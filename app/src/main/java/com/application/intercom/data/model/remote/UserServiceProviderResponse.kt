package com.application.intercom.data.model.remote

data class UserServiceProviderResponse(
    val status: Int,
    val message: String,
    val data: Data
) {
    data class Data(
        val total: Int,
        val result: ArrayList<Result>
    ) {
        data class Result(
            val _id: String,
            val provider_number: String,
            val category_Id: CategoryId,
            val division: String,
            val district: String,
            val policeStation: String,
            val postOffice: String,
            val address: String,
            val serviceProviderName: String,
            val contactNumber: String,
            val photo: String,
            val nid: String,
            val charges: Int,
            val status: String,
            val is_delete: Boolean,
            val createdAt: String,
            val updatedAt: String,
            val __v: Int
        ) {
            data class CategoryId(
                val _id: String,
                val category_number: String,
                val category_name: String,
                val type: String,
                val image: String,
                val status: String,
                val is_delete: Boolean,
                val createdAt: String,
                val updatedAt: String,
                val __v: Int
            )
        }
     }
}
