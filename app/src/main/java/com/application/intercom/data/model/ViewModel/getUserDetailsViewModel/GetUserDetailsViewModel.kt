package com.application.intercom.data.model.ViewModel.getUserDetailsViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.intercom.data.model.remote.UserPlanList.UserPlanListRes
import com.application.intercom.data.model.remote.getUserDetails.GetUserDetailsRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.UserPlanRespo.UserPlanRepo
import com.application.intercom.data.repository.getUserDetailsRepo.GetUserDetailsRepo
import kotlinx.coroutines.launch

class GetUserDetailsViewModel(private val repository: GetUserDetailsRepo) : ViewModel() {
    private val _userDetailsLiveData = MutableLiveData<EmpResource<GetUserDetailsRes>>()
    val userDetailsLiveData: LiveData<EmpResource<GetUserDetailsRes>>
        get() = _userDetailsLiveData

    fun userDetails(token: String) {
        viewModelScope.launch {
            _userDetailsLiveData.value = EmpResource.Loading
            _userDetailsLiveData.value = repository.userDetails(token)
        }
    }

}