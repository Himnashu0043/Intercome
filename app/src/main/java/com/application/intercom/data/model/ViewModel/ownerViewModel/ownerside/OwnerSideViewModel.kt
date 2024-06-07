package com.application.intercom.data.model.ViewModel.ownerViewModel.ownerside

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import com.application.intercom.data.model.remote.owner.regularHistory.RegularHistoryList
import com.application.intercom.data.model.remote.CommonResponse
import com.application.intercom.data.model.remote.gateKeeper.gateKeeperHome.GetVisitorCategoryList
import com.application.intercom.data.model.remote.gateKeeper.gateKeeperHome.RegularEntryHistoryDetailsList
import com.application.intercom.data.model.remote.manager.managerSide.bill.ManagerMarkAsPaidRes
import com.application.intercom.data.model.remote.manager.managerSide.bill.RejectBillManagerRes
import com.application.intercom.data.model.remote.owner.addRegularEntry.AddRegularEntryOwnerRes
import com.application.intercom.data.model.remote.owner.bill.OwnerUnPaidBillListRes
import com.application.intercom.data.model.remote.owner.commentPost.OwnerCommentPostRes
import com.application.intercom.data.model.remote.owner.community.OwnerEditMyCommunityRes
import com.application.intercom.data.model.remote.owner.gatePass.OwnerAddGatePassList
import com.application.intercom.data.model.remote.owner.gatePass.OwnerGatepassList
import com.application.intercom.data.model.remote.owner.getComment.OwnerDeleteCommentRes
import com.application.intercom.data.model.remote.owner.getComment.OwnerEditCommentRes
import com.application.intercom.data.model.remote.owner.getComment.OwnerGetCommentList
import com.application.intercom.data.model.remote.owner.noticBoard.OwnerNoticBoardListRes
import com.application.intercom.data.model.remote.owner.noticBoard.OwnerViewNoticeRes
import com.application.intercom.data.model.remote.owner.notifyUser.OwnerNotifyUserList
import com.application.intercom.data.model.remote.owner.ownerHome.BillCountOwnerRes
import com.application.intercom.data.model.remote.owner.registerComplain.OwnerRegisterComplainList
import com.application.intercom.data.model.remote.owner.registerComplain.OwnerRegisterComplainRes
import com.application.intercom.data.model.remote.singleEntryHistory.OwnerTenantSingleEntryHistoryList
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.ownerRepo.ownerSide.OwnerSideRepo
import kotlinx.coroutines.launch

class OwnerSideViewModel(private val repository: OwnerSideRepo) : ViewModel() {
    private val _ownerNoticeListLiveData = MutableLiveData<EmpResource<OwnerNoticBoardListRes>>()
    val ownerNoticeLiveData: LiveData<EmpResource<OwnerNoticBoardListRes>>
        get() = _ownerNoticeListLiveData

    fun ownerNoticeList(token: String) {
        viewModelScope.launch {
            _ownerNoticeListLiveData.value = EmpResource.Loading
            _ownerNoticeListLiveData.value = repository.ownerNoticeList(token)
        }
    }

    private val _billCountOwnerLiveData = MutableLiveData<EmpResource<BillCountOwnerRes>>()
    val billCountOwnerLiveData: LiveData<EmpResource<BillCountOwnerRes>>
        get() = _billCountOwnerLiveData

    fun billCountOwner(token: String) {
        viewModelScope.launch {
            _billCountOwnerLiveData.value = EmpResource.Loading
            _billCountOwnerLiveData.value = repository.billCountOwner(token)
        }
    }

    private val _viewNoticeOwnerLiveData = MutableLiveData<EmpResource<OwnerViewNoticeRes>>()
    val viewNoticeOwnerLiveData: LiveData<EmpResource<OwnerViewNoticeRes>>
        get() = _viewNoticeOwnerLiveData

    fun viewNoticeOwner(token: String, _id: String) {
        viewModelScope.launch {
            _viewNoticeOwnerLiveData.value = EmpResource.Loading
            _viewNoticeOwnerLiveData.value = repository.ownerViewNotice(token, _id)
        }
    }


    private val _registerComplainOwnerLiveData =
        MutableLiveData<EmpResource<OwnerRegisterComplainRes>>()
    val registerComplainOwnerLiveData: LiveData<EmpResource<OwnerRegisterComplainRes>>
        get() = _registerComplainOwnerLiveData

    fun registerComplainOwner(token: String, model: OwnerRegisterComplainPostModel) {
        viewModelScope.launch {
            _registerComplainOwnerLiveData.value = EmpResource.Loading
            _registerComplainOwnerLiveData.value = repository.ownerregisterComplain(token, model)
        }
    }

    private val _listregisterComplainOwnerLiveData =
        MutableLiveData<EmpResource<OwnerRegisterComplainList>>()
    val listregisterComplainOwnerLiveData: LiveData<EmpResource<OwnerRegisterComplainList>>
        get() = _listregisterComplainOwnerLiveData

    fun listregisterComplainOwner(token: String) {
        viewModelScope.launch {
            _listregisterComplainOwnerLiveData.value = EmpResource.Loading
            _listregisterComplainOwnerLiveData.value = repository.ownerRegisterComplainList(token)
        }
    }

    private val _actionToComplainOwnerLiveData = MutableLiveData<EmpResource<CommonResponse>>()
    val actionToComplainOwnerLiveData: LiveData<EmpResource<CommonResponse>>
        get() = _actionToComplainOwnerLiveData

    fun actionToComplainOwner(token: String, model: OwnerActionToComplainPostModel) {
        viewModelScope.launch {
            _actionToComplainOwnerLiveData.value = EmpResource.Loading
            _actionToComplainOwnerLiveData.value =
                repository.ownerActionToComplainList(token, model)
        }
    }

    private val _unPaidOwnerLiveData = MutableLiveData<EmpResource<OwnerUnPaidBillListRes>>()
    val unPaidOwnerListLiveData: LiveData<EmpResource<OwnerUnPaidBillListRes>>
        get() = _unPaidOwnerLiveData

    fun unPaidOwnerList(token: String, userBillStatus: String, flatId: String?) {
        viewModelScope.launch {
            _unPaidOwnerLiveData.value = EmpResource.Loading
            _unPaidOwnerLiveData.value =
                repository.unPaidOwnerList(token, userBillStatus, flatId)
        }
    }

    private val _notifyUserOwnerLiveData = MutableLiveData<EmpResource<OwnerNotifyUserList>>()
    val notifyUserOwnerListLiveData: LiveData<EmpResource<OwnerNotifyUserList>>
        get() = _notifyUserOwnerLiveData

    fun notifyUserOwnerList(token: String, billId: String) {
        viewModelScope.launch {
            _notifyUserOwnerLiveData.value = EmpResource.Loading
            _notifyUserOwnerLiveData.value =
                repository.notifyUserOwnerList(token, billId)
        }
    }

    private val _notifyOwnertoTenantLiveData = MutableLiveData<EmpResource<OwnerNotifyUserList>>()
    val notifyOwnertoTenantLiveData: LiveData<EmpResource<OwnerNotifyUserList>>
        get() = _notifyOwnertoTenantLiveData

    fun notifyOwnertoTenant(token: String, billId: String) {
        viewModelScope.launch {
            _notifyOwnertoTenantLiveData.value = EmpResource.Loading
            _notifyOwnertoTenantLiveData.value =
                repository.notifyOwnertoTenant(token, billId)
        }
    }


    private val _getCommentOwnerLiveData = MutableLiveData<EmpResource<OwnerGetCommentList>>()
    val getCommentOwnerListLiveData: LiveData<EmpResource<OwnerGetCommentList>>
        get() = _getCommentOwnerLiveData

    fun getCommentOwnerList(token: String, model: OwnerGetCommentPostModel) {
        viewModelScope.launch {
            _getCommentOwnerLiveData.value = EmpResource.Loading
            _getCommentOwnerLiveData.value =
                repository.getCommentOwnerList(token, model)
        }
    }

    private val _ommentPostOwnerLiveData = MutableLiveData<EmpResource<OwnerCommentPostRes>>()
    val commentPostOwnerListLiveData: LiveData<EmpResource<OwnerCommentPostRes>>
        get() = _ommentPostOwnerLiveData

    fun commentPostOwnerList(token: String, model: OwnerCommentPostModel) {
        viewModelScope.launch {
            _ommentPostOwnerLiveData.value = EmpResource.Loading
            _ommentPostOwnerLiveData.value =
                repository.commentPostOwnerList(token, model)
        }
    }

    private val _createPostOwnerLiveData = MutableLiveData<EmpResource<CommonResponse>>()
    val createPostOwnerLiveData: LiveData<EmpResource<CommonResponse>>
        get() = _createPostOwnerLiveData

    fun createPostOwner(token: String, model: OwnerCreatePostModel) {
        viewModelScope.launch {
            _createPostOwnerLiveData.value = EmpResource.Loading
            _createPostOwnerLiveData.value =
                repository.createPostOwner(token, model)
        }
    }

    private val _editMyCommunityPostOwnerLiveData =
        MutableLiveData<EmpResource<OwnerEditMyCommunityRes>>()
    val editMyCommunityPostOwnerLiveData: LiveData<EmpResource<OwnerEditMyCommunityRes>>
        get() = _editMyCommunityPostOwnerLiveData

    fun editMyCommunityPostOwner(token: String, model: OwnerEditMyCommunityPostModel) {
        viewModelScope.launch {
            _editMyCommunityPostOwnerLiveData.value = EmpResource.Loading
            _editMyCommunityPostOwnerLiveData.value =
                repository.editMyCommunityPostOwner(token, model)
        }
    }

    private val _deleteMyCommunityPostOwnerLiveData =
        MutableLiveData<EmpResource<CommonResponse>>()
    val deleteMyCommunityPostOwnerLiveData: LiveData<EmpResource<CommonResponse>>
        get() = _deleteMyCommunityPostOwnerLiveData

    fun deleteMyCommunityPostOwner(token: String, _id: String) {
        viewModelScope.launch {
            _deleteMyCommunityPostOwnerLiveData.value = EmpResource.Loading
            _deleteMyCommunityPostOwnerLiveData.value =
                repository.deleteMyCommunityPostOwner(token, _id)
        }
    }

    private val _visitorCategoryListLiveData =
        MutableLiveData<EmpResource<GetVisitorCategoryList>>()
    val visitorCategoryListLiveData: LiveData<EmpResource<GetVisitorCategoryList>>
        get() = _visitorCategoryListLiveData

    fun visitorCategoryList(token: String) {
        viewModelScope.launch {
            _visitorCategoryListLiveData.value = EmpResource.Loading
            _visitorCategoryListLiveData.value = repository.getVisitorCategoryList(token)
        }
    }

    private val _addVisitorOwnerLiveData =
        MutableLiveData<EmpResource<AddRegularEntryOwnerRes>>()
    val addVisitorOwnerLiveData: LiveData<EmpResource<AddRegularEntryOwnerRes>>
        get() = _addVisitorOwnerLiveData

    fun addVisitorOwner(token: String, model: AddRegularEntryOwnerPostModel) {
        viewModelScope.launch {
            _addVisitorOwnerLiveData.value = EmpResource.Loading
            _addVisitorOwnerLiveData.value = repository.addvisitorOwner(token, model)
        }
    }

    private val _addVisitorTenantLiveData =
        MutableLiveData<EmpResource<AddRegularEntryOwnerRes>>()
    val addVisitorTenantLiveData: LiveData<EmpResource<AddRegularEntryOwnerRes>>
        get() = _addVisitorTenantLiveData

    fun addVisitorTenant(token: String, model: AddRegularEntryOwnerPostModel) {
        viewModelScope.launch {
            _addVisitorTenantLiveData.value = EmpResource.Loading
            _addVisitorTenantLiveData.value = repository.addvisitorTenant(token, model)
        }
    }

    private val _deleteVisitorOwnerLiveData =
        MutableLiveData<EmpResource<CommonResponse>>()
    val deleteVisitorOwnerLiveData: LiveData<EmpResource<CommonResponse>>
        get() = _deleteVisitorOwnerLiveData

    fun deleteVisitorOwner(token: String, visitorId: String) {
        viewModelScope.launch {
            _deleteVisitorOwnerLiveData.value = EmpResource.Loading
            _deleteVisitorOwnerLiveData.value = repository.deletevisitorOwner(token, visitorId)
        }
    }

    private val _editVisitorOwnerLiveData =
        MutableLiveData<EmpResource<CommonResponse>>()
    val editVisitorOwnertLiveData: LiveData<EmpResource<CommonResponse>>
        get() = _editVisitorOwnerLiveData

    fun editVisitorOwner(token: String, model: EditRegularEntryOwnerPostModel) {
        viewModelScope.launch {
            _editVisitorOwnerLiveData.value = EmpResource.Loading
            _editVisitorOwnerLiveData.value = repository.editvisitorOwner(token, model)
        }
    }

    private val _editVisitorTenantLiveData =
        MutableLiveData<EmpResource<CommonResponse>>()
    val editVisitorTenantLiveData: LiveData<EmpResource<CommonResponse>>
        get() = _editVisitorTenantLiveData

    fun editVisitorTenant(token: String, model: EditRegularEntryOwnerPostModel) {
        viewModelScope.launch {
            _editVisitorTenantLiveData.value = EmpResource.Loading
            _editVisitorTenantLiveData.value = repository.editVisitorTenant(token, model)
        }
    }

    private val _regularVisitorHistoryOwnerLiveData =
        MutableLiveData<EmpResource<RegularHistoryList>>()
    val regularVisitorHistoryOwnerLiveData: LiveData<EmpResource<RegularHistoryList>>
        get() = _regularVisitorHistoryOwnerLiveData

    fun regularVisitorHistoryOwner(token: String, visitorStatus: String, flatId: String?) {
        viewModelScope.launch {
            _regularVisitorHistoryOwnerLiveData.value = EmpResource.Loading
            _regularVisitorHistoryOwnerLiveData.value =
                repository.regularvisitorHistoryOwner(token, visitorStatus, flatId)
        }
    }


    private val _regularVisitorHistoryTenantLiveData =
        MutableLiveData<EmpResource<RegularHistoryList>>()
    val regularVisitorHistoryTenantLiveData: LiveData<EmpResource<RegularHistoryList>>
        get() = _regularVisitorHistoryTenantLiveData

    fun regularVisitorHistoryTenant(token: String, visitorStatus: String, flatId: String?) {
        viewModelScope.launch {
            _regularVisitorHistoryTenantLiveData.value = EmpResource.Loading
            _regularVisitorHistoryTenantLiveData.value =
                repository.regularvisitorHistoryTenant(token, visitorStatus, flatId)
        }
    }

    private val _singleVisitorHistoryTenantLiveData =
        MutableLiveData<EmpResource<OwnerTenantSingleEntryHistoryList>>()
    val singleVisitorHistoryTenantLiveData: LiveData<EmpResource<OwnerTenantSingleEntryHistoryList>>
        get() = _singleVisitorHistoryTenantLiveData

    fun singleVisitorHistoryTenant(token: String, visitorStatus: String, flatId: String) {
        viewModelScope.launch {
            _singleVisitorHistoryTenantLiveData.value = EmpResource.Loading
            _singleVisitorHistoryTenantLiveData.value =
                repository.singlevisitorHistoryTenant(token, visitorStatus, flatId)
        }
    }

    private val _singleVisitorHistoryOwnerLiveData =
        MutableLiveData<EmpResource<OwnerTenantSingleEntryHistoryList>>()
    val singleVisitorHistoryOwnerLiveData: LiveData<EmpResource<OwnerTenantSingleEntryHistoryList>>
        get() = _singleVisitorHistoryOwnerLiveData

    fun singleVisitorHistoryOwner(token: String, visitorStatus: String, flatId: String) {
        viewModelScope.launch {
            _singleVisitorHistoryOwnerLiveData.value = EmpResource.Loading
            _singleVisitorHistoryOwnerLiveData.value =
                repository.singlevisitorHistoryOwner(token, visitorStatus, flatId)
        }
    }

    private val _visitorActionTenantLiveData =
        MutableLiveData<EmpResource<CommonResponse>>()
    val visitorActionTenantLiveData: LiveData<EmpResource<CommonResponse>>
        get() = _visitorActionTenantLiveData

    fun visitorActionTenant(token: String, visitorId: String, visitorStatus: String) {
        viewModelScope.launch {
            _visitorActionTenantLiveData.value = EmpResource.Loading
            _visitorActionTenantLiveData.value =
                repository.visitorActionTenant(token, visitorId, visitorStatus)
        }
    }

    private val _visitorActionOwnerLiveData =
        MutableLiveData<EmpResource<CommonResponse>>()
    val visitorActionOwnerLiveData: LiveData<EmpResource<CommonResponse>>
        get() = _visitorActionOwnerLiveData

    fun visitorActionOwner(token: String, visitorId: String, visitorStatus: String) {
        viewModelScope.launch {
            _visitorActionOwnerLiveData.value = EmpResource.Loading
            _visitorActionOwnerLiveData.value =
                repository.visitorActionOwner(token, visitorId, visitorStatus)
        }
    }

    private val _rejectvisitorActionTenantLiveData =
        MutableLiveData<EmpResource<CommonResponse>>()
    val rejectvisitorActionTenantLiveData: LiveData<EmpResource<CommonResponse>>
        get() = _rejectvisitorActionTenantLiveData

    fun rejectvisitorActionTenant(token: String, visitorId: String, visitorStatus: String) {
        viewModelScope.launch {
            _rejectvisitorActionTenantLiveData.value = EmpResource.Loading
            _rejectvisitorActionTenantLiveData.value =
                repository.rejectvisitorActionTenant(token, visitorId, visitorStatus)
        }
    }

    private val _rejectvisitorActionOwnerLiveData =
        MutableLiveData<EmpResource<CommonResponse>>()
    val rejectvisitorActionOwnerLiveData: LiveData<EmpResource<CommonResponse>>
        get() = _rejectvisitorActionOwnerLiveData

    fun rejectvisitorActionOwner(token: String, visitorId: String, visitorStatus: String) {
        viewModelScope.launch {
            _rejectvisitorActionOwnerLiveData.value = EmpResource.Loading
            _rejectvisitorActionOwnerLiveData.value =
                repository.rejectvisitorActionOwner(token, visitorId, visitorStatus)
        }
    }

    private val _ownerTenantRegularEntryHistoryDetailsOwnerLiveData =
        MutableLiveData<EmpResource<RegularEntryHistoryDetailsList>>()
    val ownerTenantRegularEntryHistoryDetailsOwnerLiveData: LiveData<EmpResource<RegularEntryHistoryDetailsList>>
        get() = _ownerTenantRegularEntryHistoryDetailsOwnerLiveData

    fun ownerTenantRegularEntryHistoryDetailsOwner(token: String, visitorId: String) {
        viewModelScope.launch {
            _ownerTenantRegularEntryHistoryDetailsOwnerLiveData.value = EmpResource.Loading
            _ownerTenantRegularEntryHistoryDetailsOwnerLiveData.value =
                repository.ownerTenantRegularEntryHistoryDetailsOwner(token, visitorId)
        }
    }

    private val _ownerTenantRegularEntryHistoryDetailsTenantLiveData =
        MutableLiveData<EmpResource<RegularEntryHistoryDetailsList>>()
    val ownerTenantRegularEntryHistoryDetailsTenantLiveData: LiveData<EmpResource<RegularEntryHistoryDetailsList>>
        get() = _ownerTenantRegularEntryHistoryDetailsTenantLiveData

    fun ownerTenantRegularEntryHistoryDetailsTenant(token: String, visitorId: String) {
        viewModelScope.launch {
            _ownerTenantRegularEntryHistoryDetailsTenantLiveData.value = EmpResource.Loading
            _ownerTenantRegularEntryHistoryDetailsTenantLiveData.value =
                repository.ownerTenantRegularEntryHistoryDetailsTenant(token, visitorId)
        }
    }

    private val _markAsPaidOwnerLiveData = MutableLiveData<EmpResource<ManagerMarkAsPaidRes>>()
    val markAsPaidOwnerLiveData: LiveData<EmpResource<ManagerMarkAsPaidRes>>
        get() = _markAsPaidOwnerLiveData

    fun markAsPaidOwner(token: String, billId: String) {
        viewModelScope.launch {
            _markAsPaidOwnerLiveData.value = EmpResource.Loading
            _markAsPaidOwnerLiveData.value = repository.markAsPaidOwnerRes(token, billId)
        }
    }


    private val _rejectBillOwnerLiveData = MutableLiveData<EmpResource<RejectBillManagerRes>>()
    val rejectBillOwnerLiveData: LiveData<EmpResource<RejectBillManagerRes>>
        get() = _rejectBillOwnerLiveData

    fun rejectBillOwner(token: String, billId: String, rejectReason: String) {
        viewModelScope.launch {
            _rejectBillOwnerLiveData.value = EmpResource.Loading
            _rejectBillOwnerLiveData.value =
                repository.rejectBillOwnerRes(token, billId, rejectReason)
        }
    }

    private val _addGatePassOwnerLiveData =
        MutableLiveData<EmpResource<OwnerAddGatePassList>>()
    val addGatePassOwnerLiveData: LiveData<EmpResource<OwnerAddGatePassList>>
        get() = _addGatePassOwnerLiveData

    fun addGatePassOwner(token: String, model: OwnerAddGatePassPostModal) {
        viewModelScope.launch {
            _addGatePassOwnerLiveData.value = EmpResource.Loading
            _addGatePassOwnerLiveData.value = repository.addGatePassOwner(token, model)
        }
    }

    private val _editGatePassOwnerLiveData =
        MutableLiveData<EmpResource<CommonResponse>>()
    val editGatePassOwnerLiveData: LiveData<EmpResource<CommonResponse>>
        get() = _editGatePassOwnerLiveData

    fun editGatePassOwner(token: String, model: OwnerEditGatePassPostModel) {
        viewModelScope.launch {
            _editGatePassOwnerLiveData.value = EmpResource.Loading
            _editGatePassOwnerLiveData.value = repository.editGatePassOwner(token, model)
        }
    }

    private val _editGatePassTenantLiveData =
        MutableLiveData<EmpResource<CommonResponse>>()
    val editGatePassTenantLiveData: LiveData<EmpResource<CommonResponse>>
        get() = _editGatePassTenantLiveData

    fun editGatePassTenant(token: String, model: OwnerEditGatePassPostModel) {
        viewModelScope.launch {
            _editGatePassTenantLiveData.value = EmpResource.Loading
            _editGatePassTenantLiveData.value = repository.editGatePassTenant(token, model)
        }
    }


    private val _addGatePassTenantLiveData =
        MutableLiveData<EmpResource<OwnerAddGatePassList>>()
    val addGatePassTenantLiveData: LiveData<EmpResource<OwnerAddGatePassList>>
        get() = _addGatePassTenantLiveData

    fun addGatePassTenant(token: String, model: OwnerAddGatePassPostModal) {
        viewModelScope.launch {
            _addGatePassTenantLiveData.value = EmpResource.Loading
            _addGatePassTenantLiveData.value = repository.addGatePassTenant(token, model)
        }
    }

    private val _gatePassListOwnerLiveData =
        MutableLiveData<EmpResource<OwnerGatepassList>>()
    val gatePassListOwnerLiveData: LiveData<EmpResource<OwnerGatepassList>>
        get() = _gatePassListOwnerLiveData

    fun gatePassListOwner(token: String, status: String, flatId: String) {
        viewModelScope.launch {
            _gatePassListOwnerLiveData.value = EmpResource.Loading
            _gatePassListOwnerLiveData.value = repository.gatePassListOwner(token, status, flatId)
        }
    }

    private val _deleteCommentOwnerLiveData =
        MutableLiveData<EmpResource<OwnerDeleteCommentRes>>()
    val deleteCommentOwnerLiveData: LiveData<EmpResource<OwnerDeleteCommentRes>>
        get() = _deleteCommentOwnerLiveData

    fun deleteCommentOwner(token: String, commentId: String, post: String) {
        viewModelScope.launch {
            _deleteCommentOwnerLiveData.value = EmpResource.Loading
            _deleteCommentOwnerLiveData.value =
                repository.deleteCommentOwner(token, commentId, post)
        }
    }

    private val _editCommentOwnerLiveData =
        MutableLiveData<EmpResource<OwnerEditCommentRes>>()
    val editCommentOwnerLiveData: LiveData<EmpResource<OwnerEditCommentRes>>
        get() = _editCommentOwnerLiveData

    fun editCommentOwner(token: String, model: EditCommentPostModel) {
        viewModelScope.launch {
            _editCommentOwnerLiveData.value = EmpResource.Loading
            _editCommentOwnerLiveData.value = repository.editCommentOwner(token, model)
        }
    }

    private val _deleteCommentTenantLiveData =
        MutableLiveData<EmpResource<OwnerDeleteCommentRes>>()
    val deleteCommentTenantLiveData: LiveData<EmpResource<OwnerDeleteCommentRes>>
        get() = _deleteCommentTenantLiveData

    fun deleteCommentTenant(token: String, commentId: String, post: String) {
        viewModelScope.launch {
            _deleteCommentTenantLiveData.value = EmpResource.Loading
            _deleteCommentTenantLiveData.value =
                repository.deleteCommentTenant(token, commentId, post)
        }
    }

    private val _editCommentTenantLiveData =
        MutableLiveData<EmpResource<OwnerEditCommentRes>>()
    val editCommentTenantLiveData: LiveData<EmpResource<OwnerEditCommentRes>>
        get() = _editCommentTenantLiveData

    fun editCommentTenant(token: String, model: EditCommentPostModel) {
        viewModelScope.launch {
            _editCommentTenantLiveData.value = EmpResource.Loading
            _editCommentTenantLiveData.value = repository.editCommentTenant(token, model)
        }
    }

}