package com.application.intercom.data.model.ViewModel.gateKeeperViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.intercom.data.model.local.gateKeeper.AddGatePassPostModel
import com.application.intercom.data.model.local.gateKeeper.AddRegularVisitorEntryPostModel
import com.application.intercom.data.model.local.gateKeeper.AddSingleEntryGateKeeperPostModel
import com.application.intercom.data.model.remote.CommonResponse
import com.application.intercom.data.model.remote.gateKeeper.gateKeeperHome.*
import com.application.intercom.data.model.remote.manager.managerHome.ManagerGetDetailsRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.gateKeeperRepo.GateKeeperHomeRepo
import kotlinx.coroutines.launch

class GateKeeperHomeViewModel(private val repository: GateKeeperHomeRepo) : ViewModel() {
    private val _gateKeeperDetailsLiveData = MutableLiveData<EmpResource<GateKeeperDetailsRes>>()
    val gateKeeperDetailsLiveData: LiveData<EmpResource<GateKeeperDetailsRes>>
        get() = _gateKeeperDetailsLiveData

    fun gateKeeperDetails(token: String) {
        viewModelScope.launch {
            _gateKeeperDetailsLiveData.value = EmpResource.Loading
            _gateKeeperDetailsLiveData.value = repository.gateKeeperDetails(token)
        }
    }

    private val _flatOfBuildingListLiveData =
        MutableLiveData<EmpResource<FlatListOfBuildingGateKeeperListRes>>()
    val flatOfBuildingListLiveData: LiveData<EmpResource<FlatListOfBuildingGateKeeperListRes>>
        get() = _flatOfBuildingListLiveData

    fun flatOfBuildingList(token: String) {
        viewModelScope.launch {
            _flatOfBuildingListLiveData.value = EmpResource.Loading
            _flatOfBuildingListLiveData.value = repository.flatOfBuildingList(token)
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

    private val _addSingleEntryLiveData =
        MutableLiveData<EmpResource<CommonResponse>>()
    val addSingleEntryLiveData: LiveData<EmpResource<CommonResponse>>
        get() = _addSingleEntryLiveData

    fun addSingleEntry(token: String, model: AddSingleEntryGateKeeperPostModel) {
        viewModelScope.launch {
            _addSingleEntryLiveData.value = EmpResource.Loading
            _addSingleEntryLiveData.value = repository.addSingleEntry(token, model)
        }
    }

    private val _gateKeeperListLiveData =
        MutableLiveData<EmpResource<GateKeeperListRes>>()
    val gateKeeperListLiveData: LiveData<EmpResource<GateKeeperListRes>>
        get() = _gateKeeperListLiveData

    fun gateKeeperList(token: String,buildingId:String?,flatId:String?,status:String) {
        viewModelScope.launch {
            _gateKeeperListLiveData.value = EmpResource.Loading
            _gateKeeperListLiveData.value = repository.gateKeeperList(token,buildingId,flatId,status)
        }
    }

    private val _exitGatePassLiveData =
        MutableLiveData<EmpResource<CommonResponse>>()
    val exitGatePassLiveData: LiveData<EmpResource<CommonResponse>>
        get() = _exitGatePassLiveData

    fun exitGatePass(token: String,gatePassId: String) {
        viewModelScope.launch {
            _exitGatePassLiveData.value = EmpResource.Loading
            _exitGatePassLiveData.value = repository.exitGatePass(token,gatePassId)
        }
    }

    private val _addGatePassLiveData =
        MutableLiveData<EmpResource<AddGatePassRes>>()
    val addGatePassLiveData: LiveData<EmpResource<AddGatePassRes>>
        get() = _addGatePassLiveData

    fun addGatePass(token: String, model: AddGatePassPostModel) {
        viewModelScope.launch {
            _addGatePassLiveData.value = EmpResource.Loading
            _addGatePassLiveData.value = repository.addGatePass(token, model)
        }
    }

    private val _singleEntryHistroyListLiveData =
        MutableLiveData<EmpResource<SingleEntryHistoryList>>()
    val singleEntryHistoryListLiveData: LiveData<EmpResource<SingleEntryHistoryList>>
        get() = _singleEntryHistroyListLiveData

    fun singleEntryHistoryList(token: String, visitorStatus: String,flatId: String) {
        viewModelScope.launch {
            _singleEntryHistroyListLiveData.value = EmpResource.Loading
            _singleEntryHistroyListLiveData.value =
                repository.singleEntryHistorylist(token, visitorStatus,flatId)
        }
    }

    private val _visitorNotifyToUserLiveData =
        MutableLiveData<EmpResource<VisitorNotifyToUserRes>>()
    val visitorNotifyToUserLiveData: LiveData<EmpResource<VisitorNotifyToUserRes>>
        get() = _visitorNotifyToUserLiveData

    fun visitorNotifyToUser(token: String, visitorId: String) {
        viewModelScope.launch {
            _visitorNotifyToUserLiveData.value = EmpResource.Loading
            _visitorNotifyToUserLiveData.value =
                repository.visitorNotifyToUser(token, visitorId)
        }
    }

    private val _flatListOfVisitorLiveData =
        MutableLiveData<EmpResource<FlatListOfVisitorGateKeeperList>>()
    val flatListOfVisitorLiveData: LiveData<EmpResource<FlatListOfVisitorGateKeeperList>>
        get() = _flatListOfVisitorLiveData

    fun flatListOfVisitor(token: String, flatId: String) {
        viewModelScope.launch {
            _flatListOfVisitorLiveData.value = EmpResource.Loading
            _flatListOfVisitorLiveData.value =
                repository.flatListOfVisitor(token, flatId)
        }
    }

    private val _addRegularVisitorEntryLiveData =
        MutableLiveData<EmpResource<AddRegularVisitorEntryRes>>()
    val addRegularVisitorEntryLiveData: LiveData<EmpResource<AddRegularVisitorEntryRes>>
        get() = _addRegularVisitorEntryLiveData

    fun addRegularVisitorEntry(token: String, model: AddRegularVisitorEntryPostModel) {
        viewModelScope.launch {
            _addRegularVisitorEntryLiveData.value = EmpResource.Loading
            _addRegularVisitorEntryLiveData.value =
                repository.addRegularVisitorEntry(token, model)
        }
    }

    private val _outRegularVisitorEntryLiveData =
        MutableLiveData<EmpResource<OutEntryGateKeeperRes>>()
    val outRegularVisitorEntryLiveData: LiveData<EmpResource<OutEntryGateKeeperRes>>
        get() = _outRegularVisitorEntryLiveData

    fun outRegularVisitorEntry(token: String, visitorId: String) {
        viewModelScope.launch {
            _outRegularVisitorEntryLiveData.value = EmpResource.Loading
            _outRegularVisitorEntryLiveData.value =
                repository.outRegularVisitorEntry(token, visitorId)
        }
    }

    private val _regularVisitorHistoryListLiveData =
        MutableLiveData<EmpResource<RegularVisitorGateKeeperList>>()
    val regularVisitorHistoryListLiveData: LiveData<EmpResource<RegularVisitorGateKeeperList>>
        get() = _regularVisitorHistoryListLiveData

    fun regularVisitorHistoryList(token: String, visitorStatus: String, flatId: String,buildingId:String) {
        viewModelScope.launch {
            _regularVisitorHistoryListLiveData.value = EmpResource.Loading
            _regularVisitorHistoryListLiveData.value =
                repository.regularVisitorHistoryGateKeeper(token, visitorStatus, flatId,buildingId)
        }
    }

    private val _regularEntryHistoryDetailsListLiveData =
        MutableLiveData<EmpResource<RegularEntryHistoryDetailsList>>()
    val regularEntryHistoryDetailsListLiveData: LiveData<EmpResource<RegularEntryHistoryDetailsList>>
        get() = _regularEntryHistoryDetailsListLiveData

    fun regularEntryHistoryDetailsList(token: String, visitorId: String) {
        viewModelScope.launch {
            _regularEntryHistoryDetailsListLiveData.value = EmpResource.Loading
            _regularEntryHistoryDetailsListLiveData.value =
                repository.regularEntryHistoryDetailsGateKeeper(token, visitorId)
        }
    }

    private val _outSingleVisitorEntryGateKeeperListLiveData =
        MutableLiveData<EmpResource<CommonResponse>>()
    val outSingleVisitorEntryGateKeeperLiveData: LiveData<EmpResource<CommonResponse>>
        get() = _outSingleVisitorEntryGateKeeperListLiveData

    fun outSingleVisitorEntryGateKeeper(token: String, visitorId: String) {
        viewModelScope.launch {
            _outSingleVisitorEntryGateKeeperListLiveData.value = EmpResource.Loading
            _outSingleVisitorEntryGateKeeperListLiveData.value =
                repository.outSingleVisitorEntryGateKeeper(token, visitorId)
        }
    }

    private val _gateKeeperProfileListLiveData =
        MutableLiveData<EmpResource<GateKeeperProfileList>>()
    val gateKeeperProfileLiveData: LiveData<EmpResource<GateKeeperProfileList>>
        get() = _gateKeeperProfileListLiveData

    fun gateKeeperProfile(token: String) {
        viewModelScope.launch {
            _gateKeeperProfileListLiveData.value = EmpResource.Loading
            _gateKeeperProfileListLiveData.value =
                repository.gateKeeperProfile(token)
        }
    }
}