package com.application.intercom.data.model.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.intercom.data.model.local.BuySubscribe.BuySubscribePost
import com.application.intercom.data.model.local.UserBuySubscriprtionPostModel
import com.application.intercom.data.model.remote.CommonResponse
import com.application.intercom.data.model.remote.UserPlanList.UserPlanListRes
import com.application.intercom.data.repository.EmpResource

import com.application.intercom.data.repository.UserPlanRespo.UserPlanRepo
import kotlinx.coroutines.launch

class UserPlanViewModel(private val repository: UserPlanRepo) : ViewModel() {
    private val _userPlanLiveData = MutableLiveData<EmpResource<UserPlanListRes>>()
    val userPlanLiveData: LiveData<EmpResource<UserPlanListRes>>
        get() = _userPlanLiveData

    fun userPlan(token: String) {
        viewModelScope.launch {
            _userPlanLiveData.value = EmpResource.Loading
            _userPlanLiveData.value = repository.userPlan(token)
        }
    }

    private val _buySubLiveData = MutableLiveData<EmpResource<CommonResponse>>()
    val buySubscriptionLiveData: LiveData<EmpResource<CommonResponse>>
        get() = _buySubLiveData

    fun buySubscription(token: String, model: BuySubscribePost) {
        viewModelScope.launch {
            _buySubLiveData.value = EmpResource.Loading
            _buySubLiveData.value = repository.buySubscription(token, model)
        }
    }
    private val _userBuySubscriptionLiveData = MutableLiveData<EmpResource<CommonResponse>>()
    val userBuySubscriptionLiveData: LiveData<EmpResource<CommonResponse>>
        get() = _userBuySubscriptionLiveData

    fun userBuySubscription(token: String, model: UserBuySubscriprtionPostModel) {
        viewModelScope.launch {
            _userBuySubscriptionLiveData.value = EmpResource.Loading
            _userBuySubscriptionLiveData.value =
                repository.userBuySubscription(token, model)
        }
    }
}