package com.application.intercom.data.repository.UserPlanRespo

import com.application.intercom.data.api.ApiService
import com.application.intercom.data.model.local.BuySubscribe.BuySubscribePost
import com.application.intercom.data.model.local.UserBuySubscriprtionPostModel
import com.application.intercom.data.model.local.UserSendPhoneOtpLocalModel
import com.application.intercom.data.model.remote.UserPlanList.UserPlanListRes
import com.application.intercom.data.repository.EmpBaseRepository

class UserPlanRepo(private val apiService: ApiService) : EmpBaseRepository() {
    suspend fun userPlan(token: String) = safeApiCall {
        apiService.userPlanList(token)
    }

    suspend fun buySubscription(token: String, model: BuySubscribePost) = safeApiCall {
        apiService.buySubscription(token, model)
    }
    suspend fun userBuySubscription(token: String, model: UserBuySubscriprtionPostModel) =
        safeApiCall {
            apiService.userBuySubscrition(token, model)
        }
}