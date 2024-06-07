package com.application.intercom.data.model.ViewModel.ownerViewModel.ownerHome

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.intercom.data.model.local.FIlter_MonthsModel
import com.application.intercom.data.model.local.manager.managerSide.rentEdit.RentEditManagerPostModel
import com.application.intercom.data.model.local.manager.managerSide.serviceCharge.ManagerAddServiceChargePostModel
import com.application.intercom.data.model.local.owner.OwnerAddTenantPostModel
import com.application.intercom.data.model.local.owner.OwnerCommunityPostModel
import com.application.intercom.data.model.local.owner.OwnerUpdateTenantPostModel
import com.application.intercom.data.model.local.owner.likeCommunty.OwnerLikeCommunityPostModel
import com.application.intercom.data.model.remote.CommonResponse
import com.application.intercom.data.model.remote.manager.managerSide.bill.EditPendingBillManagerRes
import com.application.intercom.data.model.remote.manager.managerSide.newflow.DeleteUnPaidManagerRes
import com.application.intercom.data.model.remote.manager.managerSide.rent.AddRentManagerPostModel
import com.application.intercom.data.model.remote.manager.managerSide.rent.RentManagerListRes
import com.application.intercom.data.model.remote.manager.managerSide.serviceCharege.ManagerAddServiceChargesListRes
import com.application.intercom.data.model.remote.owner.community.OwnerCommunityListRes
import com.application.intercom.data.model.remote.owner.community.OwnerLikeCommunityRes
import com.application.intercom.data.model.remote.owner.community.OwnerMyCommunityListRes
import com.application.intercom.data.model.remote.owner.flat.OwnerFlatListRes
import com.application.intercom.data.model.remote.owner.gateKeeper.OwnerGateKeeperList
import com.application.intercom.data.model.remote.owner.ownerHome.OwnerAdvertisementRes
import com.application.intercom.data.model.remote.owner.ownerHome.OwnerDetailsRes
import com.application.intercom.data.model.remote.owner.ownerTenantHistory.OwnerTenantCurrentHistoryListRes
import com.application.intercom.data.model.remote.owner.parking.OwnerParkingListRes
import com.application.intercom.data.model.remote.owner.setAsHome.OwnerSetasHomeList
import com.application.intercom.data.model.remote.owner.viewPostCount.ViewPostCountList
import com.application.intercom.data.model.remote.tenant.tenantSide.getAllMember.GetAllMemberListRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.ownerRepo.ownerHome.OwnerHomeRepo
import kotlinx.coroutines.launch

class OwnerHomeViewModel(private val repository: OwnerHomeRepo) : ViewModel() {
    private val _ownerDetailsLiveData = MutableLiveData<EmpResource<OwnerDetailsRes>>()
    val ownerDetailsLiveData: LiveData<EmpResource<OwnerDetailsRes>>
        get() = _ownerDetailsLiveData

    fun ownerDetails(token: String) {
        viewModelScope.launch {
            _ownerDetailsLiveData.value = EmpResource.Loading
            _ownerDetailsLiveData.value = repository.ownerDetails(token)
        }
    }

    private val _ownerCommunityLiveData = MutableLiveData<EmpResource<OwnerCommunityListRes>>()
    val ownerCommunityLiveData: LiveData<EmpResource<OwnerCommunityListRes>>
        get() = _ownerCommunityLiveData


    fun ownerCommunity(token: String, model: HashMap<String, Any>) {
        viewModelScope.launch {
            _ownerCommunityLiveData.value = EmpResource.Loading
            _ownerCommunityLiveData.value = repository.ownerCommunityList(token, model)
        }
    }

    private val _ownerAdvertisementListLiveData =
        MutableLiveData<EmpResource<OwnerAdvertisementRes>>()
    val ownerAdvertisementLiveData: LiveData<EmpResource<OwnerAdvertisementRes>>
        get() = _ownerAdvertisementListLiveData


    fun ownerAdvertisement(token: String, _id: String) {
        viewModelScope.launch {
            _ownerAdvertisementListLiveData.value = EmpResource.Loading
            _ownerAdvertisementListLiveData.value = repository.ownerAdvertisementList(token, _id)
        }
    }

    private val _userGetAllMemberLiveData = MutableLiveData<EmpResource<GetAllMemberListRes>>()
    val userGetAllMemberLiveData: LiveData<EmpResource<GetAllMemberListRes>>
        get() = _userGetAllMemberLiveData

    fun getAllMember(token: String, projectId: String) {
        viewModelScope.launch {
            _userGetAllMemberLiveData.value = EmpResource.Loading
            _userGetAllMemberLiveData.value = repository.getAllMember(token, projectId)
        }
    }

    private val _ownerMyCommunityLiveData = MutableLiveData<EmpResource<OwnerMyCommunityListRes>>()
    val ownerMyCommunityLiveData: LiveData<EmpResource<OwnerMyCommunityListRes>>
        get() = _ownerMyCommunityLiveData

    fun ownerMyCommunityList(token: String, model: OwnerCommunityPostModel) {
        viewModelScope.launch {
            _ownerMyCommunityLiveData.value = EmpResource.Loading
            _ownerMyCommunityLiveData.value = repository.ownerMyCommunityList(token, model)
        }
    }

    private val _likeownerCommunityLiveData = MutableLiveData<EmpResource<OwnerLikeCommunityRes>>()
    val likeownerCommunityLiveData: LiveData<EmpResource<OwnerLikeCommunityRes>>
        get() = _likeownerCommunityLiveData

    fun likeownerCommunity(token: String, model: OwnerLikeCommunityPostModel) {
        viewModelScope.launch {
            _likeownerCommunityLiveData.value = EmpResource.Loading
            _likeownerCommunityLiveData.value = repository.likeownerCommunity(token, model)
        }
    }

    private val _ownerFlatListLiveData = MutableLiveData<EmpResource<OwnerFlatListRes>>()
    val ownerFlatListLiveData: LiveData<EmpResource<OwnerFlatListRes>>
        get() = _ownerFlatListLiveData

    private val _ownerFlatBuildingIDLiveData = MutableLiveData<String>()
    val ownerFlatBuildingIDLiveData: MutableLiveData<String>
        get() = _ownerFlatBuildingIDLiveData

    private val _ownerFilterKeyLiveData = MutableLiveData<FIlter_MonthsModel>()
    val ownerFilterKeyLiveData: MutableLiveData<FIlter_MonthsModel>
        get() = _ownerFilterKeyLiveData

  /*  private val _ownerBottomMonthsFilterLiveData = MutableLiveData<String>()
    val ownerBottomMonthsFilterLiveData: MutableLiveData<String>
        get() = _ownerBottomMonthsFilterLiveData*/

    fun ownerFlatList(token: String) {
        viewModelScope.launch {
            _ownerFlatListLiveData.value = EmpResource.Loading
            _ownerFlatListLiveData.value = repository.ownerFlatList(token)
        }
    }

    private val _tenantFlatListLiveData = MutableLiveData<EmpResource<OwnerFlatListRes>>()
    val tenantFlatListLiveData: LiveData<EmpResource<OwnerFlatListRes>>
        get() = _tenantFlatListLiveData

    fun tenantFlatList(token: String) {
        viewModelScope.launch {
            _tenantFlatListLiveData.value = EmpResource.Loading
            _tenantFlatListLiveData.value = repository.tenantFlatList(token)
        }
    }

    private val _ownerParkingListLiveData = MutableLiveData<EmpResource<OwnerParkingListRes>>()
    val ownerParkingListLiveData: LiveData<EmpResource<OwnerParkingListRes>>
        get() = _ownerParkingListLiveData

    fun ownerParkingList(token: String) {
        viewModelScope.launch {
            _ownerParkingListLiveData.value = EmpResource.Loading
            _ownerParkingListLiveData.value = repository.ownerParkingtList(token)
        }
    }

    private val _addOwnerTenantLiveData = MutableLiveData<EmpResource<CommonResponse>>()
    val addOwnerTenantLiveData: LiveData<EmpResource<CommonResponse>>
        get() = _addOwnerTenantLiveData


    fun addOwnerTenant(token: String, model: OwnerAddTenantPostModel) {
        viewModelScope.launch {
            _addOwnerTenantLiveData.value = EmpResource.Loading
            _addOwnerTenantLiveData.value = repository.addOwnerTenant(token, model)
        }
    }


    private val _updateOwnerTenantLiveData = MutableLiveData<EmpResource<CommonResponse>>()
    val updateOwnerTenantLiveData: LiveData<EmpResource<CommonResponse>>
        get() = _updateOwnerTenantLiveData


    fun updateOwnerTenant(token: String, model: OwnerUpdateTenantPostModel) {
        viewModelScope.launch {
            _updateOwnerTenantLiveData.value = EmpResource.Loading
            _updateOwnerTenantLiveData.value = repository.updateOwnerTenant(token, model)
        }
    }


    private val _getOwnerCurrentHistoryLiveData =
        MutableLiveData<EmpResource<OwnerTenantCurrentHistoryListRes>>()
    val ownerCurrentHistoryLiveData: LiveData<EmpResource<OwnerTenantCurrentHistoryListRes>>
        get() = _getOwnerCurrentHistoryLiveData

    fun getOwnerCurrentHistory(token: String, type: String, flatId: String) {
        viewModelScope.launch {
            _getOwnerCurrentHistoryLiveData.value = EmpResource.Loading
            _getOwnerCurrentHistoryLiveData.value =
                repository.getOwnerCurrentHistory(token, type, flatId)
        }
    }


    private val _deleteOwnerTenantHistoryLiveData =
        MutableLiveData<EmpResource<CommonResponse>>()
    val deleteOwnerTenantLiveData: LiveData<EmpResource<CommonResponse>>
        get() = _deleteOwnerTenantHistoryLiveData

    fun deleteOwnerTenant(token: String, tenantId: String) {
        viewModelScope.launch {
            _deleteOwnerTenantHistoryLiveData.value = EmpResource.Loading
            _deleteOwnerTenantHistoryLiveData.value = repository.deleteOwnerTenant(token, tenantId)
        }
    }

    private val _gateKeeperOwnerTenantHistoryLiveData =
        MutableLiveData<EmpResource<OwnerGateKeeperList>>()
    val gateKeeperOwnerTenantLiveData: LiveData<EmpResource<OwnerGateKeeperList>>
        get() = _gateKeeperOwnerTenantHistoryLiveData

    fun gateKeeperOwnerTenant(token: String, buildingId: String) {
        viewModelScope.launch {
            _gateKeeperOwnerTenantHistoryLiveData.value = EmpResource.Loading
            _gateKeeperOwnerTenantHistoryLiveData.value =
                repository.gateKeeperOwnerTenant(token, buildingId)
        }
    }

    private val _viewPostCountOwnerLiveData =
        MutableLiveData<EmpResource<ViewPostCountList>>()
    val viewPostCountOwnerLiveData: LiveData<EmpResource<ViewPostCountList>>
        get() = _viewPostCountOwnerLiveData

    fun viewPostCountOwner(token: String, _id: String) {
        viewModelScope.launch {
            _viewPostCountOwnerLiveData.value = EmpResource.Loading
            _viewPostCountOwnerLiveData.value = repository.viewPostCountOwner(token, _id)
        }
    }


    private val _setAsHomeOwnerLiveData =
        MutableLiveData<EmpResource<OwnerSetasHomeList>>()
    val setAsHomeOwnerLiveData: LiveData<EmpResource<OwnerSetasHomeList>>
        get() = _setAsHomeOwnerLiveData

    fun setAsHomeOwner(token: String, flatId: String) {
        viewModelScope.launch {
            _setAsHomeOwnerLiveData.value = EmpResource.Loading
            _setAsHomeOwnerLiveData.value = repository.setAsHomeOwner(token, flatId)
        }
    }
    private val _addOwnerRentLiveData =
        MutableLiveData<EmpResource<ManagerAddServiceChargesListRes>>()
    val addOwnerRentLiveData: LiveData<EmpResource<ManagerAddServiceChargesListRes>>
        get() = _addOwnerRentLiveData

    fun addOwnerRent(token: String, model: /*ManagerAddServiceChargePostModel*/AddRentManagerPostModel) {
        viewModelScope.launch {
            _addOwnerRentLiveData.value = EmpResource.Loading
            _addOwnerRentLiveData.value = repository.addOwnerRent(token, model)
        }
    }

    private val _editOwnerRentLiveData =
        MutableLiveData<EmpResource<EditPendingBillManagerRes>>()
    val editOwnerRentLiveData: LiveData<EmpResource<EditPendingBillManagerRes>>
        get() = _editOwnerRentLiveData

    fun editOwnerRent(token: String, model: RentEditManagerPostModel) {
        viewModelScope.launch {
            _editOwnerRentLiveData.value = EmpResource.Loading
            _editOwnerRentLiveData.value = repository.editOwnerRent(token, model)
        }
    }

    private val _getOwnerRentLiveData =
        MutableLiveData<EmpResource<RentManagerListRes>>()
    val getOwnerRentLiveData: LiveData<EmpResource<RentManagerListRes>>
        get() = _getOwnerRentLiveData

    fun getOwnerRent(token: String) {
        viewModelScope.launch {
            _getOwnerRentLiveData.value = EmpResource.Loading
            _getOwnerRentLiveData.value = repository.getOwnerRentApi(token)
        }
    }

    private val _deleteOwnerRentLiveData = MutableLiveData<EmpResource<DeleteUnPaidManagerRes>>()
    val deleteOwnerRentLiveData: LiveData<EmpResource<DeleteUnPaidManagerRes>>
        get() = _deleteOwnerRentLiveData

    fun deleteOwnerRent(token: String, billId: String) {
        viewModelScope.launch {
            _deleteOwnerRentLiveData.value = EmpResource.Loading
            _deleteOwnerRentLiveData.value = repository.deleteOwnerRentRes(token, billId)
        }
    }
}