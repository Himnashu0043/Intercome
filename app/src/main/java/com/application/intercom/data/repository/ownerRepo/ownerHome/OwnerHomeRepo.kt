package com.application.intercom.data.repository.ownerRepo.ownerHome

import com.application.intercom.data.api.ApiService
import com.application.intercom.data.model.local.manager.managerSide.rentEdit.RentEditManagerPostModel
import com.application.intercom.data.model.local.manager.managerSide.serviceCharge.ManagerAddServiceChargePostModel
import com.application.intercom.data.model.local.owner.OwnerAddTenantPostModel
import com.application.intercom.data.model.local.owner.OwnerCommunityPostModel
import com.application.intercom.data.model.local.owner.OwnerUpdateTenantPostModel
import com.application.intercom.data.model.local.owner.likeCommunty.OwnerLikeCommunityPostModel
import com.application.intercom.data.model.remote.manager.managerSide.rent.AddRentManagerPostModel
import com.application.intercom.data.repository.EmpBaseRepository

class OwnerHomeRepo(private val apiService: ApiService) : EmpBaseRepository() {
    suspend fun ownerDetails(token: String) = safeApiCall {
        apiService.getOwnerDetails(token)
    }

    suspend fun ownerAdvertisementList(token: String, _id: String) = safeApiCall {
        apiService.getAdvertisementListOwner(token, _id)
    }

    suspend fun ownerCommunityList(token: String, model: HashMap<String, Any>) = safeApiCall {
        apiService.getOwnerCommunityList(token, model)
    }

    suspend fun ownerMyCommunityList(token: String, model: OwnerCommunityPostModel) = safeApiCall {
        apiService.ownerMyCommunityList(token, model)
    }

    suspend fun likeownerCommunity(token: String, model: OwnerLikeCommunityPostModel) =
        safeApiCall {
            apiService.likeOwnerCommunity(token, model)
        }

    suspend fun ownerFlatList(token: String) = safeApiCall {
        apiService.getOwnerFlatList(token)
    }
    suspend fun tenantFlatList(token: String) = safeApiCall {
        apiService.getTenantFlatList(token)
    }

    suspend fun addOwnerTenant(token: String, model: OwnerAddTenantPostModel) = safeApiCall {
        apiService.addOwnerTenant(token, model)
    }

    suspend fun updateOwnerTenant(token: String, model: OwnerUpdateTenantPostModel) = safeApiCall {
        apiService.updateOwnerTenant(token, model)
    }


    suspend fun getOwnerCurrentHistory(token: String, type: String, flatId: String) = safeApiCall {
        apiService.getOwnerCurrentHistoryList(token, type, flatId)
    }
    suspend fun getAllMember(token: String,projectId:String) = safeApiCall {
        apiService.getAllMemeberList(token, projectId)
    }

    suspend fun ownerParkingtList(token: String) = safeApiCall {
        apiService.getOwnerParkingList(token)
    }

    suspend fun deleteOwnerTenant(token: String, tenantId: String) = safeApiCall {
        apiService.deleteOwnerTenant(token, tenantId)
    }

    suspend fun gateKeeperOwnerTenant(token: String, buildingId: String) = safeApiCall {
        apiService.ownergatekeeperList(token, buildingId)
    }

    suspend fun viewPostCountOwner(token: String, _id: String) = safeApiCall {
        apiService.viewPostCountOwner(token, _id)
    }

    suspend fun setAsHomeOwner(token: String, flatId: String) = safeApiCall {
        apiService.setAsHomeOwnerList(token, flatId)
    }

    suspend fun addOwnerRent(token: String, model: /*ManagerAddServiceChargePostModel*/AddRentManagerPostModel) =
        safeApiCall {
            apiService.addOwnerRent(token, model)
        }

    suspend fun editOwnerRent(token: String, model: RentEditManagerPostModel) =
        safeApiCall {
            apiService.editOwnerRent(token, model)
        }

    suspend fun getOwnerRentApi(
        token: String,
    ) =
        safeApiCall {
            apiService.getOwnerRent(token)
        }
    suspend fun deleteOwnerRentRes(token: String, billId: String) = safeApiCall {
        apiService.deleteOwnerRent(token, billId)
    }

}