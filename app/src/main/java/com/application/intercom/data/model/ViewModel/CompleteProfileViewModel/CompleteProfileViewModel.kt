package com.application.intercom.data.model.ViewModel.CompleteProfileViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.intercom.data.model.local.UserUpdateProfileLocalModel
import com.application.intercom.data.model.remote.CommonResponse
import com.application.intercom.data.repository.CompletProfile.CompleteProfileRepo
import com.application.intercom.data.repository.EmpResource
import kotlinx.coroutines.launch

class CompleteProfileViewModel(private val repository: CompleteProfileRepo) : ViewModel() {
    private val _completeProfileLiveData = MutableLiveData<EmpResource<CommonResponse>>()
    val completeProfileLiveData: LiveData<EmpResource<CommonResponse>>
        get() = _completeProfileLiveData

    fun completeProfile(token: String,model: UserUpdateProfileLocalModel) {
        viewModelScope.launch {
            _completeProfileLiveData.value = EmpResource.Loading
            _completeProfileLiveData.value = repository.completeProfile(token,model)
        }
    }
}