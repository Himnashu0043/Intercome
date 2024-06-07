package com.application.intercom.data.repository.gateKeeperRepo

import com.application.intercom.data.api.ApiService
import com.application.intercom.data.model.local.gateKeeper.AddGatePassPostModel
import com.application.intercom.data.model.local.gateKeeper.AddRegularVisitorEntryPostModel
import com.application.intercom.data.model.local.gateKeeper.AddSingleEntryGateKeeperPostModel
import com.application.intercom.data.repository.EmpBaseRepository

class GateKeeperHomeRepo(private val apiService: ApiService) : EmpBaseRepository() {
    suspend fun gateKeeperDetails(token: String) = safeApiCall {
        apiService.getGateKeeperDetails(token)
    }

    suspend fun flatOfBuildingList(token: String) = safeApiCall {
        apiService.flatListOfBuildinggatekeeper(token)
    }

    suspend fun getVisitorCategoryList(token: String) = safeApiCall {
        apiService.getVisitorCategoryListGatekeeper(token)
    }

    suspend fun addSingleEntry(token: String, model: AddSingleEntryGateKeeperPostModel) =
        safeApiCall {
            apiService.addSingleEntry(token, model)
        }

    suspend fun gateKeeperList(
        token: String,
        buildingId: String?,
        flatId: String?,
        status: String
    ) =
        safeApiCall {
            apiService.gateKeeperList(token, buildingId, flatId, status)
        }

    suspend fun exitGatePass(token: String, gatePassId: String) =
        safeApiCall {
            apiService.exitGatePass(token, gatePassId)
        }

    suspend fun addGatePass(token: String, model: AddGatePassPostModel) =
        safeApiCall {
            apiService.addGatePass(token, model)
        }

    suspend fun singleEntryHistorylist(token: String, visitorStatus: String, flatId: String) =
        safeApiCall {
            apiService.singleEntryHistroyList(token, visitorStatus, flatId)
        }

    suspend fun visitorNotifyToUser(token: String, visitorId: String) =
        safeApiCall {
            apiService.visitorNotifyToUserRes(token, visitorId)
        }

    suspend fun flatListOfVisitor(token: String, flatId: String) =
        safeApiCall {
            apiService.flatListOfVisitorGateKeeper(token, flatId)
        }

    suspend fun addRegularVisitorEntry(token: String, model: AddRegularVisitorEntryPostModel) =
        safeApiCall {
            apiService.addRegularVisitorEntry(token, model)
        }

    suspend fun outRegularVisitorEntry(token: String, visitorId: String) =
        safeApiCall {
            apiService.outEntryGateKeeper(token, visitorId)
        }

    suspend fun regularVisitorHistoryGateKeeper(
        token: String,
        visitorStatus: String,
        flatId: String,
        buildingId:String
    ) =
        safeApiCall {
            apiService.regularVisitorHistoryList(token, visitorStatus, flatId,buildingId)
        }

    suspend fun regularEntryHistoryDetailsGateKeeper(token: String, visitorId: String) =
        safeApiCall {
            apiService.regularEntryHistoryDetailsGateKeeper(token, visitorId)
        }
    suspend fun outSingleVisitorEntryGateKeeper(token: String, visitorId: String) =
        safeApiCall {
            apiService.outSingleVisitorEntryGateKeeper(token, visitorId)
        }
    suspend fun gateKeeperProfile(token: String) =
        safeApiCall {
            apiService.gateKeeperProfile(token)
        }
}