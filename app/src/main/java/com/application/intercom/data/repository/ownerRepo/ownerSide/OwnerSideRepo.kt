package com.application.intercom.data.repository.ownerRepo.ownerSide

import com.application.intercom.data.api.ApiService
import com.application.intercom.data.model.local.owner.actiontoComplain.OwnerActionToComplainPostModel
import com.application.intercom.data.model.local.owner.addRegularEntry.AddRegularEntryOwnerPostModel
import com.application.intercom.data.model.local.owner.addRegularEntry.EditRegularEntryOwnerPostModel
import com.application.intercom.data.model.local.owner.commentPost.EditCommentPostModel
import com.application.intercom.data.model.local.owner.commentPost.OwnerCommentPostModel
import com.application.intercom.data.model.local.owner.createPost.OwnerCreatePostModel
import com.application.intercom.data.model.local.owner.createPost.OwnerEditMyCommunityPostModel
import com.application.intercom.data.model.local.owner.gatePass.OwnerAddGatePassPostModal
import com.application.intercom.data.model.local.owner.gatePass.OwnerEditGatePassPostModel
import com.application.intercom.data.model.local.owner.getCommentPostModel.OwnerGetCommentPostModel
import com.application.intercom.data.model.local.owner.registerComplain.OwnerRegisterComplainPostModel
import com.application.intercom.data.repository.EmpBaseRepository

class OwnerSideRepo(private val apiService: ApiService) : EmpBaseRepository() {
    suspend fun ownerNoticeList(token: String) = safeApiCall {
        apiService.getOwnerNoticBoardList(token)
    }

    suspend fun billCountOwner(token: String) = safeApiCall {
        apiService.billCountOwnerList(token)
    }

    suspend fun ownerViewNotice(token: String, _id: String) = safeApiCall {
        apiService.viewNoticeOwner(token, _id)
    }

    suspend fun ownerregisterComplain(token: String, model: OwnerRegisterComplainPostModel) =
        safeApiCall {
            apiService.registerComplainOwner(token, model)
        }

    suspend fun ownerRegisterComplainList(token: String) = safeApiCall {
        apiService.listRegisterComplainOwner(token)
    }

    suspend fun ownerActionToComplainList(token: String, model: OwnerActionToComplainPostModel) =
        safeApiCall {
            apiService.actionToComplainOwner(token, model)
        }

    suspend fun unPaidOwnerList(token: String, userBillStatus: String, flatId: String?) =
        safeApiCall {
            apiService.unpaidBillOwner(token, userBillStatus, flatId)
        }

    suspend fun notifyUserOwnerList(token: String, billId: String) =
        safeApiCall {
            apiService.notifyOwner(token, billId)
        }
    suspend fun notifyOwnertoTenant(token: String, billId: String) =
        safeApiCall {
            apiService.notifyOwnertoTenant(token, billId)
        }

    suspend fun getCommentOwnerList(token: String, model: OwnerGetCommentPostModel) = safeApiCall {
        apiService.getCommentOwner(token, model)
    }

    suspend fun commentPostOwnerList(token: String, model: OwnerCommentPostModel) = safeApiCall {
        apiService.commentPostOwner(token, model)
    }

    suspend fun createPostOwner(token: String, model: OwnerCreatePostModel) = safeApiCall {
        apiService.createPostOwner(token, model)
    }

    suspend fun editMyCommunityPostOwner(token: String, model: OwnerEditMyCommunityPostModel) =
        safeApiCall {
            apiService.ownerEditMyCommunityList(token, model)
        }

    suspend fun deleteMyCommunityPostOwner(token: String, _id: String) =
        safeApiCall {
            apiService.ownerDeleteMyCommunityList(token, _id)
        }

    suspend fun getVisitorCategoryList(token: String) = safeApiCall {
        apiService.getOwnerVisitorCategoryList(token)
    }

    suspend fun addvisitorOwner(token: String, model: AddRegularEntryOwnerPostModel) = safeApiCall {
        apiService.addVisitorOwner(token, model)
    }

    suspend fun editvisitorOwner(token: String, model: EditRegularEntryOwnerPostModel) =
        safeApiCall {
            apiService.editVisitorOwner(token, model)
        }

    suspend fun addvisitorTenant(token: String, model: AddRegularEntryOwnerPostModel) =
        safeApiCall {
            apiService.addVisitorTenant(token, model)
        }

    suspend fun editVisitorTenant(token: String, model: EditRegularEntryOwnerPostModel) =
        safeApiCall {
            apiService.editVisitorTenant(token, model)
        }

    suspend fun deletevisitorOwner(token: String, visitorId: String) =
        safeApiCall {
            apiService.deleteOwnerVisitor(token, visitorId)
        }

    suspend fun regularvisitorHistoryOwner(token: String, visitorStatus: String, flatId: String?) =
        safeApiCall {
            apiService.regularVistorHistoryOwner(token, visitorStatus, flatId)
        }

    suspend fun regularvisitorHistoryTenant(token: String, visitorStatus: String, flatId: String?) =
        safeApiCall {
            apiService.regularVistorHistoryTenant(token, visitorStatus, flatId)
        }

    suspend fun singlevisitorHistoryTenant(token: String, visitorStatus: String, flatId: String) =
        safeApiCall {
            apiService.singleVistorHistoryTenant(token, visitorStatus, flatId)
        }

    suspend fun singlevisitorHistoryOwner(token: String, visitorStatus: String, flatId: String) =
        safeApiCall {
            apiService.singleVistorHistoryOwner(token, visitorStatus, flatId)
        }

    suspend fun visitorActionTenant(token: String, visitorId: String, visitorStatus: String) =
        safeApiCall {
            apiService.tenantVisitorAction(token, visitorId, visitorStatus)
        }

    suspend fun visitorActionOwner(token: String, visitorId: String, visitorStatus: String) =
        safeApiCall {
            apiService.ownerVisitorAction(token, visitorId, visitorStatus)
        }

    suspend fun rejectvisitorActionTenant(token: String, visitorId: String, visitorStatus: String) =
        safeApiCall {
            apiService.tenantVisitorAction(token, visitorId, visitorStatus)
        }

    suspend fun rejectvisitorActionOwner(token: String, visitorId: String, visitorStatus: String) =
        safeApiCall {
            apiService.ownerVisitorAction(token, visitorId, visitorStatus)
        }

    suspend fun ownerTenantRegularEntryHistoryDetailsOwner(token: String, visitorId: String) =
        safeApiCall {
            apiService.ownerTenantEntryHistoryDetailsOwner(token, visitorId)
        }

    suspend fun ownerTenantRegularEntryHistoryDetailsTenant(token: String, visitorId: String) =
        safeApiCall {
            apiService.ownerTenantEntryHistoryDetailsTenant(token, visitorId)
        }

    suspend fun markAsPaidOwnerRes(token: String, billId: String) =
        safeApiCall {
            apiService.markAsPaidOwnerRes(token, billId)
        }

    suspend fun rejectBillOwnerRes(token: String, billId: String,rejectReason: String) =
        safeApiCall {
            apiService.rejectBillOwnerRes(token, billId,rejectReason)
        }

    suspend fun addGatePassOwner(token: String, model: OwnerAddGatePassPostModal) =
        safeApiCall {
            apiService.addGatePassOwner(token, model)
        }

    suspend fun editGatePassOwner(token: String, model: OwnerEditGatePassPostModel) =
        safeApiCall {
            apiService.editGatePassOwner(token, model)
        }

    suspend fun editGatePassTenant(token: String, model: OwnerEditGatePassPostModel) =
        safeApiCall {
            apiService.editGatePassTenant(token, model)
        }

    suspend fun addGatePassTenant(token: String, model: OwnerAddGatePassPostModal) =
        safeApiCall {
            apiService.addGatePassTenant(token, model)
        }

    suspend fun gatePassListOwner(token: String, status: String, flatId: String) =
        safeApiCall {
            apiService.gatePassListOwner(token, status, flatId)
        }

    suspend fun deleteCommentOwner(token: String, commentId: String, post: String) =
        safeApiCall {
            apiService.ownerDeleteCommentList(token, commentId, post)
        }

    suspend fun editCommentOwner(token: String, model: EditCommentPostModel) =
        safeApiCall {
            apiService.ownerEditCommentList(token, model)
        }
    suspend fun deleteCommentTenant(token: String, commentId: String, post: String) =
        safeApiCall {
            apiService.tenantDeleteCommentList(token, commentId, post)
        }
    suspend fun editCommentTenant(token: String, model: EditCommentPostModel) =
        safeApiCall {
            apiService.tenantEditCommentList(token, model)
        }
}