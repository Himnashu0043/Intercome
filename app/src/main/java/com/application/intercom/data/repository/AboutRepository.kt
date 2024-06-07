package com.application.intercom.data.repository

import com.application.intercom.data.api.ApiService

class AboutRepository(private val apiService: ApiService):EmpBaseRepository() {


    suspend fun userGetPrivacyPolicy(token: String)=safeApiCall {
        apiService.userGetPrivacyAndPolicy(token)
    }

    suspend fun userGetTermsOfService(token: String)=safeApiCall {
        apiService.userGetTermsOfService(token)
    }

    suspend fun userGetAboutUs(token: String)=safeApiCall {
        apiService.userGetAboutUs(token)
    }
}