package com.application.intercom.data.model.ViewModel.tenantViewModel.tenantHomeViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.intercom.data.model.remote.owner.ownerHome.OwnerDetailsRes
import com.application.intercom.data.model.remote.tenant.tenantHome.AdvertisementTenantRes
import com.application.intercom.data.model.remote.tenant.tenantHome.TenantDetailsRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.ownerRepo.ownerHome.OwnerHomeRepo
import com.application.intercom.data.repository.tenantRepo.tenantHomeRepo.TenantHomeRepo
import kotlinx.coroutines.launch

class TenantHomeViewModel(private val repository: TenantHomeRepo) : ViewModel(){
    private val _tenantDetailsLiveData = MutableLiveData<EmpResource<TenantDetailsRes>>()
    val tenantDetailsLiveData: LiveData<EmpResource<TenantDetailsRes>>
        get() = _tenantDetailsLiveData

    fun tenantDetails(token: String) {
        viewModelScope.launch {
            _tenantDetailsLiveData.value = EmpResource.Loading
            _tenantDetailsLiveData.value = repository.tenantDetails(token)
        }
    }

    private val _advertisementTenantLiveData =
        MutableLiveData<EmpResource<AdvertisementTenantRes>>()
    val advertisementTenantLiveData: LiveData<EmpResource<AdvertisementTenantRes>>
        get() = _advertisementTenantLiveData

    fun advertisementTenantRes(token: String, _id: String) {
        viewModelScope.launch {
            _advertisementTenantLiveData.value = EmpResource.Loading
            _advertisementTenantLiveData.value = repository.advertisementTenantRes(token, _id)
        }
    }
}