package com.application.intercom.data.repository.ownerRepo.ownerTolet

import com.application.intercom.data.api.ApiService
import com.application.intercom.data.model.local.owner.propertyToletSale.OwnerPropertySalePostModel
import com.application.intercom.data.model.local.owner.propertyToletSale.OwnerPropertyToletPostModel
import com.application.intercom.data.repository.EmpBaseRepository

class OwnerToletSale(private val apiService: ApiService) : EmpBaseRepository() {
    suspend fun owneramenities(token: String) = safeApiCall {
        apiService.getAmemities(token)
    }

    suspend fun addFlatOwner(token: String, model: OwnerPropertyToletPostModel) = safeApiCall {
        apiService.addFlatOwner(token, model)
    }

    suspend fun addParkingOwner(token: String, model: OwnerPropertySalePostModel) = safeApiCall {
            apiService.addParkingOwner(token, model)

    }
}