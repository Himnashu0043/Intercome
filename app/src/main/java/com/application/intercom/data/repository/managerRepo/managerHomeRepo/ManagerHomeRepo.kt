package com.application.intercom.data.repository.managerRepo.managerHomeRepo

import com.application.intercom.data.api.ApiService
import com.application.intercom.data.model.remote.owner.community.OwnerMyCommunityListRes
import com.application.intercom.data.repository.EmpBaseRepository

class ManagerHomeRepo(private val apiService: ApiService) : EmpBaseRepository() {
    suspend fun managerDetails(token: String) = safeApiCall {
        apiService.getManagerDetails(token)
    }

    suspend fun managerFlatList(token: String) = safeApiCall {
        apiService.managerFlatList(token)
    }

    suspend fun managerToLetFlatList(token: String) = safeApiCall {
        apiService.managerToLetFlatList(token)
    }

    suspend fun managerParkingList(token: String) = safeApiCall {
        apiService.managerParkingList(token)
    }

    suspend fun managerParkingToletList(token: String) = safeApiCall {
        apiService.managerParkingToLetList(token)
    }

    suspend fun managerTenantHistoryList(token: String, flatId: String) = safeApiCall {
        apiService.managerTenantHostoryList(token, flatId)
    }


}