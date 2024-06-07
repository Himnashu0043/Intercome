package com.application.intercom.data.model.factory.getUserFlat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.application.intercom.data.model.ViewModel.addUserEnquriy.AddUserEnquiryViewModel
import com.application.intercom.data.model.ViewModel.getUserFlat.GetUserFlatViewModel
import com.application.intercom.data.repository.getUserFlat.UserFlatRepo
import com.application.intercom.data.repository.userAddEnquiry.UserAddEnquiryRepo

class GetUserFlatFactory(private val repository: UserFlatRepo) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GetUserFlatViewModel(repository) as T
    }
}