package com.application.intercom.user.service

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.intercom.data.model.remote.ServicesListResponse
import com.application.intercom.data.model.remote.UserServiceProviderResponse
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.ServiceRepository
import kotlinx.coroutines.launch

class ServiceViewModel(private val repository: ServiceRepository) : ViewModel() {

    private val _serviceLiveData = MutableLiveData<EmpResource<ServicesListResponse>>()
    val serviceLiveData: LiveData<EmpResource<ServicesListResponse>>
        get() = _serviceLiveData

    fun getServicesListAndSearch(token: String, searchKey: String) {
        viewModelScope.launch {
            _serviceLiveData.value = EmpResource.Loading
            _serviceLiveData.value = repository.normalUserServicesList(token, searchKey)
        }
    }

    private val _serviceProviderListLiveData =
        MutableLiveData<EmpResource<UserServiceProviderResponse>>()
    val serviceProviderListLiveData: LiveData<EmpResource<UserServiceProviderResponse>>
        get() = _serviceProviderListLiveData

    fun getServiceProviderList(token: String, category_Id: String) {
        viewModelScope.launch {
            _serviceProviderListLiveData.value = EmpResource.Loading
            _serviceProviderListLiveData.value =
                repository.normalUserServiceProviderList(token, category_Id)
        }
    }


}