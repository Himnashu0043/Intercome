package com.application.intercom.data.repository.tenantRepo

import com.application.intercom.data.api.ApiService
import com.application.intercom.data.model.local.owner.addRegularEntry.AddRegularEntryOwnerPostModel
import com.application.intercom.data.model.local.owner.likeCommunty.OwnerLikeCommunityPostModel
import com.application.intercom.data.model.local.owner.registerComplain.OwnerRegisterComplainPostModel
import com.application.intercom.data.model.local.tenant.TenantPayInAdvancePostModel
import com.application.intercom.data.model.local.tenant.TenantPayNowPostModel
import com.application.intercom.data.model.remote.tenant.tenantSide.TenantComplainListRes
import com.application.intercom.data.repository.EmpBaseRepository

class TenantSideRepo(private val apiService: ApiService) : EmpBaseRepository() {
    suspend fun tenantNoticeList(token: String) = safeApiCall {
        apiService.getTenantNoticBoardList(token)
    }

    suspend fun tenantregisterComplain(token: String, model: OwnerRegisterComplainPostModel) =
        safeApiCall {
            apiService.tenant_registerComplain(token, model)
        }

    suspend fun tenantComplainList(token: String) = safeApiCall {
        apiService.tenantComplainList(token)
    }

    suspend fun tenantUnPaidList(token: String, userBillStatus: String,flatId:String?) = safeApiCall {
        apiService.tenantUnPaidList(token, userBillStatus,flatId)
    }
    suspend fun notifyUserTenantList(token: String, billId: String) = safeApiCall {
        apiService.notifyUserTenant(token, billId)
    }

    suspend fun payNowtenant(token: String, model: TenantPayNowPostModel) =
        safeApiCall {
            apiService.payNowTenant(token, model)
        }

    suspend fun payNowOwner(token: String, model: TenantPayNowPostModel) =
        safeApiCall {
            apiService.payNowOwner(token, model)
        }

    suspend fun payNowManager(token: String, model: TenantPayNowPostModel) =
        safeApiCall {
            apiService.payNowManager(token, model)
        }

    suspend fun payInAdvancetenant(token: String, model: TenantPayInAdvancePostModel) =
        safeApiCall {
            apiService.payInAdvanceTenant(token, model)
        }

    suspend fun viewPostDetailsTenant(token: String, postId: String) =
        safeApiCall {
            apiService.viewPostDetailsTenant(token, postId)
        }
    suspend fun viewPostDetailsOwner(token: String, postId: String) =
        safeApiCall {
            apiService.viewPostDetailsOwner(token, postId)
        }
    suspend fun likeTenantCommunity(token: String, model: OwnerLikeCommunityPostModel) =
        safeApiCall {
            apiService.likeTenantCommunity(token, model)
        }

}