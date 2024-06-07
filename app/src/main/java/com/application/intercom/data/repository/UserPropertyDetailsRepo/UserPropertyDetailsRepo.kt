package com.application.intercom.data.repository.UserPropertyDetailsRepo

import com.application.intercom.data.api.ApiService
import com.application.intercom.data.model.local.UserPropertyDetailsPostModel.UserPropertyDetailsPostModel
import com.application.intercom.data.repository.EmpBaseRepository

class UserPropertyDetailsRepo(private val apiService: ApiService) : EmpBaseRepository() {
    suspend fun userPropertyDetails(token: String, model: UserPropertyDetailsPostModel) =
        safeApiCall {
            apiService.userPropertyDetails(token, model)
        }

    suspend fun userPostPropertyDetails(token: String, model: UserPropertyDetailsPostModel) =
        safeApiCall {
            apiService.userPostPropertyDetails(token, model)
        }


    suspend fun userParkingDetails(token: String, model: UserPropertyDetailsPostModel) =
        safeApiCall {
            apiService.userParkingDetails(token, model)
        }
}