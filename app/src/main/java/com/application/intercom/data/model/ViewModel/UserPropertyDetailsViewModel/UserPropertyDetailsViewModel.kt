package com.application.intercom.data.model.ViewModel.UserPropertyDetailsViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.intercom.data.model.local.UserPropertyDetailsPostModel.UserPropertyDetailsPostModel
import com.application.intercom.data.model.remote.UserPropertyDetails.UserPostPropertyListRes
import com.application.intercom.data.model.remote.UserPropertyDetails.UserPropertyDetailsRes
import com.application.intercom.data.model.remote.userParkingDetails.UserParkingDetailsRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.UserPropertyDetailsRepo.UserPropertyDetailsRepo
import kotlinx.coroutines.launch

class UserPropertyDetailsViewModel(private val repository: UserPropertyDetailsRepo) : ViewModel() {
    private val _userPropertyDetailsLiveData = MutableLiveData<EmpResource<UserPropertyDetailsRes>>()
    val userPropertyDetailsLiveData: LiveData<EmpResource<UserPropertyDetailsRes>>
        get() = _userPropertyDetailsLiveData

    fun userPropertyDetail(token: String, model: UserPropertyDetailsPostModel) {
        viewModelScope.launch {
            _userPropertyDetailsLiveData.value = EmpResource.Loading
            _userPropertyDetailsLiveData.value = repository.userPropertyDetails(token, model)
        }
    }
    private val _userPostPropertyDetailsLiveData = MutableLiveData<EmpResource<UserPostPropertyListRes>>()
    val userPostPropertyDetailsLiveData: LiveData<EmpResource<UserPostPropertyListRes>>
        get() = _userPostPropertyDetailsLiveData

    fun userPostPropertyDetail(token: String, model: UserPropertyDetailsPostModel) {
        viewModelScope.launch {
            _userPostPropertyDetailsLiveData.value = EmpResource.Loading
            _userPostPropertyDetailsLiveData.value = repository.userPostPropertyDetails(token, model)
        }
    }

    private val _userParkingDetailsLiveData = MutableLiveData<EmpResource<UserParkingDetailsRes>>()
    val userParkingDetailsLiveData: LiveData<EmpResource<UserParkingDetailsRes>>
        get() = _userParkingDetailsLiveData

    fun userParkingDetail(token: String, model: UserPropertyDetailsPostModel) {
        viewModelScope.launch {
            _userParkingDetailsLiveData.value = EmpResource.Loading
            _userParkingDetailsLiveData.value = repository.userParkingDetails(token, model)
        }
    }
}