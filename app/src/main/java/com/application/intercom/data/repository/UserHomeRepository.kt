package com.application.intercom.data.repository

import com.application.intercom.data.api.ApiService
import com.application.intercom.data.model.local.PropertyListPostModel.PropertyListUserPostModel
import com.application.intercom.data.model.local.UserBuySubscriprtionPostModel
import com.application.intercom.data.model.local.UserViewContact.UserViewContactPostModel
import com.application.intercom.data.model.local.newFlow.AddUserNewPropertyPostModel
import com.application.intercom.data.model.local.newFlow.StatusUpdatePostModel
import com.application.intercom.user.newflow.modal.EditUserTestPostModel
import com.application.intercom.user.newflow.modal.UserTestPostModel

class UserHomeRepository(private val apiService: ApiService) : EmpBaseRepository() {


    suspend fun normalUserAdvertisementList(token: String) = safeApiCall {
        apiService.normalUserAdvertisementList(token)
        /* if (type == "owner") {
             apiService.normalUserAdvertisementList("",token)
         } else if (type =="tenant") {
             apiService.normalUserAdvertisementList("http://3.130.15.227:5500/api/v1/tenant/advertisementList",token)
         } else {

         }*/

    }
    suspend fun normalUserNotification(token: String) = safeApiCall {
        apiService.normalUserNotification(token)

    }

    suspend fun normalPropertyList(token: String, model: PropertyListUserPostModel) = safeApiCall {
        apiService.normalUserPropertyList(token, model)
    }

    suspend fun normalParkingList(token: String, model: PropertyListUserPostModel) = safeApiCall {
        apiService.normalUserParkingList(token, model)
    }

    suspend fun userViewContact(token: String, model: UserViewContactPostModel) = safeApiCall {
        apiService.userViewContact(token, model)
    }

    suspend fun userCreateRoom(token: String, userId: String, chatType: String) = safeApiCall {
        apiService.userCreateRoom(token, userId, chatType)
    }

    suspend fun userMessageHistory(token: String, roomId: String, chatType: String) = safeApiCall {
        apiService.userMessageHistory(token, roomId, chatType)
    }

    suspend fun userRoomList(token: String, chatType: String) = safeApiCall {
        apiService.userRoomList(token, chatType)
    }
    suspend fun ownerCommunityRoomList(token: String, chatType: String) = safeApiCall {
        apiService.ownerCommunityRoomList(token, chatType)
    }

    suspend fun userCode(token: String, title: String) = safeApiCall {
        apiService.userCode(token, title)
    }

    suspend fun userFaq(token: String) = safeApiCall {
        apiService.userFAQ(token)
    }

    suspend fun userNewAmenities(token: String) = safeApiCall {
        apiService.userNewAmenities(token)
    }

    suspend fun userNewProperty(token: String, model: UserTestPostModel) = safeApiCall {
        apiService.addUserNewProperty(token, model)
    }

    suspend fun edituserNewProperty(token: String, model: UserTestPostModel) = safeApiCall {
        apiService.editUserNewProperty(token, model)
    }

    suspend fun pendingList(token: String, status: String) = safeApiCall {
        apiService.pendingList(token, status)
    }

    suspend fun rejectList(token: String, status: String) = safeApiCall {
        apiService.rejectList(token, status)
    }

    suspend fun activeList(token: String, status: String) = safeApiCall {
        apiService.activeList(token, status)
    }

    suspend fun statusUpdate(token: String, model: StatusUpdatePostModel) = safeApiCall {
        apiService.statusUpdateNewPhase(token, model)
    }

    suspend fun userPlanDetailsList(token: String) = safeApiCall {
        apiService.userPlanDetailsList(token)
    }

    suspend fun userFavList(token: String, type: String) = safeApiCall {
        apiService.userFavList(token, type)
    }
    suspend fun userFavParkingList(token: String, type: String) = safeApiCall {
        apiService.userFavParkingList(token, type)
    }

    suspend fun userAddFavList(token: String, propertyId: String?,parkingId:String?) = safeApiCall {
        apiService.addFavProperty(token, propertyId,parkingId)
    }


}