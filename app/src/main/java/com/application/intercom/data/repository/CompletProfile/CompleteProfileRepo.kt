package com.application.intercom.data.repository.CompletProfile

import com.application.intercom.data.api.ApiService
import com.application.intercom.data.model.local.UserUpdateProfileLocalModel
import com.application.intercom.data.repository.EmpBaseRepository

class CompleteProfileRepo(private val apiService: ApiService) : EmpBaseRepository() {
    suspend fun completeProfile(token: String, model: UserUpdateProfileLocalModel) = safeApiCall {
        apiService.userUserUpdateProfile(token, model)
    }
}