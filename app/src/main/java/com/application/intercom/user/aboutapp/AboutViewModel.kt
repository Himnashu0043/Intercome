package com.application.intercom.user.aboutapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.intercom.data.model.remote.PrivacyPolicyResponse
import com.application.intercom.data.repository.AboutRepository
import com.application.intercom.data.repository.EmpResource
import kotlinx.coroutines.launch

class AboutViewModel(private val repository: AboutRepository) : ViewModel() {


    private val _privacyPolicyLiveData = MutableLiveData<EmpResource<PrivacyPolicyResponse>>()
    val privacyPolicyLiveData: LiveData<EmpResource<PrivacyPolicyResponse>>
        get() = _privacyPolicyLiveData

    fun userGetPrivacyPolicy(token: String) {
        viewModelScope.launch {
            _privacyPolicyLiveData.value = EmpResource.Loading
            _privacyPolicyLiveData.value = repository.userGetPrivacyPolicy(token)
        }
    }

    private val _termsOfServicesLiveData = MutableLiveData<EmpResource<PrivacyPolicyResponse>>()
    val termsOfServicesLiveData: LiveData<EmpResource<PrivacyPolicyResponse>>
        get() = _termsOfServicesLiveData

    fun userGetTermsOfServices(token: String) {
        viewModelScope.launch {
            _termsOfServicesLiveData.value = EmpResource.Loading
            _termsOfServicesLiveData.value = repository.userGetTermsOfService(token)
        }
    }


    private val _aboutUsLiveData = MutableLiveData<EmpResource<PrivacyPolicyResponse>>()
    val aboutUsLiveData: LiveData<EmpResource<PrivacyPolicyResponse>>
        get() = _aboutUsLiveData

    fun userGetAboutUs(token: String) {
        viewModelScope.launch {
            _aboutUsLiveData.value = EmpResource.Loading
            _aboutUsLiveData.value = repository.userGetAboutUs(token)
        }
    }
}