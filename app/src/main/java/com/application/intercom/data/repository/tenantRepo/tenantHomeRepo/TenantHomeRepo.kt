package com.application.intercom.data.repository.tenantRepo.tenantHomeRepo

import com.application.intercom.data.api.ApiService
import com.application.intercom.data.repository.EmpBaseRepository

class TenantHomeRepo(private val apiService: ApiService) : EmpBaseRepository() {
    suspend fun tenantDetails(token: String) = safeApiCall {
        apiService.getTenantDetails(token)
    }

    suspend fun advertisementTenantRes(token: String, _id: String) = safeApiCall {
        apiService.advertisementTenantRes(token, _id)
    }
}