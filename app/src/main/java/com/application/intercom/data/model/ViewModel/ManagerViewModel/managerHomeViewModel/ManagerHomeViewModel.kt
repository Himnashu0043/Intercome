package com.application.intercom.data.model.ViewModel.ManagerViewModel.managerHomeViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.intercom.data.model.remote.manager.managerHome.ManagerGetDetailsRes
import com.application.intercom.data.model.remote.manager.managerParking.ManagerParkingListRes
import com.application.intercom.data.model.remote.manager.managerParking.ManagerParkingToletListRes
import com.application.intercom.data.model.remote.manager.managerProperty.ManagerPropertyListRes
import com.application.intercom.data.model.remote.manager.managerProperty.ManagerTenantHistoryList
import com.application.intercom.data.model.remote.manager.managerProperty.ManagerToletFlatListRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.managerRepo.managerHomeRepo.ManagerHomeRepo
import kotlinx.coroutines.launch

class ManagerHomeViewModel(private val repository: ManagerHomeRepo) : ViewModel() {
    private val _managerDetailsLiveData = MutableLiveData<EmpResource<ManagerGetDetailsRes>>()
    val managerDetailsLiveData: LiveData<EmpResource<ManagerGetDetailsRes>>
        get() = _managerDetailsLiveData

    fun managerDetails(token: String) {
        viewModelScope.launch {
            _managerDetailsLiveData.value = EmpResource.Loading
            _managerDetailsLiveData.value = repository.managerDetails(token)
        }
    }

    private val _managerFlatListLiveData = MutableLiveData<EmpResource<ManagerPropertyListRes>>()
    val managerFlatListLiveData: LiveData<EmpResource<ManagerPropertyListRes>>
        get() = _managerFlatListLiveData

    fun managerFlatList(token: String) {
        viewModelScope.launch {
            _managerFlatListLiveData.value = EmpResource.Loading
            _managerFlatListLiveData.value = repository.managerFlatList(token)
        }
    }

    private val _managerToLetFlatListLiveData =
        MutableLiveData<EmpResource<ManagerToletFlatListRes>>()
    val managerToLetFlatListLiveData: LiveData<EmpResource<ManagerToletFlatListRes>>
        get() = _managerToLetFlatListLiveData

    fun managerToLetFlatList(token: String) {
        viewModelScope.launch {
            _managerToLetFlatListLiveData.value = EmpResource.Loading
            _managerToLetFlatListLiveData.value = repository.managerToLetFlatList(token)
        }
    }

    private val _managerParkingListLiveData = MutableLiveData<EmpResource<ManagerParkingListRes>>()
    val managerParkingListLiveData: LiveData<EmpResource<ManagerParkingListRes>>
        get() = _managerParkingListLiveData

    fun managerParkingList(token: String) {
        viewModelScope.launch {
            _managerParkingListLiveData.value = EmpResource.Loading
            _managerParkingListLiveData.value = repository.managerParkingList(token)
        }
    }

    private val _managerToletParkingListLiveData =
        MutableLiveData<EmpResource<ManagerParkingToletListRes>>()
    val managerToLetParkingListLiveData: LiveData<EmpResource<ManagerParkingToletListRes>>
        get() = _managerToletParkingListLiveData

    fun managerToLetParkingList(token: String) {
        viewModelScope.launch {
            _managerToletParkingListLiveData.value = EmpResource.Loading
            _managerToletParkingListLiveData.value = repository.managerParkingToletList(token)
        }
    }

    private val _managerTenantHistoryListLiveData =
        MutableLiveData<EmpResource<ManagerTenantHistoryList>>()
    val managerTenantHistoryListLiveData: LiveData<EmpResource<ManagerTenantHistoryList>>
        get() = _managerTenantHistoryListLiveData

    fun managerTenantHistoryList(token: String, flatId: String) {
        viewModelScope.launch {
            _managerTenantHistoryListLiveData.value = EmpResource.Loading
            _managerTenantHistoryListLiveData.value =
                repository.managerTenantHistoryList(token, flatId)
        }
    }
}