package com.application.intercom.data.repository.getUserFlat

import com.application.intercom.data.api.ApiService
import com.application.intercom.data.model.local.UserPropertyDetailsPostModel.UserPropertyDetailsPostModel
import com.application.intercom.data.repository.EmpBaseRepository

class UserFlatRepo(private val apiService: ApiService) : EmpBaseRepository() {
    suspend fun getUserFlat(token: String) =
        safeApiCall {
            apiService.getFlatData(token)
        }

    suspend fun getUserParkingActivity(token: String) =
        safeApiCall {
            apiService.getParkingActivity(token)
        }
}