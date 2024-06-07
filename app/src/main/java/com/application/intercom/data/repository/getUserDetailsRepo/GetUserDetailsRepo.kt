package com.application.intercom.data.repository.getUserDetailsRepo

import com.application.intercom.data.api.ApiService
import com.application.intercom.data.repository.EmpBaseRepository

class GetUserDetailsRepo(private val apiService: ApiService) : EmpBaseRepository() {
    suspend fun userDetails(token: String) = safeApiCall {
        apiService.getUserDetails(token)
    }
}