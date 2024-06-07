package com.application.intercom.data.repository

import com.application.intercom.data.api.ApiService

class ServiceRepository(private val apiService: ApiService):EmpBaseRepository() {


    suspend fun normalUserServicesList(token: String,search:String)=safeApiCall {
        apiService.noramlUserServicesListAndSearch(token,search)
    }

    suspend fun normalUserServiceProviderList(token: String,category_Id:String)=safeApiCall {
        apiService.noramlUserServiceProviderList(token,category_Id)
    }


}