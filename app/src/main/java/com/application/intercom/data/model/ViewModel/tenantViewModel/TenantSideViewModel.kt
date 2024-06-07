package com.application.intercom.data.model.ViewModel.tenantViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.intercom.data.model.local.owner.addRegularEntry.AddRegularEntryOwnerPostModel
import com.application.intercom.data.model.local.owner.registerComplain.OwnerRegisterComplainPostModel
import com.application.intercom.data.model.local.tenant.TenantPayInAdvancePostModel
import com.application.intercom.data.model.local.tenant.TenantPayNowPostModel
import com.application.intercom.data.model.remote.owner.addRegularEntry.AddRegularEntryOwnerRes
import com.application.intercom.data.model.remote.owner.notifyUser.OwnerNotifyUserList
import com.application.intercom.data.model.remote.owner.registerComplain.OwnerRegisterComplainRes
import com.application.intercom.data.model.remote.tenant.tenantSide.TenantComplainListRes
import com.application.intercom.data.model.remote.tenant.tenantSide.TenantNoticeListRes
import com.application.intercom.data.model.remote.tenant.tenantSide.TenantViewPostDetailsLIst
import com.application.intercom.data.model.remote.tenant.tenantSide.payInAdavance.TenantPayInAdavanceList
import com.application.intercom.data.model.remote.tenant.tenantSide.payNow.TenantPayNowRes
import com.application.intercom.data.model.remote.tenant.tenantSide.tenantBill.TenantUnPaidListRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.tenantRepo.TenantSideRepo
import kotlinx.coroutines.launch

class TenantSideViewModel(private val repository: TenantSideRepo) : ViewModel() {
    private val _tenantNoticeListLiveData = MutableLiveData<EmpResource<TenantNoticeListRes>>()
    val tenantNoticeLiveData: LiveData<EmpResource<TenantNoticeListRes>>
        get() = _tenantNoticeListLiveData

    fun tenantNoticeList(token: String) {
        viewModelScope.launch {
            _tenantNoticeListLiveData.value = EmpResource.Loading
            _tenantNoticeListLiveData.value = repository.tenantNoticeList(token)
        }
    }

    private val _tenant_registerComplainLiveData =
        MutableLiveData<EmpResource<OwnerRegisterComplainRes>>()
    val tenantRegisterComplainLiveData: LiveData<EmpResource<OwnerRegisterComplainRes>>
        get() = _tenant_registerComplainLiveData

    fun tenantRegisterComplain(token: String, model: OwnerRegisterComplainPostModel) {
        viewModelScope.launch {
            _tenant_registerComplainLiveData.value = EmpResource.Loading
            _tenant_registerComplainLiveData.value = repository.tenantregisterComplain(token, model)
        }
    }

    private val _tenantComplainListLiveData = MutableLiveData<EmpResource<TenantComplainListRes>>()
    val tenantComplainListLiveData: LiveData<EmpResource<TenantComplainListRes>>
        get() = _tenantComplainListLiveData

    fun tenantComplainList(token: String) {
        viewModelScope.launch {
            _tenantComplainListLiveData.value = EmpResource.Loading
            _tenantComplainListLiveData.value = repository.tenantComplainList(token)
        }
    }

    private val _tenantUnPaidLiveData = MutableLiveData<EmpResource<TenantUnPaidListRes>>()
    val tenantUnpaidLiveData: LiveData<EmpResource<TenantUnPaidListRes>>
        get() = _tenantUnPaidLiveData

    private val _tenantPendingLiveData = MutableLiveData<EmpResource<TenantUnPaidListRes>>()
    val tenantPendingLiveData: LiveData<EmpResource<TenantUnPaidListRes>>
        get() = _tenantPendingLiveData

    fun tenantUnpaidList(token: String, userBillStatus: String, flatId: String?) {
        viewModelScope.launch {
            if (userBillStatus == "Unapproved") {
                _tenantPendingLiveData.value = EmpResource.Loading
                _tenantPendingLiveData.value =
                    repository.tenantUnPaidList(token, userBillStatus, flatId)
            } else {
                _tenantUnPaidLiveData.value = EmpResource.Loading
                _tenantUnPaidLiveData.value =
                    repository.tenantUnPaidList(token, userBillStatus, flatId)
            }

        }
    }

    private val _notifyUserTenantLiveData = MutableLiveData<EmpResource<OwnerNotifyUserList>>()
    val notifyUserTenantLiveData: LiveData<EmpResource<OwnerNotifyUserList>>
        get() = _notifyUserTenantLiveData

    fun notifyUserTenantList(token: String, billId: String) {
        viewModelScope.launch {
            _notifyUserTenantLiveData.value = EmpResource.Loading
            _notifyUserTenantLiveData.value = repository.notifyUserTenantList(token, billId)
        }
    }

    private val _payNowTenantLiveData = MutableLiveData<EmpResource<TenantPayNowRes>>()
    val payNowTenantLiveData: LiveData<EmpResource<TenantPayNowRes>>
        get() = _payNowTenantLiveData

    fun payNowTenantList(token: String, model: TenantPayNowPostModel) {
        viewModelScope.launch {
            _payNowTenantLiveData.value = EmpResource.Loading
            _payNowTenantLiveData.value = repository.payNowtenant(token, model)
        }
    }

    private val _payNowManagertLiveData = MutableLiveData<EmpResource<TenantPayNowRes>>()
    val payNowManagerLiveData: LiveData<EmpResource<TenantPayNowRes>>
        get() = _payNowManagertLiveData

    fun payNowManagerList(token: String, model: TenantPayNowPostModel) {
        viewModelScope.launch {
            _payNowManagertLiveData.value = EmpResource.Loading
            _payNowManagertLiveData.value = repository.payNowManager(token, model)
        }
    }

    private val _payNowOwnertLiveData = MutableLiveData<EmpResource<TenantPayNowRes>>()
    val payNowOwnerLiveData: LiveData<EmpResource<TenantPayNowRes>>
        get() = _payNowOwnertLiveData

    fun payNowOwnerList(token: String, model: TenantPayNowPostModel) {
        viewModelScope.launch {
            _payNowOwnertLiveData.value = EmpResource.Loading
            _payNowOwnertLiveData.value = repository.payNowOwner(token, model)
        }
    }

    private val _payInAdvanceTenantLiveData =
        MutableLiveData<EmpResource<TenantPayInAdavanceList>>()
    val payInAdvanceTenantLiveData: LiveData<EmpResource<TenantPayInAdavanceList>>
        get() = _payInAdvanceTenantLiveData

    fun payInAdvanceTenant(token: String, model: TenantPayInAdvancePostModel) {
        viewModelScope.launch {
            _payInAdvanceTenantLiveData.value = EmpResource.Loading
            _payInAdvanceTenantLiveData.value = repository.payInAdvancetenant(token, model)
        }
    }


    private val _viewPostDetailsTenantLiveData =
        MutableLiveData<EmpResource<TenantViewPostDetailsLIst>>()
    val viewPostDetailsTenantLiveData: LiveData<EmpResource<TenantViewPostDetailsLIst>>
        get() = _viewPostDetailsTenantLiveData

    fun viewPostDetailsTenant(token: String, postId: String) {
        viewModelScope.launch {
            _viewPostDetailsTenantLiveData.value = EmpResource.Loading
            _viewPostDetailsTenantLiveData.value = repository.viewPostDetailsTenant(token, postId)
        }
    }

    private val _viewPostDetailsOwnerLiveData =
        MutableLiveData<EmpResource<TenantViewPostDetailsLIst>>()
    val viewPostDetailsOwnerLiveData: LiveData<EmpResource<TenantViewPostDetailsLIst>>
        get() = _viewPostDetailsOwnerLiveData

    fun viewPostDetailsOwner(token: String, postId: String) {
        viewModelScope.launch {
            _viewPostDetailsOwnerLiveData.value = EmpResource.Loading
            _viewPostDetailsOwnerLiveData.value = repository.viewPostDetailsOwner(token, postId)
        }
    }


}