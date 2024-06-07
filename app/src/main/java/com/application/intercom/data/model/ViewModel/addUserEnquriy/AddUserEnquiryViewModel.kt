package com.application.intercom.data.model.ViewModel.addUserEnquriy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.intercom.data.model.local.BuySubscribe.BuySubscribePost
import com.application.intercom.data.model.local.addUserEnquiryPost.AddUserEnquiryPostModel
import com.application.intercom.data.model.remote.CommonResponse
import com.application.intercom.data.model.remote.adduserEnquiry.AddUserEnquiryRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.UserPlanRespo.UserPlanRepo
import com.application.intercom.data.repository.userAddEnquiry.UserAddEnquiryRepo
import kotlinx.coroutines.launch

class AddUserEnquiryViewModel(private val repository: UserAddEnquiryRepo) : ViewModel() {
    private val _addUserEnquiryLiveData = MutableLiveData<EmpResource<AddUserEnquiryRes>>()
    val addUserEnquiryLiveData: LiveData<EmpResource<AddUserEnquiryRes>>
        get() = _addUserEnquiryLiveData

    fun addUserEnquiry(token: String, model: AddUserEnquiryPostModel) {
        viewModelScope.launch {
            _addUserEnquiryLiveData.value = EmpResource.Loading
            _addUserEnquiryLiveData.value = repository.addUserEnquiry(token, model)
        }
    }
}