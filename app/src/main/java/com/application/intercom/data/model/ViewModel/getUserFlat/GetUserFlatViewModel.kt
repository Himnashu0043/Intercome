package com.application.intercom.data.model.ViewModel.getUserFlat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.intercom.data.model.remote.UserPlanList.UserPlanListRes
import com.application.intercom.data.model.remote.userFlatData.UserFlatListRes
import com.application.intercom.data.model.remote.userParkingActivityData.UserParkingActivityListRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.UserPlanRespo.UserPlanRepo
import com.application.intercom.data.repository.getUserFlat.UserFlatRepo
import kotlinx.coroutines.launch

class GetUserFlatViewModel(private val repository: UserFlatRepo) : ViewModel() {
    private val _getUserFlatLiveData = MutableLiveData<EmpResource<UserFlatListRes>>()
    val getuserFlatLiveData: LiveData<EmpResource<UserFlatListRes>>
        get() = _getUserFlatLiveData

    fun userFlatData(token: String) {
        viewModelScope.launch {
            _getUserFlatLiveData.value = EmpResource.Loading
            _getUserFlatLiveData.value = repository.getUserFlat(token)
        }
    }

    private val _getUserParkingActivityLiveData =
        MutableLiveData<EmpResource<UserParkingActivityListRes>>()
    val getuserParkingActivityLiveData: LiveData<EmpResource<UserParkingActivityListRes>>
        get() = _getUserParkingActivityLiveData

    fun userParkingActivityData(token: String) {
        viewModelScope.launch {
            _getUserParkingActivityLiveData.value = EmpResource.Loading
            _getUserParkingActivityLiveData.value = repository.getUserParkingActivity(token)
        }
    }
}