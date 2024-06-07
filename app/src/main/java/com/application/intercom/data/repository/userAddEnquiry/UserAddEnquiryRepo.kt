package com.application.intercom.data.repository.userAddEnquiry

import com.application.intercom.data.api.ApiService
import com.application.intercom.data.model.local.BuySubscribe.BuySubscribePost
import com.application.intercom.data.model.local.addUserEnquiryPost.AddUserEnquiryPostModel
import com.application.intercom.data.repository.EmpBaseRepository

class UserAddEnquiryRepo(private val apiService: ApiService) : EmpBaseRepository() {
    suspend fun addUserEnquiry(token: String, model: AddUserEnquiryPostModel) = safeApiCall {
        apiService.addUserEnquiry(token, model)
    }
}